package hr.rainbow.domain.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.rainbow.domain.DataRepository
import hr.rainbow.domain.model.DaySchedule
import hr.rainbow.domain.model.DayScheduleState
import hr.rainbow.util.Resource
import hr.rainbow.util.UiEvents
import hr.rainbow.util.getMondayDateOfWeek
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class SchedulerViewModel @Inject constructor(
    private val repository: DataRepository
) : ViewModel() {

    init {
        fetchDaySchedule()
    }

    private val _monday = MutableStateFlow(DayScheduleState())
    val monday = _monday.asStateFlow()

    private val _tuesday = MutableStateFlow(DayScheduleState())
    val tuesday = _tuesday.asStateFlow()

    private val _wednesday = MutableStateFlow(DayScheduleState())
    val wednesday = _wednesday.asStateFlow()

    private val _thursday = MutableStateFlow(DayScheduleState())
    val thursday = _thursday.asStateFlow()

    private val _friday = MutableStateFlow(DayScheduleState())
    val friday = _friday.asStateFlow()

    private val _saturday = MutableStateFlow(DayScheduleState())
    val saturday = _saturday.asStateFlow()

    private val _sunday = MutableStateFlow(DayScheduleState())
    val sunday = _sunday.asStateFlow()

    private val _uiEvents = MutableSharedFlow<UiEvents>()
    val uiEvents = _uiEvents.asSharedFlow()

    fun fetchDaySchedule() {
        viewModelScope.launch {
            val dateOfMonday = getMondayDateOfWeek(LocalDate.now())

            getDaySchedule(dateOfMonday)
            getDaySchedule(dateOfMonday.plusDays(1))
            getDaySchedule(dateOfMonday.plusDays(2))
            getDaySchedule(dateOfMonday.plusDays(3))
            getDaySchedule(dateOfMonday.plusDays(4))
            getDaySchedule(dateOfMonday.plusDays(5))
            getDaySchedule(dateOfMonday.plusDays(6))

        }
    }

    private suspend fun getDaySchedule(date: LocalDate) {
        repository.getDaySchedule(date).collect {
            when (it) {
                is Resource.Success -> {
                    setIsLoading(false, date.dayOfWeek)
                    it.data?.let { daySchedule ->
                        handleData(daySchedule, date.dayOfWeek)
                    } ?: run {
                        _uiEvents.emit(
                            UiEvents.ShowSnackBar(
                                message = "Unexpected error occurred!",
                                action = "Ok"
                            )
                        )
                    }
                }
                is Resource.Error -> {
                    setIsLoading(false, date.dayOfWeek)
                    _uiEvents.emit(
                        UiEvents.ShowSnackBar(
                            message = it.message ?: "Unexpected error occurred!",
                            action = "Ok"
                        )
                    )
                }
                is Resource.Loading -> {
                    setIsLoading(true, date.dayOfWeek)
                }
            }
        }
    }

    private suspend fun setIsLoading(data: Boolean, dayOfWeek: DayOfWeek?) {
        when (dayOfWeek) {
            DayOfWeek.MONDAY -> {
                _monday.value = _monday.value.copy(
                    isLoading = data
                )
            }
            DayOfWeek.TUESDAY -> {
                _tuesday.value = _tuesday.value.copy(
                    isLoading = data
                )
            }
            DayOfWeek.WEDNESDAY -> {
                _wednesday.value = _wednesday.value.copy(
                    isLoading = data
                )
            }
            DayOfWeek.THURSDAY -> {
                _thursday.value = _thursday.value.copy(
                    isLoading = data
                )
            }
            DayOfWeek.FRIDAY -> {
                _friday.value = _friday.value.copy(
                    isLoading = data
                )
            }
            DayOfWeek.SATURDAY -> {
                _saturday.value = _saturday.value.copy(
                    isLoading = data
                )
            }
            DayOfWeek.SUNDAY -> {
                _sunday.value = _sunday.value.copy(
                    isLoading = data
                )
            }
            else -> {
                _uiEvents.emit(
                    UiEvents.ShowSnackBar(
                        message = "Unexpected error occurred!",
                        action = "Ok"
                    )
                )
            }
        }
    }

    private suspend fun handleData(data: DaySchedule, day: DayOfWeek) {
        when (day) {
            DayOfWeek.MONDAY -> {
                _monday.value = _monday.value.copy(
                    daySchedule = data
                )
            }
            DayOfWeek.TUESDAY -> {
                _tuesday.value = _tuesday.value.copy(
                    daySchedule = data
                )
            }
            DayOfWeek.WEDNESDAY -> {
                _wednesday.value = _wednesday.value.copy(
                    daySchedule = data
                )
            }
            DayOfWeek.THURSDAY -> {
                _thursday.value = _thursday.value.copy(
                    daySchedule = data
                )
            }
            DayOfWeek.FRIDAY -> {
                _friday.value = _friday.value.copy(
                    daySchedule = data
                )
            }
            DayOfWeek.SATURDAY -> {
                _saturday.value = _saturday.value.copy(
                    daySchedule = data
                )
            }
            DayOfWeek.SUNDAY -> {
                _sunday.value = _sunday.value.copy(
                    daySchedule = data
                )
            }
            else -> {
                _uiEvents.emit(
                    UiEvents.ShowSnackBar(
                        message = "Unexpected error occurred!",
                        action = "Ok"
                    )
                )
            }
        }
    }
}