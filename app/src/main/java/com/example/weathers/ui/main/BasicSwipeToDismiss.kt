package com.example.weathers.ui.main

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> BasicSwipeToDismiss(
    target: T,
    onDelete: (T) -> Unit,
    background: @Composable RowScope.() -> Unit,
    dismissContent: @Composable RowScope.() -> Unit,
) {
    SwipeToDismiss(
        state = DismissState(
            initialValue = DismissValue.Default,
            confirmValueChange = { dismissValue ->
                if (dismissValue != DismissValue.Default)
                    onDelete(target)

                true
            }
        ),
        background = background,
        dismissContent = dismissContent
    )
}