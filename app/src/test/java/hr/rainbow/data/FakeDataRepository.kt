package hr.rainbow.data

import hr.rainbow.domain.DataRepository
import hr.rainbow.domain.model.*
import hr.rainbow.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import java.time.LocalDateTime

class FakeDataRepository : DataRepository {

    private val stories: MutableList<Story> = mutableListOf(
        Story(
            title = "Fake story 1",
            description = "Fake story description 1",
            isLiked = true,
            id = 0,
            likesCount = 100,
            thumbnailUrl = "url"
        ),
        Story(
            title = "Fake story 2",
            description = "Fake story description 3",
            isLiked = true,
            id = 1,
            likesCount = 10,
            thumbnailUrl = "url"
        ),
        Story(
            title = "Fake story 3",
            description = "Fake story description 3",
            isLiked = true,
            id = 2,
            likesCount = 5,
            thumbnailUrl = "url"
        )
    )

    private var userProfile: UserProfile = UserProfile(
        id = 1,
        firstName = "Pero",
        lastName = "Peric",
        nickName = "Perica",
        email = "email@gmail.com",
        bio = "This is bio"
    )

    private var isLoadingOn = false
    private var isErrorOn = false

    fun configureFakeRepository(isLoadingOn: Boolean, isErrorOn: Boolean) {
        this.isLoadingOn = isLoadingOn
        this.isErrorOn = isErrorOn
    }

    private val errorMessage = "Error Message"

    override fun getDiscoverStories(): Flow<Resource<List<Story>>> = flow {
        if (isLoadingOn) {
            emit(Resource.Loading(null))
            return@flow
        }
        if (isErrorOn) {
            emit(Resource.Error(errorMessage))
            return@flow
        }
        emit(Resource.Success(stories))
    }

    override fun getMyStories(): Flow<Resource<List<Story>>> = flow {
        if (isLoadingOn) {
            emit(Resource.Loading(null))
            return@flow
        }
        if (isErrorOn) {
            emit(Resource.Error(errorMessage))
            return@flow
        }
        emit(Resource.Success(stories))
    }

    override fun getFavoritesStories(): Flow<Resource<List<Story>>> = flow {
        if (isLoadingOn) {
            emit(Resource.Loading(null))
            return@flow
        }
        if (isErrorOn) {
            emit(Resource.Error(errorMessage))
            return@flow
        }
        emit(Resource.Success(stories))
    }

    override fun getStoryCards(storyId: Int): Flow<Resource<List<Card>>> = flow {
        if (isLoadingOn) {
            emit(Resource.Loading(null))
            return@flow
        }
        if (isErrorOn) {
            emit(Resource.Error(errorMessage))
            return@flow
        }
        emit(
            Resource.Success(
                listOf(
                    Card(
                        id = 1,
                        topText = "",
                        bottomText = "",
                        startImageUrl = "",
                        centerImageUrl = "",
                        endImageUrl = "",
                        textFormat = TextFormat.REGULAR,
                        textSize = TextSize.Medium,
                        color = ""
                    ),
                    Card(
                        id = 2,
                        topText = "",
                        bottomText = "",
                        startImageUrl = "",
                        centerImageUrl = "",
                        endImageUrl = "",
                        textFormat = TextFormat.REGULAR,
                        textSize = TextSize.Medium,
                        color = ""
                    )
                )
            )
        )
    }

    override fun getDaySchedule(date: LocalDate): Flow<Resource<DaySchedule>> = flow {
        if (isLoadingOn) {
            emit(Resource.Loading(null))
            return@flow
        }
        if (isErrorOn) {
            emit(Resource.Error(errorMessage))
            return@flow
        }
        emit(
            Resource.Success(
                DaySchedule(
                    dayName = "Monday",
                    tasks = listOf(
                        "Task1",
                        "Task2",
                        "Task3",
                    )
                )
            )
        )
    }

    override fun getProfile(): Flow<Resource<UserProfile>> = flow {
        if (isLoadingOn) {
            emit(Resource.Loading(null))
            return@flow
        }
        if (isErrorOn) {
            emit(Resource.Error(errorMessage))
            return@flow
        }
        emit(Resource.Success(userProfile))
    }

