package hr.rainbow.data

import android.graphics.Bitmap
import hr.rainbow.data.remote.AzureMediaInterface
import hr.rainbow.domain.MediaRepository
import hr.rainbow.util.Resource
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(
    val service: AzureMediaInterface
) : MediaRepository {

    override fun downloadImage(url: String): Bitmap {
        return Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
    }

    override fun uploadImage(image: Bitmap): Resource<String> {
        try {
            val baos = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.PNG, 50, baos)

            val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
            val date = LocalDateTime.now()
            val fileName = "Android_${formatter.format(date)}.png"

            val response = service.uploadImage(
                formatter.format(date),
                fileName,
                RequestBody.create(
                    MediaType.parse("image/png"),
                    baos.toByteArray()
                )
            ).execute()

            return if (response.isSuccessful) {
                Resource.Success("${AzureMediaInterface.BASE_URL}/images/$fileName")
            } else {
                Resource.Error("${response.body()}")
            }
        } catch (e: IOException) {
            return Resource.Error(e.message ?: "Unable to reach server!")
        } catch (e: Throwable) {
            return Resource.Error(e.message ?: "Unexpected error occurred")
        }
    }

    override fun deleteImage(imageUrl: String): Resource<Boolean> {
        try {
            val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
            val date = LocalDateTime.now()

            val fileName = imageUrl
                .replaceBeforeLast('/', "")
                .replaceFirst("/", "")

            val response = service.deleteImage(
                formatter.format(date),
                fileName
            ).execute()

            return if (response.isSuccessful) {
                Resource.Success(true)
            } else {
                Resource.Error("${response.body() ?: "Unexpected error occurred"}")
            }
        } catch (e: IOException) {
            return Resource.Error(e.message ?: "Unable to reach server!")
        } catch (e: Throwable) {
            return Resource.Error(e.message ?: "Unexpected error occurred")
        }
    }
}