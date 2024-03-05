package com.example.weathers.data.source.network.model

import com.example.weathers.BuildConfig

data class RequestWeather(
    val serviceKey: String = BuildConfig.WEATHER_API_KEY,
    val numOfRows: String = "1000",
    val pageNo: String = "1",
    val dataType: String = "JSON",
    val base_date: String = "20240208",
    val base_time: String = "0800",
    val nx: String = "55",
    val ny: String = "127"
)

enum class RequestType {
    REALTIME,
    ULTRAFORECAST
}

