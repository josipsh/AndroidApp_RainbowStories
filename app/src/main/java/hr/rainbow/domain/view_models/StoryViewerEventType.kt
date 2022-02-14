package hr.rainbow.domain.view_models

sealed class StoryViewerEventType{
    object SwipeForward: StoryViewerEventType()
    object SwipeBackward: StoryViewerEventType()
    object ToggleOverlayControls: StoryViewerEventType()
}
