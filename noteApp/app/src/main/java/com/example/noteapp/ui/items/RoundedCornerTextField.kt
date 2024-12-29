package com.example.noteapp.ui.items

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.noteapp.ui.theme.TextFildBackgrund

@Composable
fun RoundedCornerTextField(
    height: Int = 55,
    maxLines: Int = 1,
    singleLine: Boolean = true,
    text: String,
    label: String,
    onValueChange: (String) -> Unit
) {
    TextField(value = text, onValueChange = {
        onValueChange(it)
    },
        shape = RoundedCornerShape(25.dp),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = TextFildBackgrund,
            focusedContainerColor = TextFildBackgrund,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
        modifier = Modifier.fillMaxWidth().height(height.dp),
        label = {
            Text(text = label, color = Color.Gray)
        },
        singleLine = singleLine,
        maxLines = maxLines
    )
}