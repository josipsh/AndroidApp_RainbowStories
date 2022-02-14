package hr.rainbow.data.remote.dto

import hr.rainbow.domain.model.Story

data class StoryDto(
    val DESCRIPTION: String,
    val FAVOURITE: Int,
    val IDSTORY: Int,
    val IMAGE: ImageDto,
    val NAME: String,
    val NumberOfLikes: Int,
    val PROFILEID: Int,
    val SHARED: Int,
    val TAGS: List<TagDto>,
    val THUMBNAIL: Int
) {
    fun toStory(): Story {
        return Story(
            id = IDSTORY,
            title = NAME,
            description = DESCRIPTION,
            likesCount = NumberOfLikes,
            thumbnailUrl = IMAGE.URL,
            isLiked = FAVOURITE != 0
        )
    }
}