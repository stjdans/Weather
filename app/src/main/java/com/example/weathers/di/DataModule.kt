package com.example.weathers.di

import android.util.Log
import com.example.weathers.data.DefaultWeatherRepository
import com.example.weathers.data.WeatherRepository
import com.example.weathers.data.source.network.MyLoggingInterceptor
import com.example.weathers.data.source.network.WeatherNetworkDataSource
import com.example.weathers.data.source.network.TAG_INTERCEPTOR
import com.example.weathers.data.source.network.NetworkDataSource
import com.example.weathers.data.source.network.WeatherService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier

@Module
@InstallIn(ActivityComponent::class)
abstract class RepositoryImplModule {

    @Binds
    abstract fun bindWeatherRepository(defaultWeatherRepository: DefaultWeatherRepository): WeatherRepository
}

@Module
@InstallIn(ActivityComponent::class)
abstract class DataSoruceImplModule {

    @Binds
    abstract fun bindWeatherDataSource(networkWeatherDataSource: WeatherNetworkDataSource): NetworkDataSource
}

@Module
@InstallIn(ActivityComponent::class)
object DataSoruceModule {
    @Provides
    fun provideWeatherService(
        @InterceptorOkHttpClient okHttpClient: OkHttpClient
    ): WeatherService {
        return Retrofit.Builder()
            .baseUrl("http://apis.data.go.kr/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherService::class.java)
    }

    @DefaultOkHttpClient
    @Provides
    fun provideOkhttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @InterceptorOkHttpClient
    @Provides
    fun provideLoggingOkhttpClient(
        @LoggingInterceptor interceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @LoggingInterceptor
    @Provides
    fun provideHttpLoggingInterceptor(): Interceptor = HttpLoggingInterceptor {
        Log.i(TAG_INTERCEPTOR, it)
    }.apply { level = HttpLoggingInterceptor.Level.BODY }

    @CustomLoggingInterceptor
    @Provides
    fun provideMyLoggingInterceptor(): Interceptor = MyLoggingInterceptor()
}

@Module
@InstallIn(SingletonComponent::class)
object CoroutineModule {

    @IoDispatcher
    @Provides
    fun provideIoDispacher(): CoroutineDispatcher = Dispatchers.IO

    @MainDispatcher
    @Provides
    fun provideMainDispacher(): CoroutineDispatcher = Dispatchers.Main

    @ApplicationScope
    @Provides
    fun provideApplicationScope(
        @IoDispatcher coroutineDispatcher: CoroutineDispatcher
    ): CoroutineScope  = CoroutineScope(SupervisorJob() + coroutineDispatcher)
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApplicationScope

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class InterceptorOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LoggingInterceptor

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CustomLoggingInterceptor