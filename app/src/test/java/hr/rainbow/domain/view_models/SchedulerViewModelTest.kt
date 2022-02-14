package hr.rainbow.domain.view_models

import com.google.common.truth.Truth.assertThat
import hr.rainbow.data.FakeDataRepository
import hr.rainbow.domain.model.DaySchedule
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
class SchedulerViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: SchedulerViewModel
    private lateinit var repository: FakeDataRepository

    @Before
    fun setup() {
        repository = FakeDataRepository()
        viewModel = SchedulerViewModel(repository)
    }

    @After
    fun teardown() {
    }


//SCHEDULE TEST - MONDAY

    @Test
    fun `schedule monday - isLoading should be true when fetching is started`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = true, isErrorOn = false)

        var isLoading = false
        val job = launch {
            viewModel.monday.collect {
                isLoading = it.isLoading
            }
        }

        viewModel.fetchDaySchedule()
        assertThat(isLoading).isTrue()
        job.cancel()
    }

    @Test
    fun `schedule monday - isLoading should be false when fetching is done`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = false, isErrorOn = false)

        var isLoading = true
        val job = launch {
            viewModel.monday.collect {
                isLoading = it.isLoading
            }
        }

        viewModel.fetchDaySchedule()
        assertThat(isLoading).isFalse()
        job.cancel()
    }

    @Test
    fun `schedule monday - should get data when all is good`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = false, isErrorOn = false)

        var data: DaySchedule? = null
        val job = launch {
            viewModel.monday.collect {
                data = it.daySchedule
            }
        }

        viewModel.fetchDaySchedule()
        assertThat(data).isNotNull()
        job.cancel()
    }

    @Test
    fun `schedule monday - if there is error while fetching profile loading should be stopped`() =
        runBlockingTest {
            repository.configureFakeRepository(isLoadingOn = false, isErrorOn = true)

            var isLoading = true
            val job = launch {
                viewModel.monday.collect {
                    isLoading = it.isLoading
                }
            }

            viewModel.fetchDaySchedule()
            assertThat(isLoading).isFalse()
            job.cancel()
        }

    @Test
    fun `schedule monday - if there is error while fetching profile snackBar event should be emitted`() =
        runBlockingTest {
            repository.configureFakeRepository(isLoadingOn = false, isErrorOn = true)

            var event: UiEvents? = null
            val job = launch {
                viewModel.uiEvents.collect {
                    event = it
                }
            }

            viewModel.fetchDaySchedule()
            assertThat(event).isNotNull()
            job.cancel()
        }


//SCHEDULE TEST - TUESDAY

    @Test
    fun `schedule tuesday - isLoading should be true when fetching is started`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = true, isErrorOn = false)

        var isLoading = false
        val job = launch {
            viewModel.tuesday.collect {
                isLoading = it.isLoading
            }
        }

        viewModel.fetchDaySchedule()
        assertThat(isLoading).isTrue()
        job.cancel()
    }

    @Test
    fun `schedule tuesday - isLoading should be false when fetching is done`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = false, isErrorOn = false)

        var isLoading = true
        val job = launch {
            viewModel.tuesday.collect {
                isLoading = it.isLoading
            }
        }

        viewModel.fetchDaySchedule()
        assertThat(isLoading).isFalse()
        job.cancel()
    }

    @Test
    fun `schedule tuesday - should get data when all is good`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = false, isErrorOn = false)

        var data: DaySchedule? = null
        val job = launch {
            viewModel.tuesday.collect {
                data = it.daySchedule
            }
        }

        viewModel.fetchDaySchedule()
        assertThat(data).isNotNull()
        job.cancel()
    }

    @Test
    fun `schedule tuesday - if there is error while fetching profile loading should be stopped`() =
        runBlockingTest {
            repository.configureFakeRepository(isLoadingOn = false, isErrorOn = true)

            var isLoading = true
            val job = launch {
                viewModel.tuesday.collect {
                    isLoading = it.isLoading
                }
            }

            viewModel.fetchDaySchedule()
            assertThat(isLoading).isFalse()
            job.cancel()
        }

    @Test
    fun `schedule tuesday - if there is error while fetching profile snackBar event should be emitted`() =
        runBlockingTest {
            repository.configureFakeRepository(isLoadingOn = false, isErrorOn = true)

            var event: UiEvents? = null
            val job = launch {
                viewModel.uiEvents.collect {
                    event = it
                }
            }

            viewModel.fetchDaySchedule()
            assertThat(event).isNotNull()
            job.cancel()
        }



