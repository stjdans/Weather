package com.example.weathers.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.weathers.Forecast
import com.example.weathers.MainUiState
import com.example.weathers.Map
import com.example.weathers.R
import com.example.weathers.RealTime
import com.example.weathers.Search
import com.example.weathers.ui.forecast.ForecastScreen
import com.example.weathers.ui.map.MapScreen
import com.example.weathers.ui.realtime.RealTimeScreen
import com.example.weathers.ui.search.SearchScreen
import com.example.weathers.ui.theme.WeathersTheme
import com.example.weathers.weatherTabScreen

val sampleUiState = MainUiState.Home()

@Composable
fun MainScreen(
    state: MainUiState.Home = sampleUiState,
    onPermissionRequestClick: () -> Unit = {},
    onDeviceSettingClick: () -> Unit = {}
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val destination = backStackEntry?.destination
    val currentScreen = weatherTabScreen.find { destination?.route == it.route } ?: RealTime

    Scaffold(
        bottomBar = {
            WeatherNavigationBar(
                allTabs = weatherTabScreen,
                currentScreen = currentScreen,
                onTabSelected = { destination -> navController.navigateSingleTop(route = destination.route) }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            WeatherNaviHost(navController = navController)

            AnimatedVisibility(
                visible = !state.permission || !state.gps,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                InfoMessage(state, onPermissionRequestClick, onDeviceSettingClick)
            }
        }
    }
}

@Composable
private fun WeatherNaviHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = RealTime.route,
        modifier = modifier
    ) {
//        composable(route = Splash.route) {
//            SplashScreen()
//        }

        composable(route = RealTime.route) {
            RealTimeScreen(onSearch = { navController.navigateSingleTop(Search.route) })
        }

        composable(route = Search.route) {
            SearchScreen(onClose = { navController.popBackStack() })
        }

        composable(route = Forecast.route) {
            ForecastScreen()
        }

        composable(route = Map.route) {
            MapScreen()
        }
    }
}

@Composable
private fun InfoMessage(
    state: MainUiState.Home,
    onPermissionRequestClick: () -> Unit,
    onDeviceSettingClick: () -> Unit
) {
    var message = ""
    var onClick: () -> Unit = {}

    when {
        !state.permission -> {
            message = stringResource(id = R.string.error_location_permission)
            onClick = onPermissionRequestClick
        }

        !state.gps -> {
            message = stringResource(id = R.string.error_gps)
            onClick = onDeviceSettingClick
        }
    }

    InfoMessageButton(
        message = message,
        onClick = onClick,
    )
}

@Composable
private fun InfoMessageButton(
    message: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        ElevatedButton(
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            ),
            modifier = modifier
                .padding(5.dp)
                .fillMaxWidth()
                .align(Alignment.BottomStart),
            onClick = onClick
        ) {
            Text(text = message)
        }
    }

}

@Preview
@Composable
private fun InfoMessageButtonPreview() {
    WeathersTheme {
        InfoMessageButton(message = stringResource(id = R.string.error_location_permission), onClick = { /*TODO*/ })
    }
}

@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    WeathersTheme {
        MainScreen()
    }
}

fun NavController.navigateSingleTop(route: String) = this.navigate(route) {
    popUpTo(this@navigateSingleTop.graph.findStartDestination().id) {
        saveState = true
    }
    launchSingleTop = true
    restoreState = true
}
