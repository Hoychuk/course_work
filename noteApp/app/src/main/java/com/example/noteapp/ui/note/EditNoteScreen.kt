package com.example.noteapp.ui.note

import android.util.Log
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
import com.example.noteapp.R
import com.example.noteapp.data.Note
import com.example.noteapp.ui.objects.CalendarScreenDataObject
import com.example.noteapp.ui.items.LongButton
import com.example.noteapp.ui.items.RoundedCornerTextField
import com.example.noteapp.ui.items.SmallButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun EditNoteScreen(
    onBackClick: (CalendarScreenDataObject) -> Unit,
    uid: String,
    noteId: String,
    title: String,
    date: String,
    content: String,
) {
    //val fs = Firebase.firestore

    val titleState = remember {
        mutableStateOf(value = title)
    }

    val contentState = remember {
        mutableStateOf(content)
    }

    val errorState = remember {
        mutableStateOf("")
    }

    val firestore = remember {
        Firebase.firestore
    }

    /*val storage = remember {
        Firebase.storage
    }

    val imageUrl = remember {
        mutableStateOf<Uri?>(null)
    }

    fs.document("notes/${userId}/user_notes/${noteId}").get().addOnCompleteListener(){ task ->
        if(task.isSuccessful){
            imageUrl.value = task.result
        }
    }

    val selectedImageUri = remember {
        mutableStateOf<Uri?>(null)
    }

    selectedImageUri.value = imageUrl.toUri()

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        selectedImageUri.value = uri
    }*/

    Box(
        modifier = Modifier.fillMaxSize()
            .background(Color.White)
    )
    Column(modifier = Modifier.fillMaxSize().padding(
        start = 20.dp, end = 20.dp
    ),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(60.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            SmallButton(
                icon = painterResource(id = R.drawable.close_button),
                desc = "back button"
            ) {
                onBackClick(CalendarScreenDataObject(uid = uid))
            }
            Text(
                text = "Змінити нотатку",
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                fontFamily = FontFamily.Serif
            )
            SmallButton(
                icon = painterResource(id = R.drawable.delete_button),
                desc = "delete button"
            ) {
                deleteNote(
                    firestore,
                    Note(
                        key = noteId,
                        title = titleState.value,
                        content = contentState.value,
                        date = date
                    ),
                    uid,
                    onSuccess = {
                        onBackClick(CalendarScreenDataObject(uid))
                        //onSaved()
                    },
                    onError = {

                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
        RoundedCornerTextField(
            text = titleState.value,
            label = "Заголовок"
        ){
            titleState.value = it
        }
        Spacer(modifier = Modifier.height(10.dp))
        RoundedCornerTextField(
            height = 180,
            maxLines = 10,
            singleLine = false,
            text = contentState.value,
            label = "Зміст"
        ){
            contentState.value = it
        }
        Spacer(modifier = Modifier.height(10.dp))
        if(errorState.value.isNotEmpty()) {
            Text(
                text = errorState.value,
                color = Color.Red,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
        LongButton(text = "Зберегти зміни") {
            //if (selectedImageUri.value != null) {
            //saveNoteImage(
            if (titleState.value.isNotEmpty() && contentState.value.isNotEmpty()) {
                saveNoteToFireStore(
                    //selectedImageUri.value!!,
                    //storage,
                    firestore,
                    Note(
                        key = noteId,
                        title = titleState.value,
                        content = contentState.value,
                        date = date
                    ),
                    uid,
                    onSaved = {
                        Log.d("EditNoteScreen", "Note saved successfully for UID: $uid")
                        onBackClick(CalendarScreenDataObject(uid = uid))
                        //onSaved()
                    },
                    onError = { error ->
                        errorState.value = error
                    }
                )
            }else{
                errorState.value = "Відповідні поля не заповненні"
            }
        }/*else{
                saveNoteToFireStore(
                    firestore,
                    "",
                    Note(
                        key = noteId,
                        title = titleState.value,
                        content = contentState.value,
                        date = date
                    ),
                    userId,
                    onSaved = {
                        onBackClick(CalendarScreenDataObject(userId))
                        //onSaved()
                    },
                    onError = {

                    }
                )
            }*/
    }
    Spacer(modifier = Modifier.height(10.dp))
    /*if(selectedImageUri.value == null) {
        LongButton(
            backColor = ButtonColorGrey,
            text = "Add photo",
        ) {
            imageLauncher.launch("image/")
        }
    }*/
    /*if (selectedImageUri.value != null) {
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

            SmallButton(
                icon = painterResource(id = R.drawable.delete_button),
                desc = "delete button"
            ) {

            }
        }
    }
}*/
}
/*
private fun saveNoteImage(
    uri: Uri,
    storage: FirebaseStorage,
    firestore: FirebaseFirestore,
    note: Note,
    uid: String,
    onSaved: () -> Unit,
    onError: () -> Unit
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
                    onError()
                }
            )
        }
    }
}
*/
private fun saveNoteToFireStore(
    firestore: FirebaseFirestore,
    //url: String,
    note: Note,
    uid: String,
    onSaved: () -> Unit,
    onError: (String) -> Unit
){
    val db = firestore.collection("notes.${uid}")
    val key = note.key.ifEmpty { db.document().id}
    //db.document(key).set(note.copy(key = key, /*imageUrl = url*/))
    db.document(key).set(note)
        .addOnSuccessListener {
            onSaved()
        }
        .addOnFailureListener{
            onError(it.message?:"Save Error")
        }
}

private fun deleteNote(
    firestore: FirebaseFirestore,
    note: Note,
    uid: String,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
){
    val db = firestore.collection("notes.${uid}")
    val key = note.key.ifEmpty { db.document().id}
    db.document(key).delete().addOnSuccessListener {
        onSuccess()
    }
        .addOnFailureListener{
            onError(it.message?:"Save Error")
        }
}