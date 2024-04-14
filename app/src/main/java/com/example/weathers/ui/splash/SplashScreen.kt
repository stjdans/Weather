package com.example.weathers.ui.splash

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.weathers.R
import com.example.weathers.ui.theme.WeathersTheme
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.background(Color.White).fillMaxSize()
    ) {
        SplashContent(onSplashComplete = onSplashComplete)
    }
}

@Composable
fun SplashContent(
    onSplashComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(key1 = Unit) {
        delay(SplashDefaults.AnimationTimeMillis)
        onSplashComplete()
    }

    ConstraintLayout(modifier = modifier.wrapContentSize()) {
        val (center, left, right) = createRefs()

        RotateSplashImage(
            drawableRes = R.drawable.sun,
            delayMills = 300,
            modifier = Modifier
                .size(100.dp)
                .constrainAs(center) {}
        )

        TranslateSplashImage(
            drawableRes = R.drawable.cloud,
            startLeft = true,
            modifier = Modifier
                .size(80.dp)
                .constrainAs(left) {
                    top.linkTo(center.top, margin = 40.dp)
                    start.linkTo(center.start, margin = (-20).dp)
                }
        )

        TranslateSplashImage(
            drawableRes = R.drawable.snow,
            startLeft = false,
            modifier = Modifier
                .size(80.dp)
                .constrainAs(right) {
                    top.linkTo(center.top, margin = 40.dp)
                    start.linkTo(center.end, margin = (-60).dp)
                }
        )
    }
}

@Composable
fun RotateSplashImage(
    @DrawableRes drawableRes: Int,
    delayMills: Int = 0,
    modifier: Modifier = Modifier
) {
    val animateAlpha = remember { Animatable(0.0f) }
    val animateScale = remember { Animatable(0.0f) }
    val infiniteTransition = rememberInfiniteTransition(label = "Transition")
    val animateRotate by infiniteTransition.animateFloat(
        initialValue = 0.0f,
        targetValue = 360.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing)
        ),
        label = "infiniteRotate"
    )

    LaunchedEffect(key1 = Unit) {
        animateAlpha.animateTo(1.0f, animationSpec = tween(300, delayMills))
        animateScale.animateTo(1.0f, animationSpec = tween(500, delayMills))
    }

    Image(
        modifier = modifier
            .graphicsLayer {
                this.alpha = animateAlpha.value
                this.scaleX = animateScale.value
                this.scaleY = animateScale.value
                this.rotationZ = animateRotate
            },
        painter = painterResource(id = drawableRes),
        contentDescription = "sun"
    )
}

@Composable
fun TranslateSplashImage(
    @DrawableRes drawableRes: Int,
    delayMills: Int = 0,
    startLeft: Boolean = true,
    modifier: Modifier = Modifier
) {
    val animateAlpha = remember { Animatable(0.0f) }
    val animateScale = remember { Animatable(0.0f) }
    val infiniteTransition = rememberInfiniteTransition(label = "Transition")
    val animateTranslation by infiniteTransition.animateFloat(
        initialValue = if (startLeft) -10f else 10.0f,
        targetValue = if (startLeft) 10.0f else -10.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "infiniteTranslation"
    )

    LaunchedEffect(key1 = Unit) {
        animateAlpha.animateTo(1.0f, animationSpec = tween(300, delayMills))
        animateScale.animateTo(1.0f, animationSpec = tween(500, delayMills))
    }

    Image(
        modifier = modifier
            .graphicsLayer {
                this.alpha = animateAlpha.value
                this.scaleX = animateScale.value
                this.scaleY = animateScale.value
                this.translationX = animateTranslation
            },
        painter = painterResource(id = drawableRes),
        contentDescription = "sun"
    )
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    WeathersTheme {
        SplashScreen(onSplashComplete = {})
    }
}