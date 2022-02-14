package hr.rainbow.data.remote.dto

import hr.rainbow.domain.model.Picture
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class ImageDto(
    val IDIMAGE: Int = -1,
    val IMGTIMESTAMP: String,
    val PROFILEID: Int,
    val SHARED: Int = 0,
    val URL: String
) {
    fun toPicture(): Picture {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
        val date = LocalDateTime.of(LocalDate.parse(IMGTIMESTAMP, formatter), LocalTime.of(0, 0))
        return Picture(
            id = IDIMAGE,
            url = URL,
            date = date
        )
    }
}