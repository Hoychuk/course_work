/*package com.example.noteapp.ui.note

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.example.noteapp.R
import com.example.noteapp.data.Note
import com.example.noteapp.ui.objects.CalendarScreenDataObject
import com.example.noteapp.ui.items.LongButton
import com.example.noteapp.ui.items.RoundedCornerTextField
import com.example.noteapp.ui.items.SmallButton
import com.example.noteapp.ui.theme.ButtonColorGrey
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

@Composable
fun NewNoteScreen(
    onBackClick: (CalendarScreenDataObject) -> Unit,
    uid: String,
    date: String
    //onSaved: () -> Unit
) {

    val titleState = remember {
        mutableStateOf("")
    }
    val contentState = remember {
        mutableStateOf("")
    }

    val errorState = remember {
        mutableStateOf("")
    }
    val selectedImageUri = remember {
        mutableStateOf<Uri?>(null)
    }

    val firestore = remember {
        Firebase.firestore
    }

    val storage = remember {
        Firebase.storage
    }

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        selectedImageUri.value = uri
    }

    Box(
        modifier = Modifier.fillMaxSize()
            .background(Color.White)
    )
    Column(
        modifier = Modifier.fillMaxSize().padding(
            start = 20.dp, end = 20.dp
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            SmallButton(
                icon = painterResource(id = R.drawable.close_button),
                desc = "back button"
            ) {
                onBackClick(CalendarScreenDataObject(uid))
            }
            Text(
                text = "Нова нотатка",
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                fontFamily = FontFamily.Serif
            )
            SmallButton(
                icon = painterResource(id = R.drawable.delete_button),
                desc = "delete button"
            ) {
                onBackClick(CalendarScreenDataObject(uid))
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
        RoundedCornerTextField(
            text = titleState.value,
            label = "Заголовок"
        ) {
            titleState.value = it
        }
        Spacer(modifier = Modifier.height(10.dp))
        RoundedCornerTextField(
            height = 180,
            maxLines = 10,
            singleLine = false,
            text = contentState.value,
            label = "Зміст"
        ) {
            contentState.value = it
        }
        if (errorState.value.isNotEmpty()) {
            Text(
                text = errorState.value,
                color = Color.Red,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
        Spacer(modifier = Modifier.height(10.dp))
        LongButton(text = "Додати нотатку") {
            //if (selectedImageUri.value != null) {
            //saveNoteImage(
            if (titleState.value.isNotEmpty() && contentState.value.isNotEmpty()) {
                if (selectedImageUri.value == null) {

                    saveNoteToFireStore(
                        firestore,
                        "",
                        Note(
                            title = titleState.value,
                            content = contentState.value,
                            date = date
                        ),
                        uid,
                        onSaved = {
                            onBackClick(CalendarScreenDataObject(uid))
                        },
                        onError = { error ->
                            errorState.value = error
                        }
                    )
                } else {
                    saveNoteImage(
                        selectedImageUri.value!!,
                        storage,
                        firestore,
                        Note(
                            title = titleState.value,
                            content = contentState.value,
                            date = date
                        ),
                        uid,
                        onSaved = {
                            onBackClick(CalendarScreenDataObject(uid))
                        },
                        onError = { error ->
                            errorState.value = error
                        }
                    )
                }
            } else {
                errorState.value = "Відповідні поля не заповненні"
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        if (selectedImageUri.value == null) {
            LongButton(
                backColor = ButtonColorGrey,
                text = "Add photo",
            ) {
                imageLauncher.launch("image/")
            }
        }
        if (selectedImageUri.value != null) {
            LongButton(
                backColor = ButtonColorGrey,
                text = "Remove photo",
            ) {
                selectedImageUri.value = null
            }
            Row {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = selectedImageUri.value
                    ),
                    contentDescription = "User_photo",
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

private fun saveNoteImage(
    uri: Uri,
    storage: FirebaseStorage,
    firestore: FirebaseFirestore,
    note: Note,
    uid: String,
    onSaved: () -> Unit,
    onError: (String) -> Unit
){
    val timeStamp = System.currentTimeMillis()
    val storageRef = storage.reference
        .child("note_images")
        .child("image_$timeStamp.jpg")
    val uploadTask = storageRef.putFile(uri)
    uploadTask.addOnSuccessListener{
        storageRef.downloadUrl.addOnSuccessListener{ url ->
            saveNoteToFireStore(
                firestore,
                url.toString(),
                note,
                uid,
                onSaved = {
                    onSaved()
                },
                onError = {
                    onError(it)
                }
            )
        }
    }
}


private fun saveNoteToFireStore(
    firestore: FirebaseFirestore,
    url: String,
    note: Note,
    uid: String,
    onSaved: () -> Unit,
    onError: (String) -> Unit
){

    val db = firestore.collection("notes.${uid}")
    val key = db.document().id
    db.document(key).set(note.copy(key = key, imageUrl = url))
        .addOnSuccessListener {
            onSaved()
        }
        .addOnFailureListener{
            onError(it.message?:"Save Error")
        }
}*/