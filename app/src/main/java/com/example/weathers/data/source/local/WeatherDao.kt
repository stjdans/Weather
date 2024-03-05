package com.example.weathers.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert

@Dao
interface WeatherDao {
//    @Insert
//    fun inserts(vararg realTime: RealTime)

    @Upsert
    fun upserts(vararg realTime: RealTime)

    @Delete
    fun deletes(vararg realTime: RealTime)

    @Query("SELECT * FROM realTime")
    fun getRealtimes(): List<RealTime>

    @Query("SELECT * FROM realTime WHERE code = :code")
    fun getRealTimeByCode(code: String): RealTime?

    @Upsert
    fun upserts(vararg ultraForecast: UltraForecast)

    @Delete
    fun deletes(vararg ultraForecast: UltraForecast)

    @Query("SELECT * FROM ultra_forecast")
    fun getUltraForecasts(): List<UltraForecast>

    @Query("SELECT * FROM ultra_forecast WHERE code = :code")
    fun getUltraForecastByCode(code: String): UltraForecast?
}