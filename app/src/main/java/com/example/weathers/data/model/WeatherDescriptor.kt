package com.example.weathers.data.model

interface WeatherDescriptor {
    //기온
    fun getTemp(categories: Map<String, Map<String, String>>): String
    // 하늘 상태
    fun getSky(categories: Map<String, Map<String, String>>): String
    // 강수 형태
    fun getPty(categories: Map<String, Map<String, String>>): String
    // 하늘 상태 아이콘
    fun getSkyIcon(categories: Map<String, Map<String, String>>): Int
    // 강수량
    fun getRainfall(categories: Map<String, Map<String, String>>): String
    fun getRainfallPer(categories: Map<String, Map<String, String>>): String

    // 습도
    fun getHumidity(categories: Map<String, Map<String, String>>): String
    // 풍속
    fun getWindSpeed(categories: Map<String, Map<String, String>>): String

}


