package com.example.weathers.ui.search

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weathers.data.source.local.Location
import com.example.weathers.ui.main.SearchBar
import com.example.weathers.ui.realtime.RealTimeViewModel
import com.example.weathers.ui.theme.WeathersTheme

@Composable
fun SearchScreen(
    viewModel: RealTimeViewModel = hiltViewModel(),
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    var init by remember {
        mutableStateOf(false)
    }

    BackHandler() {
        focusManager.clearFocus(true)
        onClose()
    }

    Column(
        modifier = modifier
    ) {
        SearchBar(
            enable = true,
            onValueChange = viewModel::search,
            onClose = {
                focusManager.clearFocus(true)
                onClose()
            }
        )
        if (init) {
            SearchList(
                list = uiState.searchItems,
                onUpdateUserLocation = { location ->
                    viewModel.setUserLocatin(location.code)
                    focusManager.clearFocus(true)
                    onClose()
                }
            )
        }
    }

    DisposableEffect(key1 = Unit) {
        init = true
        onDispose {  }
    }
}

@Composable
fun SearchList(
    list: List<Location> = smapleLocations,
    onUpdateUserLocation: (Location) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
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
fun SearchListPreview() {
    WeathersTheme {
        SearchList(onUpdateUserLocation = {})
    }
}

private val smapleLocations = listOf(
    Location(code = "0", adress1 = "서울시"),
    Location(code = "1", adress1 = "부산시")
)