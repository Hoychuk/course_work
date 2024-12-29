package com.example.noteapp.ui.items

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.example.noteapp.R

@Composable
fun PhotoItem(
    selectedImageUri: Uri,
    icon: Painter,
    desc: String,
    onClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()){
        Image(
            painter = rememberAsyncImagePainter(
                model = selectedImageUri),
            contentDescription = "User_photo",
            modifier = Modifier.fillMaxSize()
            )

        SmallButton(
            icon = icon,
            desc = desc,
        ) {

        }
    }
}