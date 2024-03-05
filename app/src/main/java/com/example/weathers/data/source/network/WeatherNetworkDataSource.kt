package com.example.weathers.data.source.network

import com.example.weathers.data.getRealTimeWeather
import com.example.weathers.data.getUltraForecastWeather
import com.example.weathers.data.source.local.Location
import com.example.weathers.data.source.network.model.RequestType
import com.example.weathers.data.model.Weather
import com.example.weathers.data.toQueryMap
import com.example.weathers.data.model.WeatherStaus
import javax.inject.Inject

class WeatherNetworkDataSource @Inject constructor(
    private val weatherService: WeatherService
) : NetworkDataSource {
    override suspend fun getRealTimeWeather(location: Location): Weather {
        val call = weatherService.getUltraSrtNcst(location.toQueryMap(RequestType.REALTIME))
        return try {
            call.execute().let {
                if (it.body()?.response?.header?.resultCode != "00")
                    throw Exception(it.body()?.response?.header?.resultMsg)

                it.body()!!.response.body.getRealTimeWeather(location)
            }
        } catch (e: Exception) {
            Weather(status = WeatherStaus.Fail)
        }
    }

    override suspend fun getForecastWeather(location: Location): Weather {
        val call = weatherService.getUltraSrtFcst(location.toQueryMap(RequestType.ULTRAFORECAST))
        return try {
            call.execute().let {
                if (it.body()?.response?.header?.resultCode != "00")
                    throw Exception(it.body()?.response?.header?.resultMsg)

                it.body()!!.response.body.getUltraForecastWeather(location)
            }
        } catch (e: Exception) {
            Weather(status = WeatherStaus.Fail)
        }
    }
}