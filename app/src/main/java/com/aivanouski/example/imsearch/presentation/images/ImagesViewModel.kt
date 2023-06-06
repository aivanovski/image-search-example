package com.aivanouski.example.imsearch.presentation.images

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aivanouski.example.imsearch.domain.ErrorInteractor
import com.aivanouski.example.imsearch.domain.images.ImagesInteractor
import com.aivanouski.example.imsearch.model.Image
import com.aivanouski.example.imsearch.model.Result
import com.aivanouski.example.imsearch.presentation.Screens
import com.aivanouski.example.imsearch.presentation.core.BaseCoroutineViewModel
import com.aivanouski.example.imsearch.presentation.core.coroutines.CoroutineJobFactory
import com.aivanouski.example.imsearch.presentation.core.coroutines.DispatcherProvider
import com.aivanouski.example.imsearch.presentation.imageDetails.ImageDetailsArguments
import com.aivanouski.example.imsearch.presentation.images.recyclerView.ImageItemFactory
import com.aivanouski.example.imsearch.presentation.images.recyclerView.ImagesAdapter.ImageItem
import com.aivanouski.example.imsearch.utils.Event
import com.aivanouski.example.imsearch.utils.StringUtils.EMPTY
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class ImagesViewModel(
    private val interactor: ImagesInteractor,
    private val errorInteractor: ErrorInteractor,
    private val imageItemFactory: ImageItemFactory,
    private val router: Router,
    private val coroutineFactory: CoroutineJobFactory,
    dispatchers: DispatcherProvider,
    private val config: ScreenConfig
) : BaseCoroutineViewModel(dispatchers) {

    val query = MutableLiveData(config.initQuery)

    private val _items = MutableLiveData<List<ImageItem>>()
    val items: LiveData<List<ImageItem>> = _items

    private val _state = MutableLiveData(ScreenState.LOADING)
    val state: LiveData<ScreenState> = _state

    private val _errorText = MutableLiveData<String>()
    val errorText: LiveData<String> = _errorText

    private val _showDetailsDialog = MutableLiveData<Event<Image>>()
    val showDetailsDialog: LiveData<Event<Image>> = _showDetailsDialog

    private val searchFlow = MutableStateFlow(query.value ?: EMPTY)
    private var searchDelayJob: Job? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    fun loadData() {
        if (_state.value == ScreenState.DATA) {
            return
        }

        viewModelScope.launch {
            searchFlow.flatMapLatest { query -> interactor.getImages(query) }
                .collectLatest { data -> onDataLoaded(data) }
        }
    }

    fun onQueryTextChanged(query: String) {
        if (searchFlow.value == query) {
            return
        }

        _state.value = ScreenState.LOADING

        searchDelayJob?.cancel()
        searchDelayJob = coroutineFactory.launchDelayed(
            scope = viewModelScope,
            delayInMillis = config.searchDelay
        ) {
            searchFlow.value = query
            searchDelayJob = null
        }
    }

    fun onShowDetailsConfirmed(image: Image) {
        router.navigateTo(
            Screens.ImageDetailsScreen(
                arguments = ImageDetailsArguments(image.id)
            )
        )
    }

    private fun onDataLoaded(data: Result<List<Image>>) {
        if (data.isSucceeded()) {
            val images = data.unwrap()

            // Pixabay API returns unique urls for images each request,
            // as a result of that images blink on the screen.
            // In order to prevent blinking, cached urls will be used only
            // if it is impossible to get data from Pixabay API (e.g. network error, API error).
            val isUseImage = if (data is Result.Cached) {
                data.exception != null
            } else {
                true
            }

            if (images.isNotEmpty()) {
                _items.value = imageItemFactory.createAdapterItems(
                    images = images,
                    onImageClicked = this::onImageClicked,
                    isUseImages = isUseImage
                )
                _state.value = ScreenState.DATA
            } else {
                _state.value = ScreenState.EMPTY
            }
        } else {
            _errorText.value = errorInteractor.getMessage(data.unwrapError())
            _state.value = ScreenState.ERROR
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun onImageClicked(image: Image) {
        _showDetailsDialog.value = Event(image)
    }

    enum class ScreenState {
        LOADING,
        DATA,
        EMPTY,
        ERROR
    }

    data class ScreenConfig(
        val initQuery: String,
        val searchDelay: Long
    )

    companion object {
        val DEFAULT_CONFIG = ScreenConfig(
            initQuery = "fruits",
            searchDelay = 1000L
        )
    }
}