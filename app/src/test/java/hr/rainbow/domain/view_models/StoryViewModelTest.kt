package hr.rainbow.domain.view_models

import hr.rainbow.domain.model.Story
import com.google.common.truth.Truth.assertThat
import hr.rainbow.data.FakeDataRepository
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
class StoryViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: StoryViewModel
    private lateinit var repository: FakeDataRepository

    @Before
    fun setup() {
        repository = FakeDataRepository()
        viewModel = StoryViewModel(repository)
    }

    @After
    fun teardown() {
    }

    @Test
    fun `discover stories - isLoading should be true state when launched`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = true, isErrorOn = false)

        var isLoading = false
        val job = launch {
            viewModel.discoverStories.collect {
                isLoading = it.isLoading
            }
        }

        viewModel.fetchDiscoverStories()
        assertThat(isLoading).isTrue()
        job.cancel()
    }

    @Test
    fun `discover stories - isLoading state should be false if Error is emitted`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = false, isErrorOn = true)

        var isLoading = true
        val job = launch {
            viewModel.discoverStories.collect {
                isLoading = it.isLoading
            }
        }

        viewModel.fetchDiscoverStories()
        assertThat(isLoading).isFalse()
        job.cancel()
    }

    @Test
    fun `discover stories - if there is error snackBar event should be emitted`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = false, isErrorOn = true)

        var event: UiEvents? = null
        val job = launch {
            viewModel.uiEvents.collect {
                event = it
            }
        }

        viewModel.fetchDiscoverStories()
        assertThat(event).isNotNull()
        job.cancel()
    }


    @Test
    fun `discover stories - isLoading state should be false if everything is good`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = false, isErrorOn = false)

        var isLoading = true
        val job = launch {
            viewModel.discoverStories.collect {
                isLoading = it.isLoading
            }
        }

        viewModel.fetchDiscoverStories()
        assertThat(isLoading).isFalse()
        job.cancel()
    }


    @Test
    fun `discover stories - we should have list of stories if everything is good`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = false, isErrorOn = false)

        var stories = emptyList<Story>()
        val job = launch {
            viewModel.discoverStories.collect {
                stories = it.storyItems
            }
        }

        viewModel.fetchDiscoverStories()
        assertThat(stories).isNotEmpty()
        job.cancel()
    }


//MY STORIES TESTS


    @Test
    fun `my stories - isLoading should be true state when launched`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = true, isErrorOn = false)

        var isLoading = false
        val job = launch {
            viewModel.myStories.collect {
                isLoading = it.isLoading
            }
        }

        viewModel.fetchMyStories()
        assertThat(isLoading).isTrue()
        job.cancel()
    }

    @Test
    fun `my stories - isLoading state should be false if Error is emitted`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = false, isErrorOn = true)

        var isLoading = true
        val job = launch {
            viewModel.myStories.collect {
                isLoading = it.isLoading
            }
        }

        viewModel.fetchMyStories()
        assertThat(isLoading).isFalse()
        job.cancel()
    }

    @Test
    fun `my stories - if there is error snackBar event should be emitted`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = false, isErrorOn = true)

        var event: UiEvents? = null
        val job = launch {
            viewModel.uiEvents.collect {
                event = it
            }
        }

        viewModel.fetchMyStories()
        assertThat(event).isNotNull()
        job.cancel()
    }


    @Test
    fun `my stories - isLoading state should be false if everything is good`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = false, isErrorOn = false)

        var isLoading = true
        val job = launch {
            viewModel.myStories.collect {
                isLoading = it.isLoading
            }
        }

        viewModel.fetchMyStories()
        assertThat(isLoading).isFalse()
        job.cancel()
    }


    @Test
    fun `my stories - we should have list of stories if everything is good`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = false, isErrorOn = false)

        var stories = emptyList<Story>()
        val job = launch {
            viewModel.myStories.collect {
                stories = it.storyItems
            }
        }

        viewModel.fetchMyStories()
        assertThat(stories).isNotEmpty()
        job.cancel()
    }


//FAVORITE STORIES TESTS


    @Test
    fun `favorite stories - isLoading should be true state when launched`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = true, isErrorOn = false)

        var isLoading = false
        val job = launch {
            viewModel.favoritesStories.collect {
                isLoading = it.isLoading
            }
        }

        viewModel.fetchFavoritesStories()
        assertThat(isLoading).isTrue()
        job.cancel()
    }

    @Test
    fun `favorite stories - isLoading state should be false if Error is emitted`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = false, isErrorOn = true)

        var isLoading = true
        val job = launch {
            viewModel.favoritesStories.collect {
                isLoading = it.isLoading
            }
        }

        viewModel.fetchFavoritesStories()
        assertThat(isLoading).isFalse()
        job.cancel()
    }

    @Test
    fun `favorite stories - if there is error snackBar event should be emitted`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = false, isErrorOn = true)

        var event: UiEvents? = null
        val job = launch {
            viewModel.uiEvents.collect {
                event = it
            }
        }

        viewModel.fetchFavoritesStories()
        assertThat(event).isNotNull()
        job.cancel()
    }


    @Test
    fun `favorite stories - isLoading state should be false if everything is good`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = false, isErrorOn = false)

        var isLoading = true
        val job = launch {
            viewModel.favoritesStories.collect {
                isLoading = it.isLoading
            }
        }

        viewModel.fetchFavoritesStories()
        assertThat(isLoading).isFalse()
        job.cancel()
    }


    @Test
    fun `favorite stories - we should have list of stories if everything is good`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = false, isErrorOn = false)

        var stories = emptyList<Story>()
        val job = launch {
            viewModel.favoritesStories.collect {
                stories = it.storyItems
            }
        }

        viewModel.fetchFavoritesStories()
        assertThat(stories).isNotEmpty()
        job.cancel()
    }


//like btn event


    @Test
    fun `like  - we should get error if something is bad`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = false, isErrorOn = true)

        var events: UiEvents? = null
        val job = launch {
            viewModel.uiEvents.collect {
                events = it
            }
        }

        viewModel.onLikeClickEvent(1, true)
        assertThat(events).isNotNull()
        job.cancel()
    }

}