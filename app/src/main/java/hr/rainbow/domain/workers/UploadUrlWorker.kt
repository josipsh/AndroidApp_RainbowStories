package hr.rainbow.domain.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import hr.rainbow.R
import hr.rainbow.domain.DataRepository
import hr.rainbow.util.*
import kotlinx.coroutines.flow.collect

class UploadUrlWorker(
    ctx: Context,
    params: WorkerParameters,
    private val dataRepository: DataRepository
) : CoroutineWorker(ctx, params) {

    override suspend fun doWork(): Result {
        val context = applicationContext

        try {
            val url = inputData.getString(KEY_UPLOAD_IMAGE_URL)

            var allGood = false
            url?.let {
                dataRepository.uploadImageUrlForUser(url).collect {
                    when (it) {
                        is Resource.Success -> {
                            notifyProgressNotification(
                                context,
                                UPLOAD_NOTIFICATION_ID,
                                CHANNEL_ID_MEDIA,
                                context.getString(R.string.Media_upload),
                                context.getString(R.string.uploaded),
                                0,
                                0,
                                false
                            )
                            allGood = true
                        }
                        is Resource.Error -> {
                            notifyProgressNotification(
                                context,
                                UPLOAD_NOTIFICATION_ID,
                                CHANNEL_ID_MEDIA,
                                context.getString(R.string.Media_upload),
                                it.message ?: context.getString(R.string.unexpected_error),
                                0,
                                0,
                                false
                            )
                            allGood = false
                        }
                        else -> {
                            notifyProgressNotification(
                                context,
                                UPLOAD_NOTIFICATION_ID,
                                CHANNEL_ID_MEDIA,
                                context.getString(R.string.Media_upload),
                                context.getString(R.string.unexpected_error),
                                0,
                                0,
                                false
                            )
                            allGood = false
                        }
                    }
                }
            }

            return if (allGood) {
                Result.success()
            } else {
                Result.failure()
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
            return Result.failure()
        }

    }
}