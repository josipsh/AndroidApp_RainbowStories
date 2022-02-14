package hr.rainbow.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TokenDto(
    @SerializedName(".expires")
    val expires: String,
    @SerializedName(".issued")
    val issued: String,
    val access_token: String,
    val expires_in: Int,
    val token_type: String,
    val userName: String
)