    override fun updateProfile(profile: UserProfile): Flow<Resource<Boolean>> = flow {
        if (isLoadingOn) {
            emit(Resource.Loading(null))
            return@flow
        }
        if (isErrorOn) {
            emit(Resource.Error(errorMessage))
            return@flow
        }
        userProfile = profile
        emit(Resource.Success(true))
    }

    override fun deleteAccount(userProfile: UserProfile): Flow<Resource<Boolean>> = flow {
        if (isLoadingOn) {
            emit(Resource.Loading(null))
            return@flow
        }
        if (isErrorOn) {
            emit(Resource.Error(errorMessage))
            return@flow
        }
        emit(Resource.Success(true))
    }

    override fun getPictureGallery(): Flow<Resource<HashMap<String, MutableList<Picture>>>> = flow {
        if (isLoadingOn) {
            emit(Resource.Loading(null))
            return@flow
        }
        if (isErrorOn) {
            emit(Resource.Error(errorMessage))
            return@flow
        }
        val gallery: HashMap<String, MutableList<Picture>> = HashMap(1)
        gallery["Jan"] = mutableListOf(
            Picture(
                id = 1,
                url = "",
                date = LocalDateTime.now()
            )
        )
        gallery["Jan"]?.add(
            Picture(
                id = 1,
                url = "",
                date = LocalDateTime.now()
            )
        )

        emit(Resource.Success(gallery))
    }

    override fun uploadImageUrlForUser(imageUrl: String): Flow<Resource<Boolean>> = flow {
        if (isLoadingOn) {
            emit(Resource.Loading(null))
            return@flow
        }
        if (isErrorOn) {
            emit(Resource.Error(errorMessage))
            return@flow
        }
        emit(Resource.Success(true))
    }

    override fun deleteImageUrlForUser(picture: Picture): Flow<Resource<Boolean>> = flow {
        if (isLoadingOn) {
            emit(Resource.Loading(null))
            return@flow
        }
        if (isErrorOn) {
            emit(Resource.Error(errorMessage))
            return@flow
        }
        emit(Resource.Success(true))
    }

    override fun like(storyId: Int, isLiked: Boolean): Flow<Resource<Boolean>> = flow {
        if (isLoadingOn) {
            emit(Resource.Loading(null))
            return@flow
        }
        if (isErrorOn) {
            emit(Resource.Error(errorMessage))
            return@flow
        }
        emit(Resource.Success(true))
    }

    override fun changePassword(
        oldPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ): Flow<Resource<Boolean>> = flow {
        if (isLoadingOn) {
            emit(Resource.Loading(null))
            return@flow
        }
        if (isErrorOn) {
            emit(Resource.Error(errorMessage))
            return@flow
        }
        emit(Resource.Success(true))
    }

    override fun logIn(email: String, password: String): Flow<Resource<Boolean>> = flow {
        if (isLoadingOn) {
            emit(Resource.Loading(null))
            return@flow
        }
        if (isErrorOn) {
            emit(Resource.Error(errorMessage))
            return@flow
        }
        emit(Resource.Success(true))
    }

    override fun register(
        userProfile: UserProfile,
        password: String,
        confirmPassword: String
    ): Flow<Resource<Boolean>> = flow {
        if (isLoadingOn) {
            emit(Resource.Loading(null))
            return@flow
        }
        if (isErrorOn) {
            emit(Resource.Error(errorMessage))
            return@flow
        }
        emit(Resource.Success(true))
    }

    override fun searchByTag(query: String): Flow<Resource<List<Story>>> = flow {
        if (isLoadingOn) {
            emit(Resource.Loading(null))
            return@flow
        }
        if (isErrorOn) {
            emit(Resource.Error(errorMessage))
            return@flow
        }
        emit(Resource.Success(stories))
    }

    override fun getTagSuggestions(query: String): Flow<Resource<List<String>>> = flow {
        if (isLoadingOn) {
            emit(Resource.Loading(null))
            return@flow
        }
        if (isErrorOn) {
            emit(Resource.Error(errorMessage))
            return@flow
        }
        emit(Resource.Success(listOf(query)))
    }

    override fun getAllTags(): Flow<Resource<List<String>>> = flow {
        if (isLoadingOn) {
            emit(Resource.Loading(null))
            return@flow
        }
        if (isErrorOn) {
            emit(Resource.Error(errorMessage))
            return@flow
        }
        emit(Resource.Success(listOf()))
    }
}