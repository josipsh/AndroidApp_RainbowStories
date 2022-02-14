package hr.rainbow.domain.view_models

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth
import hr.rainbow.data.FakeDataRepositoryForAndroidTest
import hr.rainbow.domain.model.SearchSuggestionType
import hr.rainbow.domain.model.Story
import hr.rainbow.domain.model.SuggestionItem
import hr.rainbow.util.MainCoroutineRuleForAndroidTest
import hr.rainbow.util.UiEvents
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class SearchViewModelTest {
    @get:Rule
    var mainCoroutineRule = MainCoroutineRuleForAndroidTest()

    private lateinit var viewModel: SearchViewModel
    private lateinit var repository: FakeDataRepositoryForAndroidTest

    @Before
    fun setup() {
        repository = FakeDataRepositoryForAndroidTest()
        viewModel = SearchViewModel(repository)
    }

    @After
    fun teardown() {
    }


//SEARCH TAG SUGGESTIONS

//    @Test
//    fun searchTagSuggestion_isLoadingShouldBeTrueWhenFetchingIsStarted() = runBlockingTest {
//        repository.configureFakeRepository(isLoadingOn = true, isErrorOn = false)
//
//        var isLoading = false
//        val job = launch {
//            viewModel.tagSuggestion.collect {
//                isLoading = it.isLoading
//            }
//        }
//
//        viewModel.getSuggestions(
//            query = "",
//            context = ApplicationProvider.getApplicationContext()
//        )
//        Truth.assertThat(isLoading).isTrue()
//        job.cancel()
//    }

    @Test
    fun searchTagSuggestion_isLoadingShouldBeFalseWhenFetchingIsDone() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = false, isErrorOn = false)

        var isLoading = true
        val job = launch {
            viewModel.tagSuggestion.collect {
                isLoading = it.isLoading
            }
        }

        viewModel.getSuggestions(
            query = "",
            context = ApplicationProvider.getApplicationContext()
        )
        Truth.assertThat(isLoading).isFalse()
        job.cancel()
    }

    @Test
    fun searchTagSuggestion_shouldGetDataWhenAllIsGood() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = false, isErrorOn = false)

        var data: List<SuggestionItem>? = null
        val job = launch {
            viewModel.tagSuggestion.collect {
                data = it.data
            }
        }

        viewModel.getSuggestions(
            query = "",
            context = ApplicationProvider.getApplicationContext()
        )
        Truth.assertThat(data).isNotNull()
        job.cancel()
    }

    @Test
    fun searchTagSuggestion_ifThereIsErrorWhileFetchingProfileLoadingShouldBeStopped() =
        runBlockingTest {
            repository.configureFakeRepository(isLoadingOn = false, isErrorOn = true)

            var isLoading = true
            val job = launch {
                viewModel.tagSuggestion.collect {
                    isLoading = it.isLoading
                }
            }

            viewModel.getSuggestions(
                query = "",
                context = ApplicationProvider.getApplicationContext()
            )
            Truth.assertThat(isLoading).isFalse()
            job.cancel()
        }

//    @Test
//    fun searchTagSuggestion_ifThereIsErrorWhileFetchingProfileSnackBarEventShouldBeEmitted() =
//        runBlockingTest {
//            repository.configureFakeRepository(isLoadingOn = false, isErrorOn = true)
//
//            var event: UiEvents? = null
//            val job = launch {
//                viewModel.uiEvents.collect {
//                    event = it
//                }
//            }
//
//            viewModel.getSuggestions(
//                query = "",
//                context = ApplicationProvider.getApplicationContext()
//                )
//            Truth.assertThat(event).isNotNull()
//            job.cancel()
//        }


//SEARCH RECENT SUGGESTIONS

