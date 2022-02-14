package hr.rainbow.di

import android.content.Context
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import androidx.work.Configuration
import androidx.work.WorkManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hr.rainbow.R
import hr.rainbow.data.DataRepositoryImpl
import hr.rainbow.data.MediaRepositoryImpl
import hr.rainbow.data.remote.AzureMediaInterface
import hr.rainbow.data.remote.DataRestApiInterface
import hr.rainbow.domain.DataRepository
import hr.rainbow.domain.MediaRepository
import hr.rainbow.domain.WorkerFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideDataRepository(service: DataRestApiInterface): DataRepository =
        DataRepositoryImpl(service)

    @Singleton
    @Provides
    fun provideMediaRepository(
        service: AzureMediaInterface
    ): MediaRepository = MediaRepositoryImpl(service)

    @Singleton
    @Provides
    fun provideGlide(
        @ApplicationContext context: Context
    ): RequestManager = Glide.with(context).setDefaultRequestOptions(
        RequestOptions()
            .placeholder(CircularProgressDrawable(context).apply {
                strokeWidth = 5f
                centerRadius = 30f
                start()
            }
            )
            .error(R.drawable.load_error)
    )

    @Singleton
    @Provides
    @Named("NoCache")
    fun provideGlideNoCache(
        @ApplicationContext context: Context
    ): RequestManager = Glide.with(context).setDefaultRequestOptions(
        RequestOptions()
            .placeholder(CircularProgressDrawable(context).apply {
                strokeWidth = 5f
                centerRadius = 30f
                start()
            }
            )
            .error(R.drawable.load_error)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
    )

    @Singleton
    @Provides
    fun provideAzureRetrofit(
        okClient: OkHttpClient
    ): AzureMediaInterface = Retrofit.Builder()
        .baseUrl(AzureMediaInterface.BASE_URL)
        .client(okClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(AzureMediaInterface::class.java)

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideDataRestApiRetrofit(
        okClient: OkHttpClient
    ): DataRestApiInterface {
        return Retrofit.Builder()
            .baseUrl(DataRestApiInterface.BASE_URL)
            .client(okClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DataRestApiInterface::class.java)
    }

    @Singleton
    @Provides
    fun provideWorkFactory(
        mediaRepository: MediaRepository,
        dataRepository: DataRepository
    ): WorkerFactory = WorkerFactory(
        mediaRepository,
        dataRepository
    )

    @Singleton
    @Provides
    fun provideWorkManagerConfig(
        workerFactory: WorkerFactory
    ): Configuration = Configuration.Builder()
        .setWorkerFactory(workerFactory)
        .build()

    @Singleton
    @Provides
    fun provideWorkManager(
        @ApplicationContext context: Context,
        config: Configuration
    ): WorkManager {
        WorkManager.initialize(context, config)

        return WorkManager.getInstance(context)
    }
}