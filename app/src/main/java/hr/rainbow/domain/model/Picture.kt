package hr.rainbow.domain.model

import java.time.LocalDateTime

data class Picture(
    val id: Int,
    val url: String,
    val date: LocalDateTime
)