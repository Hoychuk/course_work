package com.example.noteapp.ui.objects

import kotlinx.serialization.Serializable

@Serializable
data class NewNoteScreenDataObject(
    val uid: String = "",
    val date: String = "",
)