//SCHEDULE TEST - WEDNESDAY

    @Test
    fun `schedule wednesday - isLoading should be true when fetching is started`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = true, isErrorOn = false)

        var isLoading = false
        val job = launch {
            viewModel.wednesday.collect {
                isLoading = it.isLoading
            }
        }

        viewModel.fetchDaySchedule()
        assertThat(isLoading).isTrue()
        job.cancel()
    }

    @Test
    fun `schedule wednesday - isLoading should be false when fetching is done`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = false, isErrorOn = false)

        var isLoading = true
        val job = launch {
            viewModel.wednesday.collect {
                isLoading = it.isLoading
            }
        }

        viewModel.fetchDaySchedule()
        assertThat(isLoading).isFalse()
        job.cancel()
    }

    @Test
    fun `schedule wednesday - should get data when all is good`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = false, isErrorOn = false)

        var data: DaySchedule? = null
        val job = launch {
            viewModel.wednesday.collect {
                data = it.daySchedule
            }
        }

        viewModel.fetchDaySchedule()
        assertThat(data).isNotNull()
        job.cancel()
    }

    @Test
    fun `schedule wednesday - if there is error while fetching profile loading should be stopped`() =
        runBlockingTest {
            repository.configureFakeRepository(isLoadingOn = false, isErrorOn = true)

            var isLoading = true
            val job = launch {
                viewModel.wednesday.collect {
                    isLoading = it.isLoading
                }
            }

            viewModel.fetchDaySchedule()
            assertThat(isLoading).isFalse()
            job.cancel()
        }

    @Test
    fun `schedule wednesday - if there is error while fetching profile snackBar event should be emitted`() =
        runBlockingTest {
            repository.configureFakeRepository(isLoadingOn = false, isErrorOn = true)

            var event: UiEvents? = null
            val job = launch {
                viewModel.uiEvents.collect {
                    event = it
                }
            }

            viewModel.fetchDaySchedule()
            assertThat(event).isNotNull()
            job.cancel()
        }



//SCHEDULE TEST - THURSDAY

    @Test
    fun `schedule thursday - isLoading should be true when fetching is started`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = true, isErrorOn = false)

        var isLoading = false
        val job = launch {
            viewModel.thursday.collect {
                isLoading = it.isLoading
            }
        }

        viewModel.fetchDaySchedule()
        assertThat(isLoading).isTrue()
        job.cancel()
    }

    @Test
    fun `schedule thursday - isLoading should be false when fetching is done`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = false, isErrorOn = false)

        var isLoading = true
        val job = launch {
            viewModel.thursday.collect {
                isLoading = it.isLoading
            }
        }

        viewModel.fetchDaySchedule()
        assertThat(isLoading).isFalse()
        job.cancel()
    }

    @Test
    fun `schedule thursday - should get data when all is good`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = false, isErrorOn = false)

        var data: DaySchedule? = null
        val job = launch {
            viewModel.thursday.collect {
                data = it.daySchedule
            }
        }

        viewModel.fetchDaySchedule()
        assertThat(data).isNotNull()
        job.cancel()
    }

    @Test
    fun `schedule thursday - if there is error while fetching profile loading should be stopped`() =
        runBlockingTest {
            repository.configureFakeRepository(isLoadingOn = false, isErrorOn = true)

            var isLoading = true
            val job = launch {
                viewModel.thursday.collect {
                    isLoading = it.isLoading
                }
            }

            viewModel.fetchDaySchedule()
            assertThat(isLoading).isFalse()
            job.cancel()
        }

    @Test
    fun `schedule thursday - if there is error while fetching profile snackBar event should be emitted`() =
        runBlockingTest {
            repository.configureFakeRepository(isLoadingOn = false, isErrorOn = true)

            var event: UiEvents? = null
            val job = launch {
                viewModel.uiEvents.collect {
                    event = it
                }
            }

            viewModel.fetchDaySchedule()
            assertThat(event).isNotNull()
            job.cancel()
        }


//SCHEDULE TEST - FRIDAY

    @Test
    fun `schedule friday - isLoading should be true when fetching is started`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = true, isErrorOn = false)

        var isLoading = false
        val job = launch {
            viewModel.friday.collect {
                isLoading = it.isLoading
            }
        }

        viewModel.fetchDaySchedule()
        assertThat(isLoading).isTrue()
        job.cancel()
    }

    @Test
    fun `schedule friday - isLoading should be false when fetching is done`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = false, isErrorOn = false)

        var isLoading = true
        val job = launch {
            viewModel.friday.collect {
                isLoading = it.isLoading
            }
        }

        viewModel.fetchDaySchedule()
        assertThat(isLoading).isFalse()
        job.cancel()
    }

    @Test
    fun `schedule friday - should get data when all is good`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = false, isErrorOn = false)

        var data: DaySchedule? = null
        val job = launch {
            viewModel.friday.collect {
                data = it.daySchedule
            }
        }

        viewModel.fetchDaySchedule()
        assertThat(data).isNotNull()
        job.cancel()
    }

    @Test
    fun `schedule friday - if there is error while fetching profile loading should be stopped`() =
        runBlockingTest {
            repository.configureFakeRepository(isLoadingOn = false, isErrorOn = true)

            var isLoading = true
            val job = launch {
                viewModel.friday.collect {
                    isLoading = it.isLoading
                }
            }

            viewModel.fetchDaySchedule()
            assertThat(isLoading).isFalse()
            job.cancel()
        }

    @Test
    fun `schedule friday - if there is error while fetching profile snackBar event should be emitted`() =
        runBlockingTest {
            repository.configureFakeRepository(isLoadingOn = false, isErrorOn = true)

            var event: UiEvents? = null
            val job = launch {
                viewModel.uiEvents.collect {
                    event = it
                }
            }

            viewModel.fetchDaySchedule()
            assertThat(event).isNotNull()
            job.cancel()
        }



