package com.example.weathers.data.model

import com.google.android.gms.maps.model.LatLng

data class Weather(
    val code: String = "",
    val adress: String = "",
    val level: Int = 0,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val baseDate: String = "",
    val baseTime: String = "",
    val category: Map<String, Map<String, String>> = mapOf(),
    val descriptor: WeatherDescriptor = RealTimeDescriptor(),
    val status: WeatherStaus = WeatherStaus.Success
) {

    val shortAdress : String
        get() = if (adress.length > 5) "..${adress.substring(adress.length - 5)}" else adress

    val latLng: LatLng
        get() = LatLng(latitude, longitude)

    val temp : String
        get() = descriptor.getTemp(category)

    val sky : String
        get() = descriptor.getSky(category)

    val skyIcon : Int
        get() = descriptor.getSkyIcon(category)

    val date : String
        get() = "$baseDate - $baseTime"
    val humidity : String
        get() = descriptor.getHumidity(category)
    val rainfall : String
        get() = descriptor.getRainfall(category)
    val rainfallPer : String
        get() = descriptor.getRainfallPer(category)
    val windSpeed : String
        get() = descriptor.getWindSpeed(category)

    fun toContent() = StringBuilder().run {
        appendLine(adress)
        appendLine("$baseDate - $baseTime")
        category.map { (date, cateMap) ->
            val cateString = cateMap.map { (k, v) ->
                val category = WeatherCategory.valueOf(k)
                "${category.desc} : $v\n"
            }.joinToString("")
            "${date}\n${cateString}"
        }.forEach { appendLine(it) }
        toString()
    }
}

data class WeatherWithPosition(var position: Int, val weather: Weather)

enum class WeatherStaus {
    Success,
    Fail
}