//    @Test
//    fun searchRecentSuggestion_isLoadingShouldBeTrueWhenFetchingIsStarted() = runBlockingTest {
//        repository.configureFakeRepository(isLoadingOn = true, isErrorOn = false)
//
//        var isLoading = false
//        val job = launch {
//            viewModel.recentSuggestion.collect {
//                isLoading = it.isLoading
//            }
//        }
//
//        viewModel.getSuggestions(
//            query = "",
//            context = ApplicationProvider.getApplicationContext()
//        )
//        Truth.assertThat(isLoading).isTrue()
//        job.cancel()
//    }

    @Test
    fun searchRecentSuggestion_isLoadingShouldBeFalseWhenFetchingIsDone() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = false, isErrorOn = false)

        var isLoading = true
        val job = launch {
            viewModel.recentSuggestion.collect {
                isLoading = it.isLoading
            }
        }

        viewModel.getSuggestions(
            query = "",
            context = ApplicationProvider.getApplicationContext()
        )
        Truth.assertThat(isLoading).isFalse()
        job.cancel()
    }

    @Test
    fun searchRecentSuggestion_shouldGetDataWhenAllIsGood() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = false, isErrorOn = false)

        var data: List<SuggestionItem>? = null
        val job = launch {
            viewModel.recentSuggestion.collect {
                data = it.data
            }
        }

        viewModel.getSuggestions(
            query = "",
            context = ApplicationProvider.getApplicationContext()
        )
        Truth.assertThat(data).isNotNull()
        job.cancel()
    }

    @Test
    fun searchRecentSuggestion_ifThereIsErrorWhileFetchingProfileLoadingShouldBeStopped() =
        runBlockingTest {
            repository.configureFakeRepository(isLoadingOn = false, isErrorOn = true)

            var isLoading = true
            val job = launch {
                viewModel.recentSuggestion.collect {
                    isLoading = it.isLoading
                }
            }

            viewModel.getSuggestions(
                query = "",
                context = ApplicationProvider.getApplicationContext()
            )
            Truth.assertThat(isLoading).isFalse()
            job.cancel()
        }

//    @Test
//    fun searchRecentSuggestion_ifThereIsErrorWhileFetchingProfileSnackBarEventShouldBeEmitted() =
//        runBlockingTest {
//            repository.configureFakeRepository(isLoadingOn = false, isErrorOn = true)
//
//            var event: UiEvents? = null
//            val job = launch {
//                viewModel.uiEvents.collect {
//                    event = it
//                }
//            }
//
//            viewModel.getSuggestions(
//                query = "",
//                context = ApplicationProvider.getApplicationContext()
//                )
//            Truth.assertThat(event).isNotNull()
//            job.cancel()
//        }


//SEARCH QUERY RESULT - TRIGGERED WITH TAG SEARCH

    @Test
    fun searchResultTriggeredWithTagSearch_isLoadingShouldBeTrueWhenFetchingIsStarted() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = true, isErrorOn = false)

        var isLoading = false
        val job = launch {
            viewModel.queryResult.collect {
                isLoading = it.isLoading
            }
        }

        viewModel.tagSearch(
            query = SuggestionItem(
                type = SearchSuggestionType.TAG,
                ""
            ),
            context = ApplicationProvider.getApplicationContext()
        )
        Truth.assertThat(isLoading).isTrue()
        job.cancel()
    }

    @Test
    fun searchResultTriggeredWithTagSearch_isLoadingShouldBeFalseWhenFetchingIsDone() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = false, isErrorOn = false)

        var isLoading = true
        val job = launch {
            viewModel.queryResult.collect {
                isLoading = it.isLoading
            }
        }

        viewModel.tagSearch(
            query = SuggestionItem(
                type = SearchSuggestionType.TAG,
                ""
            ),
            context = ApplicationProvider.getApplicationContext()
        )
        Truth.assertThat(isLoading).isFalse()
        job.cancel()
    }

    @Test
    fun searchResultTriggeredWithTagSearch_shouldGetDataWhenAllIsGood() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = false, isErrorOn = false)

        var data: List<Story>? = null
        val job = launch {
            viewModel.queryResult.collect {
                data = it.storyItems
            }
        }

        viewModel.tagSearch(
            query = SuggestionItem(
                type = SearchSuggestionType.TAG,
                ""
            ),
            context = ApplicationProvider.getApplicationContext()
        )
        Truth.assertThat(data).isNotNull()
        job.cancel()
    }

    @Test
    fun searchResultTriggeredWithTagSearch_ifThereIsErrorWhileFetchingProfileLoadingShouldBeStopped() =
        runBlockingTest {
            repository.configureFakeRepository(isLoadingOn = false, isErrorOn = true)

            var isLoading = true
            val job = launch {
                viewModel.queryResult.collect {
                    isLoading = it.isLoading
                }
            }

            viewModel.tagSearch(
                query = SuggestionItem(
                    type = SearchSuggestionType.TAG,
                    ""
                ),
                context = ApplicationProvider.getApplicationContext()
            )
            Truth.assertThat(isLoading).isFalse()
            job.cancel()
        }

    @Test
    fun searchResultTriggeredWithTagSearch_ifThereIsErrorWhileFetchingProfileSnackBarEventShouldBeEmitted() =
        runBlockingTest {
            repository.configureFakeRepository(isLoadingOn = false, isErrorOn = true)

            var event: UiEvents? = null
            val job = launch {
                viewModel.uiEvents.collect {
                    event = it
                }
            }

            viewModel.tagSearch(
                query = SuggestionItem(
                    type = SearchSuggestionType.TAG,
                    ""
                ),
                context = ApplicationProvider.getApplicationContext()
                )
            Truth.assertThat(event).isNotNull()
            job.cancel()
        }

}