//SCHEDULE TEST - SATURDAY

    @Test
    fun `schedule saturday - isLoading should be true when fetching is started`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = true, isErrorOn = false)

        var isLoading = false
        val job = launch {
            viewModel.saturday.collect {
                isLoading = it.isLoading
            }
        }

        viewModel.fetchDaySchedule()
        assertThat(isLoading).isTrue()
        job.cancel()
    }

    @Test
    fun `schedule saturday - isLoading should be false when fetching is done`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = false, isErrorOn = false)

        var isLoading = true
        val job = launch {
            viewModel.saturday.collect {
                isLoading = it.isLoading
            }
        }

        viewModel.fetchDaySchedule()
        assertThat(isLoading).isFalse()
        job.cancel()
    }

    @Test
    fun `schedule saturday - should get data when all is good`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = false, isErrorOn = false)

        var data: DaySchedule? = null
        val job = launch {
            viewModel.saturday.collect {
                data = it.daySchedule
            }
        }

        viewModel.fetchDaySchedule()
        assertThat(data).isNotNull()
        job.cancel()
    }

    @Test
    fun `schedule saturday - if there is error while fetching profile loading should be stopped`() =
        runBlockingTest {
            repository.configureFakeRepository(isLoadingOn = false, isErrorOn = true)

            var isLoading = true
            val job = launch {
                viewModel.saturday.collect {
                    isLoading = it.isLoading
                }
            }

            viewModel.fetchDaySchedule()
            assertThat(isLoading).isFalse()
            job.cancel()
        }

    @Test
    fun `schedule saturday - if there is error while fetching profile snackBar event should be emitted`() =
        runBlockingTest {
            repository.configureFakeRepository(isLoadingOn = false, isErrorOn = true)

            var event: UiEvents? = null
            val job = launch {
                viewModel.uiEvents.collect {
                    event = it
                }
            }

            viewModel.fetchDaySchedule()
            assertThat(event).isNotNull()
            job.cancel()
        }



//SCHEDULE TEST - SUNDAY

    @Test
    fun `schedule sunday - isLoading should be true when fetching is started`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = true, isErrorOn = false)

        var isLoading = false
        val job = launch {
            viewModel.sunday.collect {
                isLoading = it.isLoading
            }
        }

        viewModel.fetchDaySchedule()
        assertThat(isLoading).isTrue()
        job.cancel()
    }

    @Test
    fun `schedule sunday - isLoading should be false when fetching is done`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = false, isErrorOn = false)

        var isLoading = true
        val job = launch {
            viewModel.sunday.collect {
                isLoading = it.isLoading
            }
        }

        viewModel.fetchDaySchedule()
        assertThat(isLoading).isFalse()
        job.cancel()
    }

    @Test
    fun `schedule sunday - should get data when all is good`() = runBlockingTest {
        repository.configureFakeRepository(isLoadingOn = false, isErrorOn = false)

        var data: DaySchedule? = null
        val job = launch {
            viewModel.sunday.collect {
                data = it.daySchedule
            }
        }

        viewModel.fetchDaySchedule()
        assertThat(data).isNotNull()
        job.cancel()
    }

    @Test
    fun `schedule sunday - if there is error while fetching profile loading should be stopped`() =
        runBlockingTest {
            repository.configureFakeRepository(isLoadingOn = false, isErrorOn = true)

            var isLoading = true
            val job = launch {
                viewModel.sunday.collect {
                    isLoading = it.isLoading
                }
            }

            viewModel.fetchDaySchedule()
            assertThat(isLoading).isFalse()
            job.cancel()
        }

    @Test
    fun `schedule sunday - if there is error while fetching profile snackBar event should be emitted`() =
        runBlockingTest {
            repository.configureFakeRepository(isLoadingOn = false, isErrorOn = true)

            var event: UiEvents? = null
            val job = launch {
                viewModel.uiEvents.collect {
                    event = it
                }
            }

            viewModel.fetchDaySchedule()
            assertThat(event).isNotNull()
            job.cancel()
        }

}