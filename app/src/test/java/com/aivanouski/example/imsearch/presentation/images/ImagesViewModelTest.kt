package com.aivanouski.example.imsearch.presentation.images

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.aivanouski.example.imsearch.domain.ErrorInteractor
import com.aivanouski.example.imsearch.domain.images.ImagesInteractor
import com.aivanouski.example.imsearch.model.Image
import com.aivanouski.example.imsearch.model.Result
import com.aivanouski.example.imsearch.presentation.Screens.ImageDetailsScreen
import com.aivanouski.example.imsearch.presentation.core.coroutines.CoroutineJobFactory
import com.aivanouski.example.imsearch.presentation.images.ImagesViewModel.ScreenState
import com.aivanouski.example.imsearch.presentation.images.recyclerView.ImageItemFactory
import com.aivanouski.example.imsearch.presentation.images.recyclerView.ImagesAdapter
import com.aivanouski.example.imsearch.testUtils.ImmediateCoroutineJobFactory
import com.aivanouski.example.imsearch.testUtils.LiveDataValueCaptor
import com.aivanouski.example.imsearch.testUtils.NeverExecutedCoroutineJobFactory
import com.aivanouski.example.imsearch.testUtils.TestData.DEFAULT_QUERY
import com.aivanouski.example.imsearch.testUtils.TestData.DELAY
import com.aivanouski.example.imsearch.testUtils.TestData.ERROR_MESSAGE
import com.aivanouski.example.imsearch.testUtils.TestData.IMAGE1
import com.aivanouski.example.imsearch.testUtils.TestData.IMAGE2
import com.aivanouski.example.imsearch.testUtils.TestData.IMAGES
import com.aivanouski.example.imsearch.testUtils.TestData.QUERY
import com.aivanouski.example.imsearch.testUtils.TestData.QUERY1
import com.aivanouski.example.imsearch.testUtils.TestData.QUERY2
import com.aivanouski.example.imsearch.testUtils.TestDispatcherProvider
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.Screen
import com.google.common.truth.Truth.assertThat
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import io.mockk.verifySequence
import java.lang.Exception
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.Rule
import org.junit.Test

class ImagesViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val interactor = mockk<ImagesInteractor>()
    private val errorInteractor = mockk<ErrorInteractor>()
    private val router = mockk<Router>()
    private val imageItemFactory = ImageItemFactory()
    private val dispatchers = TestDispatcherProvider()

    @Test
    fun `init should initialize loading state`() {
        // arrange

        // act
        val viewModel = newViewModel()

        // assert
        assertThat(viewModel.state.value).isEqualTo(ScreenState.LOADING)
    }

    @Test
    fun `init should set default query from default config`() {
        // act
        val viewModel = newViewModel(config = ImagesViewModel.DEFAULT_CONFIG)

        // assert
        assertThat(viewModel.query.value).isEqualTo(ImagesViewModel.DEFAULT_CONFIG.initQuery)
    }

    @Test
    fun `loadData should should show images`() {
        // arrange
        val viewModel = newViewModel()
        every { interactor.getImages(any()) }.returns(resultFlow(IMAGES))

        // act
        viewModel.loadData()

        // assert
        verify { interactor.getImages(any()) }

        assertThat(viewModel.state.value).isEqualTo(ScreenState.DATA)
        assertThat(viewModel.items.value).isEqualTo(IMAGES.toAdapterItems())
    }

    @Test
    fun `loadData should skip data loading if it is already loaded`() {
        // arrange
        val viewModel = newViewModel()
        val itemsDataCollector = LiveDataValueCaptor(viewModel.items)
        every { interactor.getImages(any()) }.returns(resultFlow(IMAGES))

        // act
        viewModel.loadData()
        viewModel.loadData()

        // assert
        verify { interactor.getImages(any()) }

        assertThat(viewModel.state.value).isEqualTo(ScreenState.DATA)
        assertThat(itemsDataCollector.capturedValues.size).isEqualTo(1)
        assertThat(viewModel.items.value).isEqualTo(IMAGES.toAdapterItems())
    }

    @Test
    fun `loadData should show empty state`() {
        // arrange
        val viewModel = newViewModel()
        val images = emptyList<Image>()
        every { interactor.getImages(any()) }.returns(resultFlow(images))

        // act
        viewModel.loadData()

        // assert
        verify { interactor.getImages(any()) }

        assertThat(viewModel.state.value).isEqualTo(ScreenState.EMPTY)
    }

    @Test
    fun `loadData should show cached images`() {
        // arrange
        val viewModel = newViewModel()
        every { interactor.getImages(any()) }.returns(cacheFlow(IMAGES))

        // act
        viewModel.loadData()

        // assert
        verify { interactor.getImages(any()) }

        assertThat(viewModel.state.value).isEqualTo(ScreenState.DATA)
        assertThat(viewModel.items.value).isEqualTo(IMAGES.toAdapterItems(isUseImages = false))
    }

    @Test
    fun `loadData should show error`() {
        // arrange
        val viewModel = newViewModel()
        val error = Result.Error(Exception())
        every { interactor.getImages(any()) }.returns(errorFlow(error))
        every { errorInteractor.getMessage(error) }.returns(ERROR_MESSAGE)

        // act
        viewModel.loadData()

        // assert
        verify { interactor.getImages(any()) }
        verify { errorInteractor.getMessage(error) }

        assertThat(viewModel.state.value).isEqualTo(ScreenState.ERROR)
        assertThat(viewModel.errorText.value).isEqualTo(ERROR_MESSAGE)
    }

    @Test
    fun `onQueryTextChanged should trigger new search`() {
        // arrange
        val viewModel = newViewModel(defaultSearchQuery = DEFAULT_QUERY)
        val defaultImages = listOf(IMAGE1)
        val newImages = listOf(IMAGE2)
        every { interactor.getImages(DEFAULT_QUERY) }.returns(resultFlow(defaultImages))
        every { interactor.getImages(QUERY) }.returns(resultFlow(newImages))

        // act
        viewModel.loadData()
        viewModel.onQueryTextChanged(QUERY)

        // assert
        verifySequence {
            interactor.getImages(DEFAULT_QUERY)
            interactor.getImages(QUERY)
        }

        assertThat(viewModel.state.value).isEqualTo(ScreenState.DATA)
        assertThat(viewModel.items.value).isEqualTo(newImages.toAdapterItems())
    }

    @Test
    fun `onQueryTextChanged should pass default delay to CoroutineJobFactory`() {
        // arrange
        val coroutineFactory = mockk<CoroutineJobFactory>()
        val viewModel = newViewModel(
            coroutineJobFactory = coroutineFactory,
            config = ImagesViewModel.DEFAULT_CONFIG
        )
        val delay = ImagesViewModel.DEFAULT_CONFIG.searchDelay
        every { interactor.getImages(any()) }.returns(resultFlow(IMAGES))
        every { coroutineFactory.launchDelayed(any(), delay, any()) }.returns(mockk())

        // act
        viewModel.loadData()
        viewModel.onQueryTextChanged(QUERY)

        // assert
        verify { coroutineFactory.launchDelayed(any(), delay, any()) }
    }

    @Test
    fun `onQueryTextChanged should change state to loading`() {
        // arrange
        val viewModel = newViewModel(coroutineJobFactory = NeverExecutedCoroutineJobFactory())
        every { interactor.getImages(any()) }.returns(resultFlow(IMAGES))

        // act
        viewModel.loadData()
        viewModel.onQueryTextChanged(QUERY)

        // assert
        assertThat(viewModel.state.value).isEqualTo(ScreenState.LOADING)
    }

    @Test
    fun `onQueryTextChanged should cancel previous search`() {
        // arrange
        val job1 = mockk<Job>()
        val job2 = mockk<Job>()
        val coroutineFactory = mockk<CoroutineJobFactory>()
        val viewModel = newViewModel(coroutineJobFactory = coroutineFactory)
        every { interactor.getImages(any()) }.returns(resultFlow(IMAGES))
        every { coroutineFactory.launchDelayed(any(), DELAY, any()) }.returnsMany(job1, job2)
        every { job1.cancel() }.returns(Unit)

        // act
        viewModel.loadData()
        viewModel.onQueryTextChanged(QUERY1)
        viewModel.onQueryTextChanged(QUERY2)

        // assert
        verify { job1.cancel() }
    }

    @Test
    fun `onQueryTextChanged should not be triggered twice`() {
        // arrange
        val viewModel = newViewModel(defaultSearchQuery = DEFAULT_QUERY)
        every { interactor.getImages(DEFAULT_QUERY) }.returns(resultFlow(IMAGES))

        // act
        viewModel.loadData()
        viewModel.onQueryTextChanged(DEFAULT_QUERY)

        // assert
        verify { interactor.getImages(DEFAULT_QUERY) }
        confirmVerified(interactor)

        assertThat(viewModel.state.value).isEqualTo(ScreenState.DATA)
        assertThat(viewModel.items.value).isEqualTo(IMAGES.toAdapterItems())
    }

    @Test
    fun `onImageClicked should trigger dialog`() {
        // arrange
        val viewModel = newViewModel()

        // act
        viewModel.onImageClicked(IMAGE1)

        // assert
        assertThat(viewModel.showDetailsDialog.value?.peekContent()).isEqualTo(IMAGE1)
    }

    @Test
    fun `onShowDetailsConfirmed should navigate to ImageDetails screen`() {
        // arrange
        val viewModel = newViewModel()
        val slot = slot<Screen>()
        every { router.navigateTo(capture(slot)) }.returns(Unit)

        // act
        viewModel.onShowDetailsConfirmed(IMAGE1)

        // assert
        assertThat(slot.captured).isInstanceOf(ImageDetailsScreen::class.java)

        val screen = slot.captured as ImageDetailsScreen
        assertThat(screen.arguments.imageId).isEqualTo(IMAGE1.id)
    }

    private fun resultFlow(images: List<Image>): Flow<Result<List<Image>>> =
        flow {
            emit(Result.Success(images))
        }

    private fun cacheFlow(images: List<Image>): Flow<Result<List<Image>>> =
        flow {
            emit(Result.Cached(images))
        }

    private fun errorFlow(error: Result.Error): Flow<Result<List<Image>>> =
        flow {
            emit(error)
        }

    private fun List<Image>.toAdapterItems(isUseImages: Boolean = true): List<ImagesAdapter.ImageItem> {
        return ImageItemFactory().createAdapterItems(
            images = this,
            onImageClicked = {},
            isUseImages = isUseImages
        )
    }

    private fun newViewModel(
        defaultSearchQuery: String? = null,
        coroutineJobFactory: CoroutineJobFactory = ImmediateCoroutineJobFactory(),
        config: ImagesViewModel.ScreenConfig? = null
    ): ImagesViewModel {
        return ImagesViewModel(
            interactor = interactor,
            errorInteractor = errorInteractor,
            imageItemFactory = imageItemFactory,
            router = router,
            coroutineFactory = coroutineJobFactory,
            dispatchers = dispatchers,
            config = config ?: ImagesViewModel.ScreenConfig(
                initQuery = defaultSearchQuery ?: DEFAULT_QUERY,
                searchDelay = DELAY
            )
        )
    }
}