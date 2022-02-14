package hr.rainbow.domain.view_models

import com.google.common.truth.Truth.assertThat
import hr.rainbow.data.FakeDataRepository
import hr.rainbow.domain.model.Picture
import hr.rainbow.util.MainCoroutineRule
import hr.rainbow.util.UiEvents
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PictureGalleryViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: PictureGalleryViewModel
    private lateinit var repository: FakeDataRepository

    @Before
    fun setup() {
        repository = FakeDataRepository()
        viewModel = PictureGalleryViewModel(repository)
    }

    @After
    fun teardown() {
    }

//PICTURE GALLERY

    @Test
    fun `picture gallery - isLoading should be true when fetching is started`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = true, isErrorOn = false)

        var isLoading = false
        val job = launch {
            viewModel.pictureGallery.collect {
                isLoading = it.isLoading
            }
        }

        viewModel.fetchPictureGalleryData()
        assertThat(isLoading).isTrue()
        job.cancel()
    }

    @Test
    fun `picture gallery - isLoading should be false when fetching is done`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = false, isErrorOn = false)

        var isLoading = true
        val job = launch {
            viewModel.pictureGallery.collect {
                isLoading = it.isLoading
            }
        }

        viewModel.fetchPictureGalleryData()
        assertThat(isLoading).isFalse()
        job.cancel()
    }

    @Test
    fun `picture gallery - should get data when all is good`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = false, isErrorOn = false)

        var data: HashMap<String, MutableList<Picture>>? = null
        val job = launch {
            viewModel.pictureGallery.collect {
                data = it.gallery
            }
        }

        viewModel.fetchPictureGalleryData()
        assertThat(data).isNotNull()
        job.cancel()
    }

    @Test
    fun `picture gallery - if there is error while fetching profile loading should be stopped`() =
        runBlockingTest {
            repository.configureFakeRepository(isLoadingOn = false, isErrorOn = true)

            var isLoading = true
            val job = launch {
                viewModel.pictureGallery.collect {
                    isLoading = it.isLoading
                }
            }

            viewModel.fetchPictureGalleryData()
            assertThat(isLoading).isFalse()
            job.cancel()
        }

    @Test
    fun `picture gallery - if there is error while fetching profile snackBar event should be emitted`() =
        runBlockingTest {
            repository.configureFakeRepository(isLoadingOn = false, isErrorOn = true)

            var event: UiEvents? = null
            val job = launch {
                viewModel.uiEvents.collect {
                    event = it
                }
            }

            viewModel.fetchPictureGalleryData()
            assertThat(event).isNotNull()
            job.cancel()
        }

}