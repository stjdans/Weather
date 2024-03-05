package com.example.weathers.data.source.network.model

data class ResponseWeather(
    val response: Resp
)

data class Resp(
    val header: WeatherHeader,
    val body: WeatherBody
)

data class WeatherHeader(
    val resultCode: String,
    val resultMsg: String
)

data class WeatherBody(
    val dataType: String,
    val pageNo: String,
    val numOfRows: String,
    val totalCount: String,
    val items: WeatherItems
)

data class WeatherItems(
    val item: List<WeatherItem>
)

data class WeatherItem(
    val baseDate: String,
    val baseTime: String,
    val category: String,
    val nx: String,
    val ny: String,
    val obsrValue: String,   //초단기 실황
    val fcstDate: String,   //초단기 예보
    val fcstTime: String,   //초단기 예보
    val fcstValue: String   //초단기 예보
)