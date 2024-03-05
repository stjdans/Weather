package com.example.weathers.data.model

import com.example.weathers.R

class RealTimeDescriptor: WeatherDescriptor {

    override fun getTemp(categories: Map<String, Map<String, String>>): String {
        if (categories.keys.isEmpty()) return ""
        val temp = categories[categories.keys.first()]?.get(WeatherCategory.T1H.name) ?: return ""
        return "$temp${WeatherCategory.T1H.unit}"
    }

    override fun getSky(categories: Map<String, Map<String, String>>): String {
        val pty = getPty(categories)
        return if (pty == "없음") "맑음" else pty
    }

    override fun getPty(categories: Map<String, Map<String, String>>): String {
        if (categories.keys.isEmpty()) return ""
        val pty = categories[categories.keys.first()]?.get(WeatherCategory.PTY.name) ?: return ""
        return when (pty.toInt()) {
            1 -> "비"
            2 -> "비/눈"
            3 -> "눈"
            5 -> "빗방울"
            6 -> "빗방울/눈날림"
            7 -> "눈날림"
            else -> "없음"
        }
    }

    // SKY 데이터가 없으므로 PTY 를 이용 임의 조합
    override fun getSkyIcon(categories: Map<String, Map<String, String>>): Int {
        return when (getSky(categories)) {
            "비" -> R.drawable.rainy
            "비/눈" -> R.drawable.rainy
            "빗방울" -> R.drawable.cloudy
            "빗방울/눈날림" -> R.drawable.cloudy_rain
            "눈" -> R.drawable.snow
            "눈날림" -> R.drawable.snow
            else -> R.drawable.sun
        }
    }

    override fun getRainfall(categories: Map<String, Map<String, String>>): String {
        if (categories.keys.isEmpty()) return ""
        val rn1 = try {
            categories[categories.keys.first()]?.get(WeatherCategory.RN1.name)?.toFloat() ?: return ""
        } catch (e: Exception) {
            return "-"
        }
        return when {
            rn1 < 1f -> "1mm 미만"
            1f <= rn1 && rn1 < 30f -> "${rn1}mm"
            30f <= rn1 && rn1 < 50f -> "30 ~ 50mm"
            else -> "50mm 이상"
        }
    }

    override fun getHumidity(categories: Map<String, Map<String, String>>): String {
        if (categories.keys.isEmpty()) return ""
        val reh = categories[categories.keys.first()]?.get(WeatherCategory.REH.name) ?: return ""
        return "습도: $reh${WeatherCategory.REH.unit}"
    }

    override fun getWindSpeed(categories: Map<String, Map<String, String>>): String {
        if (categories.keys.isEmpty()) return ""
        val wsd = categories[categories.keys.first()]?.get(WeatherCategory.WSD.name) ?: return ""
        return "풍속: $wsd${WeatherCategory.WSD.unit}"
    }

    override fun getRainfallPer(categories: Map<String, Map<String, String>>): String {
        TODO("Not yet implemented")
    }
}
