package com.example.weathers.ui.main

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weathers.R
import com.example.weathers.ui.theme.WeathersTheme

@Composable
fun SearchBar(
    enable: Boolean = false,
    onValueChange: (String) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    var query by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val animatePadding by animateDpAsState(targetValue = if (enable) 0.dp else 10.dp)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(TextFieldDefaults.MinHeight + 20.dp)
            .padding(horizontal = animatePadding, vertical = 10.dp)
            .clip(if (enable) RectangleShape else CircleShape)
    ) {
        val focusModifier = if (enable) {
            Modifier.focusRequester(focusRequester)
        } else {
            Modifier.pointerInput(Unit) {
                awaitPointerEventScope {
                    onClose()
                    true
                }
            }
        }

        TextField(
            enabled = enable,
            modifier = focusModifier,
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "search_icon") },
            placeholder = { Text(text = stringResource(id = R.string.searchbar_hint)) },
            trailingIcon = {

                Row {
                    if (query.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                query = ""
                                onValueChange(query)
                            }
                        ) { Icon(imageVector = Icons.Default.Delete, contentDescription = null) }
                    }

                    if (enable) {
                        IconButton(
                            onClick = {
                                onClose()
                            }
                        ) { Icon(imageVector = Icons.Default.Close, contentDescription = null) }
                    }
                }
            },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            value = query,
            onValueChange = { s ->
                query = s
                onValueChange(query)
            },
            maxLines = 1
        )
    }

    DisposableEffect(key1 = Unit) {
        if (enable) focusRequester.requestFocus()
        onDispose {}
    }
}

@Preview
@Composable
private fun TopSearchBarPreview() {
    WeathersTheme {
        SearchBar(
            onValueChange = {},
            onClose = {}
        )
    }
}