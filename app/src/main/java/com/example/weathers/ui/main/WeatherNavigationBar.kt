package com.example.weathers.ui.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.weathers.WeatherDestination
import com.example.weathers.ui.theme.WeathersTheme
import com.example.weathers.weatherTabScreen

@Composable
fun WeatherNavigationBar(
    allTabs: List<WeatherDestination>,
    currentScreen: WeatherDestination,
    onTabSelected: (WeatherDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
    ) {
        allTabs.forEach { destination ->
            val isCurrentScreen = currentScreen == destination
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.surfaceColorAtElevation(LocalAbsoluteTonalElevation.current)
                ),
                selected = isCurrentScreen,
                onClick = { onTabSelected(destination) },
                icon = {
                    if (isCurrentScreen) {
                        SpringIcon(icon = destination.icon)
                    } else {
                        Icon(imageVector = destination.unSelectedIcon, contentDescription = null)
                    }
                },
                label = { Text(text = stringResource(id = destination.label)) }
            )
        }
    }
}


@Composable
private fun SpringIcon(
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    val scale = remember { Animatable(0.5f) }

    LaunchedEffect(key1 = Unit) {
        scale.animateTo(1.0f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
    }

    Icon(
        imageVector = icon,
        contentDescription = "",
        modifier = modifier.graphicsLayer {
            this.scaleX = scale.value
            this.scaleY = scale.value
        }
    )
}

@Preview
@Composable
private fun WeatherNavigationBarPreview() {
    WeathersTheme {
        WeatherNavigationBar(
            allTabs = weatherTabScreen,
            currentScreen = weatherTabScreen[1],
            onTabSelected = {}
        )
    }
}