package com.example.weathers.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Location::class, RealTime::class, UltraForecast::class], version = 1)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun locationDao(): LocationDao

    abstract fun weatherDao(): WeatherDao
}