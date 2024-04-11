package com.example.weathers

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.ui.graphics.vector.ImageVector

interface WeatherDestination {
    val icon: ImageVector
    val unSelectedIcon: ImageVector
    val label: Int
    val route: String
}

//object Splash: WeatherDestination {
//    override val icon: ImageVector = Icons.Default.Home
//    override val route: String = "splash"
//}

object RealTime: WeatherDestination {
    override val icon: ImageVector = Icons.Default.Home
    override val unSelectedIcon: ImageVector = Icons.Outlined.Home
    override val label: Int = R.string.title_realtime
    override val route: String = "realtime"
}

object Forecast: WeatherDestination {
    override val icon: ImageVector = Icons.Default.Info
    override val unSelectedIcon: ImageVector = Icons.Outlined.Info
    override val label: Int = R.string.title_forecast
    override val route: String = "forecast"
}

object Map: WeatherDestination {
    override val icon: ImageVector = Icons.Default.LocationOn
    override val unSelectedIcon: ImageVector = Icons.Outlined.LocationOn
    override val label: Int = R.string.title_map
    override val route: String = "map"
}

val weatherTabScreen = listOf(RealTime, Forecast, Map)