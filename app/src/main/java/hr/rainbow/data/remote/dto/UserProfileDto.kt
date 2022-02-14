package hr.rainbow.data.remote.dto

import hr.rainbow.domain.model.UserProfile

data class UserProfileDto(
    val BIO: String?,
    val EMAIL: String?,
    val FIRSTNAME: String?,
    val IDPROFILE: Int = -1,
    val LASTNAME: String?,
    val NICKNAME: String?
) {
    fun toUserProfile(): UserProfile {
        return UserProfile(
            id = IDPROFILE,
            firstName = FIRSTNAME ?: "",
            lastName = LASTNAME ?: "",
            nickName = NICKNAME ?: "",
            bio = BIO ?: "",
            email = EMAIL ?: ""
        )
    }
}