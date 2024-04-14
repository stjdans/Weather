package com.example.weathers.ui.realtime

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weathers.data.model.Weather
import com.example.weathers.data.model.WeatherStaus
import com.example.weathers.data.model.WeatherWithPosition
import com.example.weathers.data.toListWithPosition
import com.example.weathers.ui.main.BasicSwipeToDismiss
import com.example.weathers.ui.main.BasicWeatherCard
import com.example.weathers.ui.main.LoadingWeatherCard
import com.example.weathers.ui.main.SearchBar
import com.example.weathers.ui.theme.WeathersTheme

@Composable
fun RealTimeScreen(
    viewModel: RealTimeViewModel = hiltViewModel(),
    onSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
    ) {
        SearchBar(
            onValueChange = { },
            onClose = { onSearch() },
        )
        RealTimeList(
            list = uiState.weatherItems.toListWithPosition(),
            onDelete = { weatherWithPosition ->
                viewModel.deleteUserLocatin(weatherWithPosition.weather.code)
            }
        )
    }
}

@Composable
private fun RealTimeList(
    list: List<WeatherWithPosition> = remember { sampleWeathers },
    onDelete: (WeatherWithPosition) -> Unit,
    modifier: Modifier = Modifier
) {

    LazyColumn(
        state = rememberLazyListState(),
        modifier = modifier
    ) {
        items(
            items = list,
            key = { it.weather.adress }
        ) { item ->
            if (item.position == 0) {
                RealTimeCard(item)
            } else {
                BasicSwipeToDismiss(
                    target = item,
                    onDelete = onDelete,
                    background = { RealTimeCard(item = item, modifier = Modifier.alpha(0.3f)) },
                    dismissContent = { RealTimeCard(item) }
                )
            }
        }
    }
}

@Composable
private fun RealTimeCard(
    item: WeatherWithPosition,
    modifier: Modifier = Modifier
) {
    if (item.weather.status == WeatherStaus.Fail)
        LoadingWeatherCard()
    else
        BasicWeatherCard(item, modifier)
}

@Preview
@Composable
private fun RealTimeColumnPreview() {
    WeathersTheme {
        RealTimeList(onDelete = {})
    }
}

private val sampleWeathers = listOf(
    WeatherWithPosition(0, Weather(adress = "서울시 송파구 송파동", code = "0")),
    WeatherWithPosition(1, Weather(adress = "서울시 송파구 방이동", code = "1")),
)