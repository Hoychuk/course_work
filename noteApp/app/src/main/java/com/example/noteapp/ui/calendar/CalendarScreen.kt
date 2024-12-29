import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.example.noteapp.R
import com.example.noteapp.data.Note
import com.example.noteapp.ui.objects.EditNoteScreenDataObject
import com.example.noteapp.ui.objects.NewNoteScreenDataObject
import com.example.noteapp.ui.items.NoteItem
import com.example.noteapp.ui.items.SmallButton
import com.example.noteapp.ui.theme.ButtonColorBlue
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    onLogoutClick: () -> Unit,
    onNewNoteClick: (NewNoteScreenDataObject) -> Unit,
    onEditNoteClick: (EditNoteScreenDataObject) -> Unit,
    uid: String,
    //email: String
    //dataObject: CalendarScreenDataObject
    //onNavigationToNewNoteScreen: (String) -> Unit,
) {

    Log.d("CalendarScreen", "UID: $uid")

    var showLogoutDialog by remember { mutableStateOf(false) }

    val fs = Firebase.firestore
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    val list = remember {
        mutableStateOf(emptyList<Note>())
    }

    val auth = remember {
        Firebase.auth
    }

    fs.collection("notes.${uid}").get().addOnCompleteListener(){ task ->
        if(task.isSuccessful){
            list.value = task.result.toObjects(Note::class.java)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Box(modifier = Modifier.width(50.dp)
            .height(50.dp)
            .align(Alignment.End),

            ){
            SmallButton(
                icon = painterResource(id = R.drawable.log_out_button),
                desc = "log_out"
            ) {
                showLogoutDialog = true
            }
            LogoutConfirmationDialog(
                showDialog = showLogoutDialog,
                onConfirm = {
                    showLogoutDialog = false
                    logOut(auth = auth)
                    onLogoutClick()
                },
                onDismiss = {
                    showLogoutDialog = false
                }
            )
        }

        // Calendar displayed at the top
        DatePicker(
            state = datePickerState,
            showModeToggle = true,
            colors = DatePickerDefaults.colors(
                todayDateBorderColor = Color("#3ba1e3".toColorInt()),
                selectedDayContainerColor = Color("#3ba1e3".toColorInt()),
            ),
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp))
        )

        /*// Button to confirm date selection
        Button(onClick = {
            selectedDate = datePickerState.selectedDateMillis?.let { convertMillisToDate(it) } ?: ""
        }) {
            Text("Select Date")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Text field to display the selected date
        OutlinedTextField(
            value = selectedDate,
            onValueChange = { },
            label = { Text("Selected Date") },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
        )*/
        Box(modifier = Modifier
            .width(66.dp)
            .height(50.dp)
            .padding(end = 16.dp)
            .background(ButtonColorBlue, shape = RoundedCornerShape(15.dp))
            .align(Alignment.End),
            contentAlignment = Alignment.Center){
            SmallButton(
                icon = painterResource(id = R.drawable.add_button),
                desc = "add note"
            ) {
                if(selectedDate != ""){
                    onNewNoteClick(NewNoteScreenDataObject(uid, selectedDate))
                    //onNavigationToNewNoteScreen()
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(list.value) { item ->
                if(item.date == selectedDate) {
                    Spacer(modifier = Modifier.height(15.dp))
                    NoteItem(
                        text = item.title,
                        icon = painterResource(id = R.drawable.arrow_right),
                        desc = "open note"
                    ) {
                        onEditNoteClick(
                            EditNoteScreenDataObject(
                                uid,
                                item.key,
                                item.title,
                                item.date,
                                item.content
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .padding(horizontal = 16.dp)
                            .background(Color.Black)
                    )
                }
            }
        }
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM.dd.yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

fun logOut(auth: FirebaseAuth){
    auth.signOut()
}

@Composable
fun LogoutConfirmationDialog(
    showDialog: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(text = "Вийти з облікового запису?")
            },
            text = {
                Text(text = "Ви впевнені, що хочете вийти з вашого облікового запису? Цю дію не можна скасувати.")
            },
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text(text = "Так")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(text = "Ні")
                }
            }
        )
    }
}