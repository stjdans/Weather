package com.example.weathers.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng

@Entity(tableName = "location")
data class Location(
    @PrimaryKey
    val code: String = "", //행정구역코드
    val adress1: String? = null, // 시, 도
    val adress2: String?= null, // 시, 구, 군..
    val adress3: String?= null, // 동, 면..
    val dx: Int = 0,
    val dy: Int = 0,
    val longitude1: Int = 0, //경도(시)
    val longitude2: Int = 0, //경도(분)
    val longitude3: Float = 0f, //경도(초)
    val latitude1: Int = 0, //위도(시)
    val latitude2: Int = 0, //위도(분)
    val latitude3: Float = 0f, //위도(초)
    val longitude4_100: Double = 0.0, //경도(초/100)
    val latitude4_100: Double = 0.0, //위도(초/100)
    val level: Int = 0 // 1: [시, 도] 2: [구, 시] 3: [동, 면, 리, ...]
) {
    val adress: String
        get() = "${adress1}${if (adress2 != null) " $adress2" else ""}${if (adress3 != null) " $adress3" else ""}"

    val latLng: LatLng
        get() = LatLng(latitude4_100, longitude4_100)
    val toLatLng: String
        get() = "$adress (${latitude4_100}, $longitude4_100)"

    // 두 지점 거리(m)
    fun distanceTo(location: Location) = distanceTo(location.latLng)

    fun distanceTo(latLng: LatLng): Int {
        val result = FloatArray(1)
        android.location.Location.distanceBetween(latitude4_100, longitude4_100, latLng.latitude, latLng.longitude, result)
        return result[0].toInt()
    }
}
