package com.example.weathers.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey

sealed class LocalWeather

//@Entity(tableName = "realTime", primaryKeys = ["code", "baseDate", "baseTime"])
@Entity(tableName = "realTime")
data class RealTime(
    @PrimaryKey
    val code: String,
    val adress: String,
    val baseDate: String,
    val baseTime: String,
    val categories: String
): LocalWeather() {
    fun equalBase(date: String, time: String) = baseDate.isNotEmpty() && date == baseDate && baseTime.isNotEmpty() && time == baseTime
}

//@Entity(tableName = "ultra_forecast", primaryKeys = ["code", "baseDate", "baseTime"])
@Entity(tableName = "ultra_forecast")
data class UltraForecast(
    @PrimaryKey
    val code: String,
    val adress: String,
    val baseDate: String,
    val baseTime: String,
    val categories: String
): LocalWeather() {
    fun equalBase(date: String, time: String) = baseDate.isNotEmpty() && date == baseDate && baseTime.isNotEmpty() && time == baseTime
}