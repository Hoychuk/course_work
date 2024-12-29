package com.example.noteapp.ui.items

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.noteapp.ui.theme.ButtonColorBlue

@Composable
fun LongButton(
    backColor: Color = ButtonColorBlue,
    text: String,
    onclick: () -> Unit
) {
    Button(onClick = {
        onclick()
    },
        modifier = Modifier.fillMaxWidth().height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backColor
        )
    ) {
        Text(
            text = text,
            fontFamily = FontFamily.Serif,
            fontSize = 16.sp,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold
            )
    }
}