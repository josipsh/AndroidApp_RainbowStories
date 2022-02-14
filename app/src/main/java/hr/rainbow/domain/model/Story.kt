package hr.rainbow.domain.model

data class Story(
    val id: Int,
    val title: String,
    val description: String,
    var likesCount: Int,
    val thumbnailUrl: String,
    var isLiked: Boolean)