package com.example.weathers.ui.realtime

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weathers.data.model.Weather
import com.example.weathers.data.model.WeatherWithPosition
import com.example.weathers.data.source.local.Location
import com.example.weathers.data.toListWithPosition
import com.example.weathers.ui.main.BasicSwipeToDismiss
import com.example.weathers.ui.main.BasicWeatherCard
import com.example.weathers.ui.theme.WeathersTheme

@Composable
fun RealTimeScreen(
    viewModel: RealTimeViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var searching by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    println("update = $uiState")
    Column(
        modifier = modifier
    ) {
        SearchBar(
            onValueChange = viewModel::search,
            onFocused = { searching = it },
            onClose = { focusManager.clearFocus(true) }
        )
        if (searching) {
            SearchList(
                list = uiState.searchItems,
                onUpdateUserLocation = { location ->
                    viewModel.setUserLocatin(location.code)
                    focusManager.clearFocus(true)
                }
            )
        } else {
            RealTimeList(
                list = uiState.weatherItems.toListWithPosition(),
                onDelete = { weatherWithPosition ->
                    viewModel.deleteUserLocatin(weatherWithPosition.weather.code)
                }
            )
        }
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
    BasicWeatherCard(item, modifier)
}

@Composable
fun SearchList(
    list: List<Location> = smapleLocations,
    onUpdateUserLocation: (Location) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.animateContentSize()
    ) {
        items(list, key = { it.code }) { location ->
            SearchItem(
                location = location,
                onUpdateUserLocation = onUpdateUserLocation
            )
        }
    }
}

@Composable
private fun SearchItem(
    location: Location,
    onUpdateUserLocation: (Location) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(50.dp)
            .clickable { onUpdateUserLocation(location) }
    ) {
        Text(
            text = location.adress,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
                .align(Alignment.Center)
        )
    }
}

@Preview
@Composable
private fun RealTimeColumnPreview() {
    WeathersTheme {
        RealTimeList(onDelete = {})
    }
}

@Preview
@Composable
fun SearchListPreview() {
    WeathersTheme {
        SearchList(onUpdateUserLocation = {})
    }
}

private val sampleWeathers = listOf(
    WeatherWithPosition(0, Weather(adress = "서울시 송파구 송파동", code = "0")),
    WeatherWithPosition(1, Weather(adress = "서울시 송파구 방이동", code = "1")),
)

private val smapleLocations = listOf(
    Location(code = "0", adress1 = "서울시"),
    Location(code = "1", adress1 = "부산시")
)