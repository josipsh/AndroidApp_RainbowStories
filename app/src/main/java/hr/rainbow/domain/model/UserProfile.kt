package hr.rainbow.domain.model

import hr.rainbow.data.remote.dto.UserProfileDto

data class UserProfile(
    var id: Int = 0,
    val email: String,
    val firstName: String,
    val lastName: String,
    val nickName: String,
    val bio: String = ""
) {
    fun toDto(): UserProfileDto {
        return UserProfileDto(
            IDPROFILE = id,
            FIRSTNAME = firstName,
            LASTNAME = lastName,
            NICKNAME = nickName,
            BIO = bio,
            EMAIL = email
        )
    }
}