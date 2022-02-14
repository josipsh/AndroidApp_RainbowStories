package hr.rainbow.domain

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.core.app.JobIntentService
import dagger.hilt.android.AndroidEntryPoint
import hr.rainbow.data.local.tag_search_suggestion.TAG_SEARCH_SUGGESTION_PROVIDER_URI
import hr.rainbow.util.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

private const val JOB_ID = 1

@AndroidEntryPoint
class DownloadDataService : JobIntentService() {

    @Inject
    lateinit var dataRepository: DataRepository

    override fun onHandleWork(intent: Intent) {
        try {
            runBlocking {
                dataRepository.getAllTags().collect {
                    when (it) {
                        is Resource.Success -> {
                            it.data?.let { data ->
                                saveInDb(data)
                            }
                            delay(1000)
                            sendBroadcast(
                                Intent(
                                    this@DownloadDataService,
                                    DownloadDataReceiver::class.java
                                )
                            )
                        }
                        is Resource.Error -> {
                            delay(2500)
                            sendBroadcast(
                                Intent(
                                    this@DownloadDataService,
                                    DownloadDataReceiver::class.java
                                )
                            )
                        }
                        is Resource.Loading -> Unit
                    }
                }
            }
        } catch (e: Throwable) {
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    sendBroadcast(
                        Intent(
                            this@DownloadDataService,
                            DownloadDataReceiver::class.java
                        )
                    )
                },
                2500
            )
        }
    }

    private fun saveInDb(data: List<String>) {
        data.forEach {
            contentResolver.insert(
                TAG_SEARCH_SUGGESTION_PROVIDER_URI,
                ContentValues().apply {
                    put("tag", it)
                }
            )
        }
    }

    companion object {
        fun enqueueService(context: Context) {
            enqueueWork(
                context,
                DownloadDataService::class.java,
                JOB_ID,
                Intent(context, DownloadDataService::class.java)
            )
        }
    }
}