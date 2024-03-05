package com.example.weathers.data.source.network

import com.example.weathers.data.source.network.model.ResponseWeather
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface WeatherService {

    //초단기실황조회
    //http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst
    @GET("1360000/VilageFcstInfoService_2.0/getUltraSrtNcst")
    fun getUltraSrtNcst(@QueryMap queryMap: Map<String, String>): Call<ResponseWeather>

    //초단기예보조회
    //http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst
    @GET("1360000/VilageFcstInfoService_2.0/getUltraSrtFcst")
    fun getUltraSrtFcst(@QueryMap queryMap: Map<String, String>): Call<ResponseWeather>

    //단기예보조회
    //http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst
    @GET("1360000/VilageFcstInfoService_2.0/getVilageFcst")
    fun getVilageFcst(@QueryMap queryMap: Map<String, String>): Call<ResponseBody>

    //예보버전조회
    //http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getFcstVersion
    @GET("1360000/VilageFcstInfoService_2.0/getFcstVersion")
    fun getFcstVersion(
        @Query("serviceKey") serviceKey: String = "vW7WT997qA03c912hkomYrcWEzkOMMbQAjsItVQ+b/X3NYIgHpYt7rpBkhZstEyL79h9Ki5dHosxw9at5sFIOA==",
        @Query("numOfRows") numOfRows: String = "1000",
        @Query("pageNo") pageNo: String = "1",
        @Query("dataType") dataType: String = "JSON",
        @Query("ftype") ftype: String = "ODAM",
        @Query("basedatetime") basedatetime: String = "202402050800"
    ): Call<ResponseBody>
}