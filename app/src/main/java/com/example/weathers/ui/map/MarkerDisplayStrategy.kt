package com.example.weathers.ui.map

import android.content.Context
import android.graphics.Bitmap
import com.example.weathers.data.model.Weather
import com.example.weathers.util.BitmapGenerator
import com.example.weathers.util.distanceTo
import com.google.android.gms.maps.model.Marker
import java.util.LinkedList

interface MarkerDisplayStrategy<T> {
    // googleMap 줌 레벨 -> 지도 레벨 (1, 2, 3)
    fun getMapLevel(zoomLevel: Int): Int

    // 마커 표시 간격(m)
    fun getDisplayGap(zoomLevel: Int): Int

    // 마커에 사용되는 이미지
    fun getBitmap(context: Context, weather: T): Bitmap

    // 보여주는 목록과 숨길 목록 반환
    fun getVisibleList(zoomLevel: Int, markerMap: Map<Int, Map<String, Marker>>): Pair<List<Marker>, List<Marker>>

}

class DefaultMarkerDisplayStrategy: MarkerDisplayStrategy<Weather> {
    override fun getMapLevel(zoomLevel: Int) = when (zoomLevel) {
        in 1..7 -> 1
        in 8..11 -> 2
        else -> 3
    }

    override fun getDisplayGap(zoomLevel: Int) = when (zoomLevel) {
        in 1..7 -> 90000
        8 -> 30000
        9 -> 20000
        10 -> 5000
        11 -> 3000
        12 -> 2000
        13 -> 500
        else -> 0
    }

    override fun getBitmap(context: Context, weather: Weather): Bitmap {
        return BitmapGenerator.generateBitmapWithImageAndText(
            context, weather.skyIcon,
            weather.temp,
            weather.shortAdress
        )
    }

    override fun getVisibleList(zoomLevel: Int, markerMap: Map<Int, Map<String, Marker>>): Pair<List<Marker>, List<Marker>> {
        var list: LinkedList<Marker>
        val visibleList = mutableListOf<Marker>()
        val inVisibleList = mutableListOf<Marker>()
        var m: Marker
        val displayGap = getDisplayGap(zoomLevel)
        when (getMapLevel(zoomLevel)) {
            1 -> {
                list = LinkedList(listOf(markerMap[1]!!.values, markerMap[2]!!.values, markerMap[3]!!.values).flatten())
                while (list.isNotEmpty()) {
                    m = list.poll()!!
                    visibleList.add(m)
                    val group = list.groupBy { m.position.distanceTo(it.position) < displayGap }
                    group[true]?.also { inVisibleList.addAll(it) }
                    group[false]?.also { list = LinkedList(it) }
                }
            }

            2 -> {
                list = LinkedList(listOf(markerMap[2]!!.values, markerMap[3]!!.values).flatten())
                while (list.isNotEmpty()) {
                    m = list.poll()!!
                    visibleList.add(m)
                    val group = list.groupBy { m.position.distanceTo(it.position) < displayGap }
                    group[true]?.also { inVisibleList.addAll(it) }
                    group[false]?.also { list = LinkedList(it) }
                }
            }

            3 -> {
                list = LinkedList(listOf(markerMap[3]!!.values).flatten())
                while (list.isNotEmpty()) {
                    m = list.poll()!!
                    visibleList.add(m)
                    val group = list.groupBy { m.position.distanceTo(it.position) < displayGap }
                    group[true]?.also { inVisibleList.addAll(it) }
                    group[false]?.also { list = LinkedList(it) }
                }
            }
        }

        return visibleList to inVisibleList
    }
}