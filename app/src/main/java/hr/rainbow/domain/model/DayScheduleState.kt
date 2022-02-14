package hr.rainbow.domain.model

import java.time.DayOfWeek

data class DayScheduleState(
    var daySchedule: DaySchedule = DaySchedule(
        DayOfWeek.MONDAY.toString(),
        emptyList()
    ),
    var isLoading: Boolean = false
)