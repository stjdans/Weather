package com.example.weathers.data.model

import com.example.weathers.R

class EmptyDescriptor: WeatherDescriptor {
    override fun getTemp(categories: Map<String, Map<String, String>>): String {
        return ""
    }

    override fun getSky(categories: Map<String, Map<String, String>>): String {
        return ""
    }

    override fun getPty(categories: Map<String, Map<String, String>>): String {
        val category = WeatherCategory.PTY
        return "${category.desc}(${category.unit})"
    }

    override fun getSkyIcon(categories: Map<String, Map<String, String>>): Int {
        return 0;
    }

    override fun getRainfall(categories: Map<String, Map<String, String>>): String {
        val category = WeatherCategory.RN1
        return "${category.desc2}(${category.unit})"
    }

    override fun getHumidity(categories: Map<String, Map<String, String>>): String {
        val category = WeatherCategory.REH
        return "${category.desc}(${category.unit})"
    }

    override fun getWindSpeed(categories: Map<String, Map<String, String>>): String {
        val category = WeatherCategory.WSD
        return "${category.desc}(${category.unit})"
    }

    override fun getRainfallPer(categories: Map<String, Map<String, String>>): String {
        val category = WeatherCategory.POP
        return "${category.desc}(${category.unit})"
    }
}
