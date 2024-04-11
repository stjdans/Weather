package com.example.weathers.ui.realtime

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weathers.R
import com.example.weathers.ui.theme.WeathersTheme

@Composable
fun SearchBar(
    onValueChange: (String) -> Unit,
    onFocused: (Boolean) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
    ) {

        var query by remember { mutableStateOf("") }
        var focused by remember { mutableStateOf(false) }

        TextField(
            modifier = Modifier
                .weight(1.0f)
                .onFocusChanged { focusState ->
                    focused = focusState.isFocused
                    onFocused(focused)
                    if(!focused) {
                        query = ""
                        onValueChange(query)
                    }
                },
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

                    if (focused) {
                        IconButton(
                            onClick = {
                                onClose()
                            }
                        ) { Icon(imageVector = Icons.Default.Close, contentDescription = null) }
                    }
                }

            },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            value = query,
            onValueChange = { s ->
                query = s
                onValueChange(query)
            },
            maxLines = 1
        )
    }
}

@Preview
@Composable
private fun TopSearchBarPreview() {
    WeathersTheme {
        SearchBar(
            onValueChange = {},
            onFocused = {},
            onClose = {}
        )
    }
}