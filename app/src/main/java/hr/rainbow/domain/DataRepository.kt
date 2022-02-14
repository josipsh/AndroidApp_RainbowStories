package hr.rainbow.domain

import hr.rainbow.domain.model.*
import hr.rainbow.util.Resource
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface DataRepository {
    fun getDiscoverStories(): Flow<Resource<List<Story>>>
    fun getMyStories(): Flow<Resource<List<Story>>>
    fun getFavoritesStories(): Flow<Resource<List<Story>>>

    fun getStoryCards(storyId: Int): Flow<Resource<List<Card>>>

    fun getDaySchedule(date: LocalDate): Flow<Resource<DaySchedule>>

    fun getProfile(): Flow<Resource<UserProfile>>
    fun updateProfile(profile: UserProfile): Flow<Resource<Boolean>>
    fun deleteAccount(userProfile: UserProfile): Flow<Resource<Boolean>>

    fun getPictureGallery(): Flow<Resource<HashMap<String, MutableList<Picture>>>>
    fun uploadImageUrlForUser(imageUrl: String): Flow<Resource<Boolean>>
    fun deleteImageUrlForUser(picture: Picture): Flow<Resource<Boolean>>

    fun like(storyId: Int, isLiked: Boolean): Flow<Resource<Boolean>>

    fun changePassword(
        oldPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ): Flow<Resource<Boolean>>

    fun logIn(email: String, password: String): Flow<Resource<Boolean>>
    fun register(
        userProfile: UserProfile,
        password: String,
        confirmPassword: String
    ): Flow<Resource<Boolean>>

    fun searchByTag(query: String): Flow<Resource<List<Story>>>
    fun getTagSuggestions(query: String): Flow<Resource<List<String>>>
    fun getAllTags(): Flow<Resource<List<String>>>

}