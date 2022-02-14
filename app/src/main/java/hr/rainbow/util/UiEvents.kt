package hr.rainbow.util

sealed class UiEvents {
    data class ShowSnackBar(val message: String, val action: String? = null) : UiEvents()
}
