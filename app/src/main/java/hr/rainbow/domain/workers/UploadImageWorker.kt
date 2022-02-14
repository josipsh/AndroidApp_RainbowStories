package hr.rainbow.domain.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import hr.rainbow.R
import hr.rainbow.domain.DataRepository
import hr.rainbow.domain.MediaRepository
import hr.rainbow.util.*

class UploadImageWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {

    lateinit var mediaRepository: MediaRepository
    lateinit var dataRepository: DataRepository

    override suspend fun doWork(): Result {
        val context = applicationContext
        val contentResolver = context.contentResolver

        try {
            val imageUri = inputData.getString(KEY_UPLOAD_IMAGE_URI)

            if (imageUri.isNullOrEmpty()) {
                throw IllegalArgumentException("Error occurred! Image uri didn't come!")
            }

            val imageBitmap = BitmapFactory.decodeStream(
                contentResolver.openInputStream(
                    Uri.parse(imageUri)
                )
            )

            return when (val result = mediaRepository.uploadImage(imageBitmap)) {
                is Resource.Success -> {
                    val data = result.data!!
                    Result.success(
                        Data.Builder()
                            .putString(KEY_UPLOAD_IMAGE_URL, data)
                            .build()
                    )
                }
                is Resource.Error -> {
                    notifyProgressNotification(
                        context,
                        UPLOAD_NOTIFICATION_ID,
                        CHANNEL_ID_MEDIA,
                        context.getString(R.string.Media_upload),
                        result.message ?: context.getString(R.string.error_occurred),
                        0,
                        0,
                        false
                    )
                    Log.e("UploadMediaWorker", result.message ?: "Unknown error occurred")
                    Result.failure()
                }
                else -> {
                    notifyProgressNotification(
                        context,
                        UPLOAD_NOTIFICATION_ID,
                        CHANNEL_ID_MEDIA,
                        context.getString(R.string.Media_upload),
                        context.getString(R.string.error_occurred),
                        0,
                        0,
                        false
                    )
                    Log.e("UploadMediaWorker", "Unknown error occurred")
                    Result.failure()
                }

            }
        } catch (e: Throwable) {
            notifyProgressNotification(
                context,
                UPLOAD_NOTIFICATION_ID,
                CHANNEL_ID_MEDIA,
                context.getString(R.string.Media_upload),
                context.getString(R.string.error_occurred),
                0,
                0,
                false
            )
            Log.e("UploadMediaWorker", e.message.toString(), e)
            return Result.failure()
        }
    }
}