package com.example.weathers.di

import android.content.Context
import com.example.weathers.data.source.sensor.LocationProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ServiceModule {

    @Provides
    @Singleton
    fun provideLocationProvider(@ApplicationContext context: Context) = LocationProvider(context)

//    @Provides
//    @Singleton
//    fun provideGoogleMapManager(@ApplicationScope scope: CoroutineScope) = GoogleMapProvider(scope)
}