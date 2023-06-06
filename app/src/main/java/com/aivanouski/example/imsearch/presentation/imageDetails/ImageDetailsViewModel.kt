package com.aivanouski.example.imsearch.presentation.imageDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aivanouski.example.imsearch.di.Injector
import com.aivanouski.example.imsearch.domain.imageDetails.ImageDetailsInteractor
import com.aivanouski.example.imsearch.model.Image
import com.aivanouski.example.imsearch.model.Result
import com.aivanouski.example.imsearch.presentation.core.BaseCoroutineViewModel
import com.aivanouski.example.imsearch.presentation.core.coroutines.DispatcherProvider
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf

class ImageDetailsViewModel(
    private val interactor: ImageDetailsInteractor,
    private val router: Router,
    dispatchers: DispatcherProvider,
    private val arguments: ImageDetailsArguments
) : BaseCoroutineViewModel(dispatchers) {

    private val _url = MutableLiveData<String>()
    val url: LiveData<String> = _url

    private val _userImageUrl = MutableLiveData<String>()
    val userImageUrl: LiveData<String> = _userImageUrl

    private val _image = MutableLiveData<Image>()
    val image: LiveData<Image> = _image

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        loadData()
    }

    fun navigateBack() {
        router.exit()
    }

    private fun loadData() {
        viewModelScope.launch {
            interactor.getImage(arguments.imageId)
                .collectLatest { data -> onDataLoaded(data) }
        }
    }

    private fun onDataLoaded(data: Result<Image>) {
        if (data.isSucceeded()) {
            val image = data.unwrap()

            if (_url.value != image.url) {
                _url.value = image.url
            }
            if (image.userImageUrl.isNotEmpty()) {
                _userImageUrl.value = image.userImageUrl
            }
            _image.value = image
            _isLoading.value = false
        }
    }

    class Factory(
        private val arguments: ImageDetailsArguments
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return Injector.get<ImageDetailsViewModel>(
                parametersOf(arguments)
            ) as T
        }
    }
}