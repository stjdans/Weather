package com.example.weathers.ui.forecast

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weathers.R
import com.example.weathers.data.model.EmptyDescriptor
import com.example.weathers.data.model.Weather
import com.example.weathers.data.model.WeatherWithPosition
import com.example.weathers.data.toListWithPosition
import com.example.weathers.data.toTimeBaseList
import com.example.weathers.ui.main.BasicSwipeToDismiss
import com.example.weathers.ui.main.BasicWeatherCard
import com.example.weathers.ui.theme.WeathersTheme

@Composable
fun ForecastScreen(
    viewModel: ForecastViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ForecastList(
        list = uiState.weatherItems.toListWithPosition(),
        onDelete = { weatherWithPosition -> viewModel.deleteUserLocatin(weatherWithPosition.weather.code) }
    )
}

@Composable
private fun ForecastList(
    list: List<WeatherWithPosition>,
    onDelete: (WeatherWithPosition) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(list, key = { it.weather.code }) { item ->
            if (item.position == 0) {
                ForecastItem(item)
            } else {
                BasicSwipeToDismiss(
                    target = item,
                    onDelete = onDelete,
                    background = { ForecastItem(item, modifier = Modifier.alpha(0.5f)) },
                    dismissContent = { ForecastItem(item) }
                )
            }
        }
    }
}

@Composable
private fun ForecastItem(
    item: WeatherWithPosition,
    modifier: Modifier = Modifier
) {
    BasicWeatherCard(item = item, modifier = modifier) {
        LazyRow(modifier = Modifier.padding(top = 10.dp)) {
            item {
                VerticalItem(item = Weather(descriptor = EmptyDescriptor()), modifier.width(70.dp))
            }
            items(item.weather.toTimeBaseList(), key = { it.baseDate + it.baseTime }) { item ->
                VerticalItem(item = item, modifier.width(50.dp))
            }
        }
    }
}

@Composable
private fun VerticalItem(
    item: Weather,
    modifier: Modifier = Modifier
) {
    VerticalItem(
        date = item.baseTime,
        icon = item.skyIcon,
        temp = item.temp,
        rainfallPer = item.rainfallPer,
        rainfall = item.rainfall,
        windSpeed = item.windSpeed,
        humidity = item.humidity,
        modifier = modifier
    )
}

@Composable
private fun VerticalItem(
    date: String,
    @DrawableRes icon: Int,
    temp: String,
    rainfallPer: String,
    rainfall: String,
    windSpeed: String,
    humidity: String,
    modifier: Modifier = Modifier
) {
    val textStyle = TextStyle.Default.copy(fontSize = 10.sp)
    val defaultModifier = Modifier.height(20.dp)

    Row(
        modifier = modifier.height(IntrinsicSize.Min)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1.0f)
        ) {
            Text(text = date, style = textStyle, modifier = Modifier.height(20.dp))
            if (icon == 0) {
                Spacer(modifier = Modifier.height(40.dp))
            } else {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    modifier = Modifier
                        .height(40.dp)
                        .padding(vertical = 5.dp)
                )
            }
            Text(text = temp, style = textStyle, modifier = defaultModifier)
            Text(text = rainfallPer, style = textStyle, modifier = defaultModifier)
            Text(text = rainfall, style = textStyle, modifier = defaultModifier)
            Text(text = windSpeed, style = textStyle, modifier = defaultModifier)
            Text(text = humidity, style = textStyle, modifier = defaultModifier)
        }

        Divider(modifier = Modifier.fillMaxHeight().width(DividerDefaults.Thickness))
    }
}

@Preview
@Composable
private fun VerticalItemPreview() {
    WeathersTheme {
        VerticalItem(
            modifier = Modifier.width(50.dp),
            date = "2100", icon = R.drawable.cloudy, temp = "16",
            rainfallPer = "-", rainfall = "0",
            windSpeed = "0", humidity = "25"
        )
    }
}

//@Preview
@Composable
private fun ForecastScreenPreview() {
    WeathersTheme {
        ForecastScreen()
    }
}