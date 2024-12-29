package com.example.noteapp.ui.objects

import kotlinx.serialization.Serializable

@Serializable
data class EditNoteScreenDataObject(
    val uid: String = "",
    val noteId: String = "",
    val title: String = "",
    val date: String = "",
    val content: String = "",
    //val imageUrl: String = ""
)
