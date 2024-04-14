package com.example.weathers.ui.main

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.weathers.R
import com.example.weathers.data.model.Weather
import com.example.weathers.data.model.WeatherWithPosition
import com.example.weathers.ui.theme.WeathersTheme

@Composable
fun BasicWeatherCard(
    item: WeatherWithPosition,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = modifier
            .heightIn(min = 130.dp)
            .padding(10.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(25.dp)
                    .padding(horizontal = 10.dp)
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
                        .offset(x = (-40).dp)

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
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .fillMaxWidth()
                    .height(23.dp)
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
                modifier = Modifier
                    .fillMaxWidth()
                    .height(23.dp)
                    .wrapContentHeight(Alignment.CenterVertically)
            )

            content()
        }
    }
}

@Preview
@Composable
private fun BasicWeatherCardPreview() {
    WeathersTheme {
        BasicWeatherCard(item = WeatherWithPosition(0, weather = Weather()))
    }
}

@Composable
fun LoadingWeatherCard(
    modifier: Modifier = Modifier,
) {

    val transition = rememberInfiniteTransition()
    val alpha by transition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(500),
            repeatMode = RepeatMode.Reverse
        ), label = "alpha"
    )

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier
            .heightIn(min = 120.dp)
            .padding(10.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
        ) {

            val animateBackgroundModifier = Modifier
                .graphicsLayer {
                    this.alpha = alpha
                }
                .background(Color.LightGray)

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .padding(horizontal = 5.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(6.0f)
                        .then(animateBackgroundModifier)
                )

                Spacer(modifier = Modifier.weight(2.0f))
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(2.0f)
                        .then(animateBackgroundModifier)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(60.dp)
                    .padding(5.dp)
            ) {

                Spacer(modifier = Modifier.weight(1.0f))

                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1.0f)
                        .then(animateBackgroundModifier)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Box(
                    modifier = Modifier
                        .height(30.dp)
                        .weight(1.0f)
                        .then(animateBackgroundModifier)

                )

                Spacer(modifier = Modifier.weight(1.0f))

            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp)
                    .height(17.dp)
            ) {
//                Spacer(modifier = Modifier.weight(1.0f))
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1.0f)
                        .then(animateBackgroundModifier)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1.0f)
                        .then(animateBackgroundModifier)
                )
//                Spacer(modifier = Modifier.weight(1.0f))
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp)
                    .height(17.dp)
                    .then(animateBackgroundModifier)
            )
        }
    }
}


@Preview
@Composable
private fun LoadingWeatherCardPreview() {
    WeathersTheme {
        LoadingWeatherCard()
    }
}