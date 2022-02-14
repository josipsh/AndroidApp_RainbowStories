package hr.rainbow.data.remote

import hr.rainbow.util.AZURE_SAS_KEY
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface AzureMediaInterface {

    @Headers(
        "x-ms-version:2020-12-06",
        "x-ms-blob-type:BlockBlob",
        "Content-Type:image/png",
    )
    @GET("/images/{fileName}?$AZURE_SAS_KEY")
    fun getImage(
        @Header("x-ms-date") date: String,
        @Path("fileName") fileNAme: String
    ): Call<ResponseBody>

    @Headers(
        "x-ms-version:2020-12-06",
        "x-ms-blob-type:BlockBlob"
    )
    @PUT("/images/{fileName}?$AZURE_SAS_KEY")
    fun uploadImage(
        @Header("x-ms-date") date: String,
        @Path("fileName") fileNAme: String,
        @Body image: RequestBody
    ): Call<ResponseBody>

    @Headers(
        "x-ms-version:2020-12-06",
        "x-ms-blob-type:BlockBlob"
    )
    @DELETE("/images/{fileName}?$AZURE_SAS_KEY")
    fun deleteImage(
        @Header("x-ms-date") date: String,
        @Path("fileName") fileNAme: String,
    ): Call<ResponseBody>

    companion object {
        const val BASE_URL = "https://ojcar2storage2account.blob.core.windows.net"
    }
}