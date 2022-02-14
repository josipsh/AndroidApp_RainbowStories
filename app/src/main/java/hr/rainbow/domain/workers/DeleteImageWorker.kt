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

class DeleteImageWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params)  {

    lateinit var mediaRepository: MediaRepository
    lateinit var dataRepository: DataRepository

    override suspend fun doWork(): Result {
        val context = applicationContext

        try {
            val imageUrl = inputData.getString(KEY_DELETE_IMAGE_URL)

            if (imageUrl.isNullOrEmpty()) {
                throw IllegalArgumentException("Error occurred! Image url didn't come!")
            }

            return when (val result = mediaRepository.deleteImage(imageUrl)) {
                is Resource.Success -> {
                    notifyProgressNotification(
                        context,
                        UPLOAD_NOTIFICATION_ID,
                        CHANNEL_ID_MEDIA,
                        context.getString(R.string.media_delete),
                        context.getString(R.string.successfully_deleted),
                        0,
                        0,
                        false
                    )
                    Result.success()
                }
                is Resource.Error -> {
                    notifyProgressNotification(
                        context,
                        UPLOAD_NOTIFICATION_ID,
                        CHANNEL_ID_MEDIA,
                        context.getString(R.string.media_delete),
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
                        context.getString(R.string.media_delete),
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
                context.getString(R.string.media_delete),
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