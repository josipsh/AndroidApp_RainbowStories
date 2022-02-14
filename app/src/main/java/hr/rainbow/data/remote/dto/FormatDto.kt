package hr.rainbow.data.remote.dto

data class FormatDto(
    val COLOR: String,
    val FONT_FORMAT: Int,
    val FONT_SIZE: Int,
    val IMAGE1: ImageDto?,
    val IMAGE2: ImageDto?,
    val IMAGE3: ImageDto?,
    val LAYOUT: Int
)