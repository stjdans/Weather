package com.example.weathers.data.source.sensor

import java.lang.Math.*
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.tan

//#  단기예보 지점 좌표(X,Y)위치와 위경도 간의 전환
//
//** 아래 프로그램은 위경도 값을 직접 좌표 값으로 변환하여 사용하기 원하는 사용자를 위한  예제입니다.
//** 행정구역별 지점 좌표(X,Y) 값은 별첨 엑셀 파일에 작성되어 제공 중입니다.
//** 단기예보서비스는 남한에 대해서만 제공되며, 북한 및 국외는 제공되지 않습니다.
//** 아래의 컴파일 방법은 예시이며, 사용하는 컴파일러나 툴 등에 맞춰 컴파일하면 됩니다.
//
//○ 컴파일 방법 예시
//
//# cc 소스파일명 -lm
//
//○ 실행 방법 예시
//
//# 실행파일명 1 <X-grid> <Y-grid>
//예) # a.out 1 59 125
//출력결과)X = 59, Y = 125 --->lon.= 126.929810, lat.= 37.488201
//
//# 실행파일명 0 <경도> <위도>
//예) # a.out 0 126.929810 37.488201
//출력결과)lon.= 126.929810, lat.= 37.488201 ---> X = 59, Y = 125

//struct lamc_parameter {
//	float Re /* 사용할 지구반경 [ km ] */
//	float grid /* 격자간격 [ km ] */
//	float slat1 /* 표준위도 [degree] */
//	float slat2 /* 표준위도 [degree] */
//	float olon /* 기준점의 경도 [degree] */
//	float olat /* 기준점의 위도 [degree] */
//	float xo /* 기준점의 X좌표 [격자거리] */
//	float yo /* 기준점의 Y좌표 [격자거리] */
//	int first /* 시작여부 (0 = 시작) */
//}

class CoordinateConverter {
    private data class Lamc_Parameter(
        var Re: Float, /* 사용할 지구반경 [ km ] */
        var grid: Float, /* 격자간격 [ km ] */
        var slat1: Float, /* 표준위도 [degree] */
        var slat2: Float, /* 표준위도 [degree] */
        var olon: Float, /* 기준점의 경도 [degree] */
        var olat: Float, /* 기준점의 위도 [degree] */
        var xo: Float, /* 기준점의 X좌표 [격자거리] */
        var yo: Float, /* 기준점의 Y좌표 [격자거리] */
        var first: Int /* 시작여부 (0 = 시작) */
    )

    private data class LocationParam(
        var lon: Float = 0f,
        var lat: Float = 0f,
        var x: Float = 0f,
        var y: Float = 0f,
    )

