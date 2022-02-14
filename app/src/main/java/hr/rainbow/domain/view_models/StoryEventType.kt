package hr.rainbow.domain.view_models

sealed class StoryEventType {
    data class PlayStory(val storyId: Int, val title: String) : StoryEventType()
    data class LikeClick(val storyId: Int, val isLiked: Boolean) : StoryEventType()
}
