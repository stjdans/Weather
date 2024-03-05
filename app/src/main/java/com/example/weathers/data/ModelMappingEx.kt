package com.example.weathers.data

import com.example.weathers.data.model.UltraForecastDescriptor
import com.example.weathers.data.source.local.Location
import com.example.weathers.data.source.local.RealTime
import com.example.weathers.data.source.local.UltraForecast
import com.example.weathers.data.source.network.model.RequestType
import com.example.weathers.data.source.network.model.RequestWeather
import com.example.weathers.data.model.Weather
import com.example.weathers.data.source.network.model.WeatherBody
import com.example.weathers.data.model.WeatherCategory
import com.example.weathers.data.model.WeatherWithPosition
import com.example.weathers.data.source.network.model.WeatherItem
import java.text.SimpleDateFormat
import java.util.Calendar
import kotlin.reflect.full.declaredMemberProperties

fun WeatherBody.getRealTimeWeather(location: Location) = Weather(
    code = location.code,
    adress = location.adress,
    latitude = location.latitude4_100,
    longitude = location.longitude4_100,
    level = location.level,
    baseDate = items.item[0].baseDate,
    baseTime = items.item[0].baseTime,
    category = items.item.groupBy { it.baseDate + it.baseTime }
        .mapValues { (k, v) -> v.toRealTimeCategoryMap() }
)

fun WeatherBody.getUltraForecastWeather(location: Location) = Weather(
    code = location.code,
    adress = location.adress,
    latitude = location.latitude4_100,
    longitude = location.longitude4_100,
    level = location.level,
    baseDate = items.item[0].baseDate,
    baseTime = items.item[0].baseTime,
    descriptor = UltraForecastDescriptor(),
    category = items.item
        .groupBy { it.fcstDate + it.fcstTime }
        .mapValues { (k, v) -> v.toFcsCategoryMap() }
)

fun List<WeatherItem>.toRealTimeCategoryMap() = associate { it.category to it.obsrValue }

fun List<WeatherItem>.toFcsCategoryMap() = associate { it.category to it.fcstValue }

// RealTime Basetime
fun Calendar.baseTime(createTime: Int, pattern: String) = run {
    val min = get(Calendar.MINUTE)
    if (min < createTime)
        add(Calendar.HOUR_OF_DAY, -1)

    val fString = SimpleDateFormat(pattern).format(time).split(",")

    Pair(fString[0], fString[1])
}

// RealTime Basetime
fun Calendar.rtBaseTime() = Calendar.getInstance().baseTime(40, "yyyyMMdd,HH00")

// UltraForecast Basetime
fun Calendar.ufctBaseTime() = Calendar.getInstance().baseTime(45, "yyyyMMdd,HH30")

fun RequestWeather.toQueryMap() = this::class.declaredMemberProperties.map { prop ->
    prop.name to prop.getter.call(this).toString()
}.toMap()

// Location
fun Location.toQueryMap(type: RequestType) = toRequestWeather(type).toQueryMap()

fun Location.toRequestWeather(type: RequestType) = run {
    val (date, time) = when(type) {
        RequestType.REALTIME -> Calendar.getInstance().rtBaseTime()
        RequestType.ULTRAFORECAST -> Calendar.getInstance().ufctBaseTime()
    }
    RequestWeather(
        base_date = date,
        base_time = time,
        nx = dx.toString(),
        ny = dy.toString()
    )
}

// Weather
fun Weather.toRealTime() = RealTime(
    code = code,
    adress = adress,
    baseDate = baseDate,
    baseTime = baseTime,
    categories = categoryString()
)

fun Weather.toUltraForecast() = UltraForecast(
    code = code,
    adress = adress,
    baseDate = baseDate,
    baseTime = baseTime,
    categories = categoryString()
)

// Category 형식
//202402151700#PTY,0|REH,69%|RN1,0mm|T1H,3.3C|UUU,0.9m/s|VEC,325deg|VVV,-1.2m/s|WSD,1.6m/s*
// 202402151700#PTY,0|REH,69%|RN1,0mm|T1H,3.3C|UUU,0.9m/s|VEC,325deg|VVV,-1.2m/s|WSD,1.6m/s
fun Weather.categoryString() = category
    .map { (date, cateMap) -> "$date#${cateMap.map { (k, v) -> "$k,$v" }.joinToString("|")}" }
    .joinToString("*")

fun Weather.toTimeBaseList() = category.map { (k, v) ->
    Weather(
        code = code,
        adress = adress,
        latitude = latitude,
        longitude = longitude,
        level = level,
        baseDate = k.substring(0, k.length - 4),
        baseTime = k.substring(k.length - 4),
        category = mapOf(k to v),
        descriptor = UltraForecastDescriptor()
    )
}
fun List<Weather>.toListWithPosition() = mapIndexed { index, weather -> WeatherWithPosition(index, weather)}


// RealTime
fun RealTime.toWeather(location: Location) = Weather(
    code = location.code,
    adress = location.adress,
    latitude = location.latitude4_100,
    longitude = location.longitude4_100,
    level = location.level,
    baseDate = baseDate,
    baseTime = baseTime,
    category = categories.categoryMap()
)

fun String.categoryMap() = split("*").map { it.split("#") }
    .associate { (date, cateString) ->
        val cateMap = cateString.split("|").associate { s ->
            val (k, v) = s.split(",")
            k to v
        }
        date to cateMap
    }

// UltraForecast
fun UltraForecast.toWeather(location: Location) = Weather(
    code = location.code,
    adress = location.adress,
    latitude = location.latitude4_100,
    longitude = location.longitude4_100,
    level = location.level,
    baseDate = baseDate,
    baseTime = baseTime,
    category = categories.categoryMap()
)
