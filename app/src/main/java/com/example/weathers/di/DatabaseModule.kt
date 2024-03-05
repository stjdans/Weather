package com.example.weathers.di

import android.content.Context
import androidx.room.Room
import com.example.weathers.data.source.local.WeatherDatabase
import com.example.weathers.data.source.local.store.settingsDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideLocationDatabase(
        @ApplicationContext context: Context
    ): WeatherDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            WeatherDatabase::class.java,
            "weather.db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideLocationDao(weatherDatabase: WeatherDatabase) = weatherDatabase.locationDao()

    @Singleton
    @Provides
    fun provideWeatherDao(weatherDatabase: WeatherDatabase) = weatherDatabase.weatherDao()

    @Singleton
    @Provides
    fun provideSettingDataStore(@ApplicationContext context: Context) = context.settingsDataStore
}