package com.example.weathers.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.weathers.R
import com.example.weathers.data.model.WeatherWithPosition

@Composable
fun BasicWeatherCard(
    item: WeatherWithPosition,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        modifier = modifier.padding(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = item.weather.adress,
                    style = TextStyle.Default.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Start,
                    modifier = Modifier.weight(8.0f)
                )

                if (item.position == 0) {
                    Text(
                        text = stringResource(id = R.string.current_position),
                        style = TextStyle.Default.copy(
                            color = Color.Blue,
                            fontWeight = FontWeight.Bold
                        ),
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(2.0f)
                    )
                }
            }

            ConstraintLayout(
                modifier = Modifier.fillMaxWidth()
            ) {

                val (image, temp, desc) = createRefs()
                val centerGuideline = createGuidelineFromStart(0.5f)

                Image(
                    painter = painterResource(id = item.weather.skyIcon),
                    contentDescription = null,
                    modifier = Modifier
                        .constrainAs(image) { start.linkTo(temp.start) }
                        .size(70.dp)
                        .padding(5.dp)
                        .offset(x = (-30).dp)

                )
                Text(
                    text = item.weather.temp,
                    style = TextStyle.Default.copy(
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.constrainAs(temp) {
                        end.linkTo(centerGuideline)
                        centerVerticallyTo(parent)
                    }
                )
                Text(
                    text = item.weather.sky,
                    style = TextStyle.Default.copy(
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.constrainAs(desc) {
                        start.linkTo(centerGuideline)
                        centerVerticallyTo(parent)
                    }
                )
            }

            val textStyle = TextStyle.Default.copy(fontSize = 12.sp)
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = modifier.fillMaxWidth()
            ) {
                Text(
                    text = item.weather.humidity,
                    style = textStyle,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .weight(1.0f)
                        .padding(horizontal = 10.dp)
                )
                Text(
                    text = item.weather.windSpeed,
                    style = textStyle,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .weight(1.0f)
                        .padding(horizontal = 10.dp)
                )
            }

            Text(
                text = item.weather.date,
                style = textStyle,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            content()
        }
    }
}