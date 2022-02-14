package hr.rainbow.domain.view_models

import com.google.common.truth.Truth
import hr.rainbow.data.FakeDataRepository
import hr.rainbow.domain.model.Card
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
class StoryViewerViewModelTest{
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: StoryViewerViewModel
    private lateinit var repository: FakeDataRepository

    @Before
    fun setup() {
        repository = FakeDataRepository()
        viewModel = StoryViewerViewModel(repository)
    }

    @After
    fun teardown() {
    }

//STORY CARDS

    @Test
    fun `story viewer - isLoading should be true when fetching is started`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = true, isErrorOn = false)

        var isLoading = false
        val job = launch {
            viewModel.cards.collect {
                isLoading = it.isLoading
            }
        }

        viewModel.fetchCards(1)
        Truth.assertThat(isLoading).isTrue()
        job.cancel()
    }

    @Test
    fun `story viewer - isLoading should be false when fetching is done`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = false, isErrorOn = false)

        var isLoading = true
        val job = launch {
            viewModel.cards.collect {
                isLoading = it.isLoading
            }
        }

        viewModel.fetchCards(1)
        Truth.assertThat(isLoading).isFalse()
        job.cancel()
    }

    @Test
    fun `story viewer - should get data when all is good`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = false, isErrorOn = false)

        var data: List<Card>? = null
        val job = launch {
            viewModel.cards.collect {
                data = it.cards
            }
        }

        viewModel.fetchCards(1)
        Truth.assertThat(data).isNotNull()
        job.cancel()
    }

    @Test
    fun `story viewer - if there is error while fetching profile loading should be stopped`() =
        runBlockingTest {
            repository.configureFakeRepository(isLoadingOn = false, isErrorOn = true)

            var isLoading = true
            val job = launch {
                viewModel.cards.collect {
                    isLoading = it.isLoading
                }
            }

            viewModel.fetchCards(1)
            Truth.assertThat(isLoading).isFalse()
            job.cancel()
        }

    @Test
    fun `story viewer - if there is error while fetching profile snackBar event should be emitted`() =
        runBlockingTest {
            repository.configureFakeRepository(isLoadingOn = false, isErrorOn = true)

            var event: UiEvents? = null
            val job = launch {
                viewModel.uiEvents.collect {
                    event = it
                }
            }

            viewModel.fetchCards(1)
            Truth.assertThat(event).isNotNull()
            job.cancel()
        }

}