package hr.rainbow.domain.view_models

import hr.rainbow.domain.model.Story

data class StoryState(
    val storyItems: List<Story> = emptyList(),
    val isLoading: Boolean = true
)
