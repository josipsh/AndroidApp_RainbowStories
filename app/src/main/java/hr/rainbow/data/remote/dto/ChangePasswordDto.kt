package hr.rainbow.data.remote.dto

data class ChangePasswordDto(
    val ConfirmPassword: String,
    val NewPassword: String,
    val OldPassword: String
)