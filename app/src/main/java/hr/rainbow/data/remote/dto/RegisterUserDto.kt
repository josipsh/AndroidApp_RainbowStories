package hr.rainbow.data.remote.dto

data class RegisterUserDto(
    val Email: String,
    val Password: String,
    val ConfirmPassword: String
)