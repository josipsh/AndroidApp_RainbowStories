package hr.rainbow.domain

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import hr.rainbow.domain.workers.DeleteImageWorker
import hr.rainbow.domain.workers.UploadImageWorker
import hr.rainbow.domain.workers.UploadUrlWorker

class WorkerFactory(
    private val mediaRepository: MediaRepository,
    private val dataRepository: DataRepository
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {

        return when (workerClassName) {
            UploadImageWorker::class.java.name -> {
                val instance = UploadImageWorker(appContext, workerParameters)
                instance.mediaRepository = mediaRepository
                instance.dataRepository = dataRepository
                instance
            }
            UploadUrlWorker::class.java.name -> {
                UploadUrlWorker(appContext, workerParameters, dataRepository)
            }
            DeleteImageWorker::class.java.name -> {
                val instance = DeleteImageWorker(appContext, workerParameters)
                instance.mediaRepository = mediaRepository
                instance.dataRepository = dataRepository
                instance
            }
            else -> {
                null
            }
        }
    }


}