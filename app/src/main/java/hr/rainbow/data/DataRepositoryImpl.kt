package hr.rainbow.data

import hr.rainbow.data.remote.DataRestApiInterface
import hr.rainbow.data.remote.dto.ChangePasswordDto
import hr.rainbow.data.remote.dto.ImageDto
import hr.rainbow.data.remote.dto.RegisterUserDto
import hr.rainbow.data.remote.dto.UserProfileDto
import hr.rainbow.domain.DataRepository
import hr.rainbow.domain.model.*
import hr.rainbow.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.awaitResponse
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject


class DataRepositoryImpl @Inject constructor(
    private var dataRestApi: DataRestApiInterface
) : DataRepository {

    private var loggedToken = ""
    private var loggedUserId = -1
    private var unexpectedErrorMessage = "Unexpected error occurred!"

    override fun getDiscoverStories(): Flow<Resource<List<Story>>> = flow {
        emit(Resource.Loading(null))
        try {
            val response = dataRestApi.getDiscoverStories(loggedToken, loggedUserId).awaitResponse()
            if (response.isSuccessful) {
                val stories = response.body()?.map { it.toStory() } ?: emptyList()
                emit(Resource.Success(stories))
            } else {
                emit(Resource.Error(response.message() ?: "Unexpected error occurred"))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.message ?: "Unable to reach server!"))
        } catch (e: Throwable) {
            emit(Resource.Error(e.message ?: unexpectedErrorMessage))
        }
    }

    override fun getMyStories(): Flow<Resource<List<Story>>> = flow {
        emit(Resource.Loading(null))
        try {
            val response = dataRestApi.getUserStories(loggedToken, loggedUserId).awaitResponse()
            if (response.isSuccessful) {
                val stories = response.body()?.map { it.toStory() } ?: emptyList()
                emit(Resource.Success(stories))
            } else {
                emit(Resource.Error(response.message() ?: "Unexpected error occurred"))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.message ?: "Unable to reach server!"))
        } catch (e: Throwable) {
            emit(Resource.Error(e.message ?: unexpectedErrorMessage))
        }
    }

    override fun getFavoritesStories(): Flow<Resource<List<Story>>> = flow {
        emit(Resource.Loading(null))
        try {
            val response =
                dataRestApi.getUserFavoritesStories(loggedToken, loggedUserId).awaitResponse()
            if (response.isSuccessful) {
                val stories = response.body()?.map { it.toStory() } ?: emptyList()
                emit(Resource.Success(stories))
            } else {
                emit(Resource.Error(response.message() ?: "Unexpected error occurred"))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.message ?: "Unable to reach server!"))
        } catch (e: Throwable) {
            emit(Resource.Error(e.message ?: unexpectedErrorMessage))
        }
    }

    override fun getStoryCards(storyId: Int): Flow<Resource<List<Card>>> = flow {
        try {
            emit(Resource.Loading(null))

            val response = dataRestApi.getCardsForStory(loggedToken, storyId).awaitResponse()
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    val cards = body.map { it.toCard() }
                    emit(Resource.Success(cards))
                } ?: run {
                    emit(Resource.Error("Unexpected error occurred"))
                }
            } else {
                emit(Resource.Error(response.message() ?: "Unexpected error occurred"))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.message ?: "Unable to reach server!"))
        } catch (e: Throwable) {
            emit(Resource.Error(e.message ?: unexpectedErrorMessage))
        }
    }

    override fun getDaySchedule(date: LocalDate): Flow<Resource<DaySchedule>> = flow {
        emit(Resource.Loading(null))
        try {
            val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
            val dateTime = LocalDateTime.of(date, LocalTime.of(0, 0, 0))

            val response = dataRestApi.getDaySchedule(
                loggedToken,
                loggedUserId,
                formatter.format(dateTime)
            ).awaitResponse()

            if (response.isSuccessful) {
                response.body()?.let {
                    val result = it.toDaySchedule()
                    emit(Resource.Success(result))
                } ?: run {
                    emit(
                        Resource.Success(
                            DaySchedule(
                                dayName = date.dayOfWeek.toString(),
                                tasks = listOf()
                            )
                        )
                    )
                }
            } else {
                emit(Resource.Error(response.message() ?: "Unable to reach server!"))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.message ?: "Unable to reach server!"))
        } catch (e: Throwable) {
            emit(Resource.Error(e.message ?: unexpectedErrorMessage))
        }
    }

    override fun getPictureGallery(): Flow<Resource<HashMap<String, MutableList<Picture>>>> = flow {
        emit(Resource.Loading(null))
        try {
            val response = dataRestApi.getUserImages(loggedToken, loggedUserId).awaitResponse()

            if (response.isSuccessful) {

                response.body()?.let {
                    val images = it.map { t -> t.toPicture() }

                    val gallery: HashMap<String, MutableList<Picture>> =
                        HashMap(12)

                    images.forEach { image ->
                        gallery[image.date.month.toString()]?.add(image) ?: run {
                            gallery.put(image.date.month.toString(), mutableListOf(image))
                        }
                    }
                    emit(Resource.Success(gallery))
                }

            } else {
                emit(Resource.Error(response.message() ?: "Unable to reach server!"))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.message ?: "Unable to reach server!"))
        } catch (e: Throwable) {
            emit(Resource.Error(e.message ?: unexpectedErrorMessage))
        }
    }

    override fun uploadImageUrlForUser(imageUrl: String): Flow<Resource<Boolean>> = flow {
        try {
            val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
            val date = LocalDateTime.now()

            val response = dataRestApi.uploadImage(
                loggedToken,
                ImageDto(
                    IMGTIMESTAMP = formatter.format(date),
                    PROFILEID = 12,
                    URL = imageUrl
                )
            ).awaitResponse()

            if (response.isSuccessful) {
                emit(Resource.Success(true))
            } else {
                emit(Resource.Error(response.message()))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.message ?: "Unable to reach server!"))
        } catch (e: Throwable) {
            emit(Resource.Error(e.message ?: unexpectedErrorMessage))
        }
    }

    override fun deleteImageUrlForUser(picture: Picture): Flow<Resource<Boolean>> = flow {
        try {
            val response = dataRestApi.deleteImage(loggedToken, picture.id).awaitResponse()

            if (response.isSuccessful) {
                emit(Resource.Success(true))
            } else {
                emit(Resource.Error(response.message()))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.message ?: "Unable to reach server!"))
        } catch (e: Throwable) {
            emit(Resource.Error(e.message ?: unexpectedErrorMessage))
        }
    }

    override fun getProfile(): Flow<Resource<UserProfile>> = flow {
        emit(Resource.Loading(null))

        try {
            val profile = getUserProfileData(loggedToken).toUserProfile()
            emit(Resource.Success(profile))

        } catch (e: IOException) {
            emit(Resource.Error(e.message ?: "Unable to reach server!"))
        } catch (e: Throwable) {
            emit(Resource.Error(e.message ?: unexpectedErrorMessage))
        }
    }

    private suspend fun getUserProfileData(authToken: String): UserProfileDto {
        val response =
            dataRestApi.getUserProfile(authToken).awaitResponse()

        if (response.isSuccessful) {
            response.body()?.get(0)?.let {
                return it
            } ?: run {
                throw Exception("Getting user data is unsuccessful!")
            }

        } else {
            throw Exception(response.message() ?: unexpectedErrorMessage)
        }
    }

    override fun updateProfile(profile: UserProfile): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading(null))

        try {
            val result = updateUserProfileDto(profile.toDto(), loggedToken)
            emit(Resource.Success(result))

        } catch (e: IOException) {
            emit(Resource.Error(e.message ?: "Unable to reach server!"))
        } catch (e: Throwable) {
            emit(Resource.Error(e.message ?: unexpectedErrorMessage))
        }
    }

    private suspend fun updateUserProfileDto(profile: UserProfileDto, authToken: String): Boolean {
        val response = dataRestApi.updateUserProfile(authToken, profile).awaitResponse()
        if (response.isSuccessful) {
            return true
        } else {
            if (response.code() == 401) {
                throw Exception("For some reason unable to authorize")
            }
            throw Exception(response.message())
        }
    }

    override fun deleteAccount(userProfile: UserProfile): Flow<Resource<Boolean>> = flow {
        try {
            val response = dataRestApi.deleteAccount(
                authToken = loggedToken,
                userId = loggedUserId
            ).awaitResponse()

            if (response.isSuccessful) {
                emit(Resource.Success(true))
            } else {
                emit(Resource.Error(response.message()))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.message ?: "Unable to reach server!"))
        } catch (e: Throwable) {
            emit(Resource.Error(e.message ?: unexpectedErrorMessage))
        }
    }

    override fun like(storyId: Int, isLiked: Boolean): Flow<Resource<Boolean>> = flow {
        try {
            val response = dataRestApi.likeStory(
                authToken = loggedToken,
                userId = loggedUserId,
                storyId = storyId,
                isLiked = isLiked
            ).awaitResponse()

            if (response.isSuccessful) {
                emit(Resource.Success(true))
            } else {
                emit(Resource.Error(response.message()))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.message ?: "Unable to reach server!"))
        } catch (e: Throwable) {
            emit(Resource.Error(e.message ?: unexpectedErrorMessage))
        }
    }

    override fun changePassword(
        oldPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading(null))
        try {
            val response = dataRestApi.changePassword(
                loggedToken,
                ChangePasswordDto(
                    OldPassword = oldPassword,
                    NewPassword = newPassword,
                    ConfirmPassword = confirmNewPassword
                )
            ).awaitResponse()

            if (response.isSuccessful) {
                emit(Resource.Success(true))
            } else {
                emit(Resource.Error(response.message()))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.message ?: "Unable to reach server!"))
        } catch (e: Throwable) {
            emit(Resource.Error(e.message ?: unexpectedErrorMessage))
        }
    }

    override fun logIn(email: String, password: String): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading(null))
        try {
            val authToken = getLogInAuthToken(email, password)
            val userProfileData = getUserProfileData(authToken)

            loggedToken = authToken
            loggedUserId = userProfileData.IDPROFILE
            emit(Resource.Success(true))
            //save user id and auth token
        } catch (e: IOException) {
            emit(Resource.Error(e.message ?: "Unable to reach server!"))
        } catch (e: Throwable) {
            emit(Resource.Error(e.message ?: unexpectedErrorMessage))
        }
    }

    private suspend fun getLogInAuthToken(email: String, password: String): String {
        val response = dataRestApi.getToken(
            email = email,
            password = password
        ).awaitResponse()

        if (response.isSuccessful) {
            response.body()?.let {
                return "Bearer ${it.access_token}"
            } ?: run {
                throw Exception("Authorization is unsuccessful!")
            }

        } else {
            throw Exception(response.message() ?: unexpectedErrorMessage)
        }

    }

    override fun register(
        userProfile: UserProfile,
        password: String,
        confirmPassword: String
    ): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading(null))

        try {
            val userProfileData = createNewUser(userProfile.email, password, confirmPassword)
            val authToken = getLogInAuthToken(userProfile.email, password)
            userProfile.id = userProfileData.IDPROFILE
            val result = updateUserProfileDto(userProfile.toDto(), authToken)
            loggedToken = authToken
            loggedUserId = userProfile.id
            emit(Resource.Success(result))
        } catch (e: IOException) {
            emit(Resource.Error(e.message ?: "Unable to reach server!"))
        } catch (e: Throwable) {
            emit(Resource.Error(e.message ?: unexpectedErrorMessage))
        }
    }

    private suspend fun createNewUser(
        email: String,
        password: String,
        confirmPassword: String
    ): UserProfileDto {
        val response = dataRestApi.register(
            RegisterUserDto(
                email,
                password,
                confirmPassword
            )
        ).awaitResponse()

        if (response.isSuccessful) {
            response.body()?.get(0)?.let {
                return it
            } ?: run {
                throw Exception("Register is unsuccessful")
            }
        } else {
            throw Exception(response.message())
        }
    }

    override fun searchByTag(query: String): Flow<Resource<List<Story>>> = flow {
        emit(Resource.Loading(null))
        try {
            val response = dataRestApi.searchByTag(loggedToken, loggedUserId, query)
                .awaitResponse()

            if (response.isSuccessful) {
                val stories = response.body()?.map { it.toStory() } ?: emptyList()
                emit(Resource.Success(stories))
            } else {
                emit(Resource.Error(response.message() ?: "Unexpected error occurred"))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.message ?: "Unable to reach server!"))
        } catch (e: Throwable) {
            emit(Resource.Error(e.message ?: unexpectedErrorMessage))
        }
    }

    override fun getTagSuggestions(query: String): Flow<Resource<List<String>>> = flow {
        emit(Resource.Success(data = listOf()))
    }

    override fun getAllTags(): Flow<Resource<List<String>>> = flow {
        emit(Resource.Loading(null))
        try {
            val response =
                dataRestApi.getAllTags(authToken = loggedToken, userId = loggedUserId)
                    .awaitResponse()

            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Resource.Success(it))
                } ?: run {
                    emit(Resource.Error(unexpectedErrorMessage))
                }
            } else {
                emit(Resource.Error(response.message()))
            }
        } catch (e: IOException) {
            emit(Resource.Error(e.message ?: "Unable to reach server!"))
        } catch (e: Throwable) {
            emit(Resource.Error(e.message ?: unexpectedErrorMessage))
        }
    }
}