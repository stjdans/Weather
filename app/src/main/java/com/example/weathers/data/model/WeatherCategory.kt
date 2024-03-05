package com.example.weathers.data.model

enum class WeatherCategory(val desc: String, val unit: String, val desc2: String = "") {

    //단기예보
    //short forecast
        POP("강수확률", "%"),
        PTY("강수형태", ""),
        PCP("1시간 강수량", "mm"),
        REH("습도", "%"),
        SNO("1시간 신적설", "1cm"),
        SKY("하늘상태", ""),
        TMP("1시간 기온", "°"),
        TMN("일 최저기온", "°"),
        TMX("일 최고기온", "°"),
        UUU("풍속(동서성분)", "m/s"),
        VVV("풍속(남북성분)", "m/s"),
        WAV("파고", "M"),
        VEC("풍향", "deg"),
        WSD("풍속", "m/s"),

    //초단기 실황
    //very short situation
        T1H("기온", "°"),
        RN1("1시간 강수량", "mm", "강수량"),
//        UUU("동서바람성분", "m/s"),
//        VVV("남북바람성분", "m/s"),
//        REH("습도", "%"),
//        PTY("강수형태", ""),
//        VEC("풍향", "deg"),
//        WSD("풍속", "m/s"),

    //초단기 예보
    //very short forecast
//    enum class VSF(desc: String, unit: String) {
//        T1H("기온", "C"),
//        RN1("1시간 강수량", "mm"),
//        SKY("하늘상태", ""),
//        UUU("동서바람성분", "m/s"),
//        VVV("남북바람성분", "m/s"),
//        REH("습도", "%"),
//        PTY("강수형태", ""),
        LGT("낙뢰", ""),
//        VEC("풍향", "deg"),
//        WSD("풍속", "m/s"),
//    }
}