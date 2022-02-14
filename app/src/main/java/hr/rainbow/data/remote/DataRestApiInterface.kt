package hr.rainbow.data.remote

import hr.rainbow.data.remote.dto.*
import hr.rainbow.domain.model.UserProfile
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface DataRestApiInterface {
    @GET("/api/Images/GetUserImages")
    fun getUserImages(
        @Header("Authorization") authToken: String,
        @Query("userId") userId: Int): Call<List<ImageDto>>

    @POST("/api/Images/InsertImage")
    fun uploadImage(
        @Header("Authorization") authToken: String,
        @Body image: ImageDto
    ): Call<ResponseBody>

    @DELETE("/api/Images/DeleteImage")
    fun deleteImage(
        @Header("Authorization") authToken: String,
        @Query("idImage") imageId: Int
    ): Call<ResponseBody>

    @GET("/api/Stories/DiscoverStories")
    fun getDiscoverStories(
        @Header("Authorization") authToken: String,
        @Query("userId") userId: Int
    ): Call<List<StoryDto>>

    @GET("/api/Stories/GetUserStories")
    fun getUserStories(
        @Header("Authorization") authToken: String,
        @Query("userId") userId: Int
    ): Call<List<StoryDto>>

    @GET("/api/Stories/GetUserFavouriteStories")
    fun getUserFavoritesStories(
        @Header("Authorization") authToken: String,
        @Query("userId") userId: Int
    ): Call<List<StoryDto>>

    @GET("/api/Cards/GetStoryCards")
    fun getCardsForStory(
        @Header("Authorization") authToken: String,
        @Query("storyId") storyId: Int
    ): Call<List<CardDto>>


    @POST("/api/Account/Register")
    fun register(
        @Body registerUser: RegisterUserDto
    ): Call<List<UserProfileDto>>

    @FormUrlEncoded
    @POST("/Token")
    fun getToken(
        @Field("username") email: String,
        @Field("password") password: String,
        @Field("grant_type") grantType: String = "password"
    ): Call<TokenDto>

    @PUT("/api/Account/UpdateProfile")
    fun updateUserProfile(
        @Header("Authorization") authToke: String,
        @Body userProfile: UserProfileDto
    ): Call<ResponseBody>

    @GET("/api/Account/GetProfile")
    fun getUserProfile(
        @Header("Authorization") authToken: String
    ): Call<List<UserProfileDto>>

    @POST("/api/Account/ChangePassword")
    fun changePassword(
        @Header("Authorization") authToken: String,
        @Body changePasswordDto: ChangePasswordDto
    ): Call<ResponseBody>

    @DELETE("/api/Account/DeleteAccount")
    fun deleteAccount(
        @Header("Authorization") authToken: String,
        @Query("profileId") userId: Int
    ): Call<ResponseBody>

    @POST("/api/Story/LikeStory")
    fun likeStory(
        @Header("Authorization") authToken: String,
        @Query("userId") userId: Int,
        @Query("storyId") storyId: Int,
        @Query("status") isLiked: Boolean
    ): Call<ResponseBody>

    @GET("/api/Search/SearchStoryTags")
    fun getAllTags(
        @Header("Authorization") authToken: String,
        @Query("profileId") userId: Int
    ): Call<List<String>>

    @GET("/api/Search/SearchStoryByTag")
    fun searchByTag(
        @Header("Authorization") authToken: String,
        @Query("userId") userId: Int,
        @Query("tag") query: String
    ): Call<List<StoryDto>>

    @GET("/api/Schedules/GetSchedule")
    fun getDaySchedule(
        @Header("Authorization") authToken: String,
        @Query("userId") userId: Int,
        @Query("date") query: String
    ): Call<ScheduleDto>


    companion object {
        const val BASE_URL = "https://happypicturesapi.azurewebsites.net/"
    }
}