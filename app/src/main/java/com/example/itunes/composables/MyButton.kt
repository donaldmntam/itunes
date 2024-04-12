package com.example.itunes.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ElevatedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

@Composable
fun MyButton(
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit,
) {
    ElevatedButton(
        onClick = onClick,
        shape = RectangleShape,
        contentPadding = PaddingValues(8.dp),
        content = content,
    )
}
