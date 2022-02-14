package hr.rainbow.domain.view_models

sealed class SwipeEventType{
    object SwipeForward : SwipeEventType()
    object SwipeBackward : SwipeEventType()
}
