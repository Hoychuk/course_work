package com.example.noteapp.ui.items

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
fun SmallButton(
    icon: Painter,
    desc: String,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(35.dp)
    ) {
        Icon(
            painter = icon,
            contentDescription = desc,
            modifier = Modifier.size(30.dp)
        )
    }
}