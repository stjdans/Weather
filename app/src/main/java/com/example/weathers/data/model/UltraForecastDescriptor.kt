package com.example.weathers.data.model

import com.example.weathers.R

class UltraForecastDescriptor: WeatherDescriptor {
    override fun getTemp(categories: Map<String, Map<String, String>>): String {
        if (categories.keys.isEmpty()) return ""
        val temp = categories[categories.keys.first()]?.get(WeatherCategory.T1H.name) ?: return ""
//        return "$temp${WeatherCategory.T1H.unit}"
        return "$temp${WeatherCategory.T1H.unit}"
    }

    override fun getSky(categories: Map<String, Map<String, String>>): String {
        if (categories.keys.isEmpty()) return ""
        val sky = categories[categories.keys.first()]?.get(WeatherCategory.SKY.name) ?: return ""
        return when (sky.toInt()) {
            1 -> "맑음"
            3 -> "구름많음"
            4 -> "흐림"
            else -> getPty(categories)
        }
    }

    override fun getPty(categories: Map<String, Map<String, String>>): String {
        if (categories.keys.isEmpty()) return ""
        val pty = categories[categories.keys.first()]?.get(WeatherCategory.PTY.name) ?: return ""
        return when (pty.toInt()) {
            1 -> "비"
            2 -> "비/눈"
            3 -> "눈"
            4 -> "소나기"
            else -> "없음"
        }
    }

    override fun getSkyIcon(categories: Map<String, Map<String, String>>): Int {
        return return when (getSky(categories)) {
            "비" -> R.drawable.rainy
            "비/눈" -> R.drawable.rainy
            "눈" -> R.drawable.snow
            "소나기" -> R.drawable.cloudy_rain
            "구름많음" -> R.drawable.cloudy
            "흐림" -> R.drawable.cloud
            else -> R.drawable.sun
        }
    }

    override fun getRainfall(categories: Map<String, Map<String, String>>): String {
        if (categories.keys.isEmpty()) return ""
        val rn1 = try {
            categories[categories.keys.first()]?.get(WeatherCategory.RN1.name)?.toFloat() ?: return ""
        } catch (e: Exception) {
            return "0"
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
        return "$reh"
    }

    override fun getWindSpeed(categories: Map<String, Map<String, String>>): String {
        if (categories.keys.isEmpty()) return ""
        val wsd = categories[categories.keys.first()]?.get(WeatherCategory.WSD.name) ?: return ""
        return "$wsd"
    }

    override fun getRainfallPer(categories: Map<String, Map<String, String>>): String {
        if (categories.keys.isEmpty()) return "-"
        val pop = categories[categories.keys.first()]?.get(WeatherCategory.POP.name) ?: return "-"
        return "$pop"
    }
}
