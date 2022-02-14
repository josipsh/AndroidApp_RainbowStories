package hr.rainbow.domain.view_models

import hr.rainbow.domain.model.Story
import hr.rainbow.domain.model.UserProfile

data class ProfileState(
    val profileData: UserProfile = UserProfile(
        id = -1,
        firstName = "",
        lastName = "",
        bio = "",
        nickName = "",
        email = ""
    ),
    val isLoading: Boolean = false
)