package hr.rainbow.data.remote.dto

import hr.rainbow.domain.model.DaySchedule
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class ScheduleDto(
    val IDSCHEDULE: Int,
    val PROFILEID: Int,
    val SCHEDULE_DATE: String,
    val SCHEDULE_IMAGE: List<ScheduleTasksDto>
) {
    fun toDaySchedule(): DaySchedule {
        val date = LocalDateTime.parse(SCHEDULE_DATE)
        val tasks = mutableListOf<String>()
        SCHEDULE_IMAGE.forEach {
            tasks.add(it.IMAGE.URL)
        }

        return DaySchedule(
            dayName = date.dayOfWeek.toString(),
            tasks = tasks
        )
    }
}