    companion object {
        private val NX = 149 /* X축 격자점 수 */
        private val NY = 253 /* Y축 격자점 수 */

        // 단기예보 지점 좌표(X,Y)위치와 위경도 간의 전환
        // 1 : (x,y) --> (lon,lat)
        fun mapToLamproj(x: Int, y: Int): Pair<Float, Float> {
            val param = converter(arrayOf("", "1", x.toString(), y.toString()))
            return param.lon to param.lat
        }

        // 위경도와 단기예보 지점 좌표(X,Y)위치 전환
        // 0 : (lon,lat) --> (x,y)
        fun lamprojToMap(lon: Float, lat: Float): Pair<Int, Int> {
            val param = converter(arrayOf("", "0", lon.toString(), lat.toString()))
            return param.x.toInt() to param.y.toInt()
        }

        private fun converter(argv: Array<String>): LocationParam {
            val param = LocationParam()

//            if (argc != 4) {
//                println("[Usage] ${argv[0]} 1 <X-grid><Y-grid>\n")
//                println(" ${argv[0]} 0 <longitude><latitude>\n")
//                return
//            }

            if (argv[1].toInt() == 1) {
                param.x = argv[2].toFloat()
                param.y = argv[3].toFloat()

                if (param.x < 1 || param.x > NX || param.y < 1 || param.y > NY) {
                    println("X-grid range [1,$NX] / Y-grid range [1,$NY]\n")
                    return LocationParam()
                }
            } else if (argv[1].toInt() == 0) {
                param.lon = argv[2].toFloat()
                param.lat = argv[3].toFloat()
            }


            //
            // 단기예보 지도 정보
            //

            val map = Lamc_Parameter(
                Re = 6371.00877f, // 지도반경
                grid = 5.0f, // 격자간격 (km)
                slat1 = 30.0f, // 표준위도 1
                slat2 = 60.0f, // 표준위도 2
                olon = 126.0f, // 기준점 경도
                olat = 38.0f, // 기준점 위도
                xo = 210f / 5.0f, // 기준점 X좌표
                yo = 675f / 5.0f, // 기준점 Y좌표
                first = 0
            )


            //
            // 단기예보
            //
            map_conv(param, argv[1].toInt(), map)

//            if (argv[1].toInt() != 0)
//                println("X = ${param.x.toInt()}, Y = ${param.y.toInt()} --->lon.= ${param.lon}, lat.= ${param.lat}\n")
//            else
//                println("lon.= ${param.lon}, lat.= ${param.lat} ---> X = ${param.x.toInt()}, Y = ${param.y.toInt()}\n")

            return param
        }

        /*============================================================================*
        * 좌표변환
        *============================================================================*/
        private fun map_conv(param0: LocationParam, code: Int, map: Lamc_Parameter): Int {
//    var lon1 = 0f
//    var lat1 = 0f
//    var x1 = 0f
//    var y1 = 0f

            //
            // 위경도 -> (X,Y)
            //

            if (code == 0) {
                val param1 = LocationParam(
                    lon = param0.lon,
                    lat = param0.lat
                )

                lamcproj(param1, 0, map)
                param0.x = (param1.x + 1.5.toFloat()).toInt().toFloat()
                param0.y = (param1.y + 1.5.toFloat()).toInt().toFloat()
            }

            //
            // (X,Y) -> 위경도
            //

            if (code == 1) {
                val param1 = LocationParam(
                    x = param0.x - 1,
                    y = param0.y - 1
                )
                lamcproj(param1, 1, map)
                param0.lon = param1.lon
                param0.lat = param1.lat
            }
            return 0
        }

        /***************************************************************************
         *
         * [ Lambert Conformal Conic Projection ]
         *
         * olon, lat : (longitude,latitude) at earth [degree]
         * o x, y : (x,y) cordinate in map [grid]
         * o code = 0 : (lon,lat) --> (x,y)
         * 1 : (x,y) --> (lon,lat)
         *
         ***************************************************************************/

        private fun lamcproj(param: LocationParam, code: Int, map: Lamc_Parameter): Int {
//    loat *lon, *lat /* Longitude, Latitude [degree] */
//    float *x, *y /* Coordinate in Map [grid] */
//    int code /* (0) lon,lat ->x,y (1) x,y ->lon,lat */
//    struct lamc_parameter *map
//    {
            var PI = 0.0
            var DEGRAD = 0.0
            var RADDEG = 0.0

            var re = 0.0
            var olon = 0.0
            var olat = 0.0
            var sn = 0.0
            var sf = 0.0
            var ro = 0.0

            var (slat1, slat2, alon, alat) = arrayOf(0.0, 0.0, 0.0, 0.0)
            var (xn, yn, ra, theta) = arrayOf(0.0, 0.0, 0.0, 0.0)

            if (map.first == 0) {
                PI = asin(1.0) * 2.0
                DEGRAD = PI / 180.0
                RADDEG = 180.0 / PI

                re = (map.Re / map.grid).toDouble()
                slat1 = map.slat1 * DEGRAD
                slat2 = map.slat2 * DEGRAD
                olon = map.olon * DEGRAD
                olat = map.olat * DEGRAD

                sn = tan(PI * 0.25 + slat2 * 0.5) / tan(PI * 0.25 + slat1 * 0.5)
                sn = log(cos(slat1) / cos(slat2)) / log(sn)
                sf = tan(PI * 0.25 + slat1 * 0.5)
                sf = pow(sf, sn) * cos(slat1) / sn
                ro = tan(PI * 0.25 + olat * 0.5)
                ro = re * sf / pow(ro, sn)
                map.first = 1
            }

            if (code == 0) {
                ra = tan(PI * 0.25 + param.lat * DEGRAD * 0.5)
                ra = re * sf / pow(ra, sn)
                theta = param.lon * DEGRAD - olon
                if (theta > PI) theta -= 2.0 * PI
                if (theta < -PI) theta += 2.0 * PI
                theta *= sn
                param.x = (ra * sin(theta)).toFloat() + map.xo
                param.y = (ro - ra * cos(theta)).toFloat() + map.yo
            } else {
                xn = (param.x - map.xo).toDouble()
                yn = ro - param.y + map.yo
                ra = sqrt(xn * xn + yn * yn)
                if (sn < 0.0) -ra
                alat = pow((re * sf / ra), (1.0 / sn))
                alat = 2.0 * atan(alat) - PI * 0.5
                if (abs(xn) <= 0.0) {
                    theta = 0.0
                } else {
                    if (abs(yn) <= 0.0) {
                        theta = PI * 0.5
                        if (xn < 0.0) -theta
                    } else
                        theta = atan2(xn, yn)
                }
                alon = theta / sn + olon
                param.lat = (alat * RADDEG).toFloat()
                param.lon = (alon * RADDEG).toFloat()
            }

            return 0
        }

        // 위/경도 두 점 사이 거리
        private const val R = 6372.8 * 1000

        /**
         * 두 좌표의 거리를 계산한다.
         *
         * @param lat1 위도1
         * @param lon1 경도1
         * @param lat2 위도2
         * @param lon2 경도2
         * @return 두 좌표의 거리(m)
         */
        fun getDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Int {
            val dLat = toRadians(lat2 - lat1)
            val dLon = toRadians(lon2 - lon1)
            val a = sin(dLat / 2).pow(2.0) + sin(dLon / 2).pow(2.0) * cos(toRadians(lat1)) * cos(toRadians(lat2))
            val c = 2 * asin(sqrt(a))
            return (R * c).toInt()
        }
    }

}
