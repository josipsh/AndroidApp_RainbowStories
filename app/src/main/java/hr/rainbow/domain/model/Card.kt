package hr.rainbow.domain.model

data class Card(
    val id: Int,
    val topText: String,
    val bottomText: String,
    val startImageUrl: String?,
    val centerImageUrl: String?,
    val endImageUrl: String?,
    val color: String,
    val textSize: TextSize,
    val textFormat: TextFormat
)
