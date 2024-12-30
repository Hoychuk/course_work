package com.example.noteapp.ui.authentification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.noteapp.ui.objects.CalendarScreenDataObject
import com.example.noteapp.ui.items.BackButton
import com.example.noteapp.ui.items.LongButton
import com.example.noteapp.ui.items.RoundedCornerTextField
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun RegisterScreen(
    onLoginClick: () -> Unit,
    onRegisterSuccess: (CalendarScreenDataObject) -> Unit
    //onRegisterSuccess: (/*CalendarScreenDataObject*/) -> Unit
    //onNavigationToCalendarScreen: (CalendarScreenDataObject) -> Unit,
) {

    val auth = remember {
        Firebase.auth
    }

    val errorState = remember {
        mutableStateOf("")
    }

    val emailState = remember {
        mutableStateOf("")
    }
    val passwordState = remember {
        mutableStateOf("")
    }

    val passwordState2 = remember {
        mutableStateOf("")
    }

    Box(
        modifier = Modifier.fillMaxSize()
            .background(Color.White)
    )
    Column(modifier = Modifier.fillMaxSize().padding(
        start = 20.dp, end = 20.dp
    ),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(80.dp))
        Text(
            text = "Реєстрація",
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            fontFamily = FontFamily.Serif
            )
        Spacer(modifier = Modifier.height(100.dp))
        RoundedCornerTextField(
            text = emailState.value,
            label = "Email",
        ){
            emailState.value = it
        }
        Spacer(modifier = Modifier.height(10.dp))
        RoundedCornerTextField(
            text = passwordState.value,
            label = "Пароль",
            password = true
        ){
            passwordState.value = it
        }
        Spacer(modifier = Modifier.height(10.dp))
        RoundedCornerTextField(
            text = passwordState2.value,
            label = "Повторіть пароль",
            password = true
        ){
            passwordState2.value = it
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
        LongButton(text = "ЗАРЕЄСТРУВАТИСЯ") {
            register(
                auth,
                emailState.value,
                passwordState.value,
                passwordState2.value,
                onRegisterSuccess = { navData ->
                    //onRegisterSuccess()
                    onRegisterSuccess(navData)
                    //onNavigationToCalendarScreen(navData)
                },
                onRegisterFailure = {error ->
                    errorState.value = error
                }
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        BackButton(text = "Повернутися до входу") {
            onLoginClick()
        }
    }
}

fun register(auth: FirebaseAuth,
             email: String,
             password: String,
             password2: String,
             onRegisterSuccess: (CalendarScreenDataObject) -> Unit,
             onRegisterFailure: (String) -> Unit){

    if (email.isBlank() || password.isBlank()){
        onRegisterFailure("Email та пароль не можуть бути порожніми")
        return
    }
    if (password.length < 6){
        onRegisterFailure("Пароль повинен бути більше 6 символів")
        return
    }
    if(!email.contains("@")){
        onRegisterFailure("Невірний email")
        return
    }

    if (password != password2){
        onRegisterFailure("Різні паролі")
        return
    }

    auth.createUserWithEmailAndPassword(email,password)
        .addOnCompleteListener{ task ->
            if (task.isSuccessful) {
                onRegisterSuccess(
                    CalendarScreenDataObject(
                        task.result.user?.uid!!,
                        //task.result.user?.email!!
                    )
                )
            }
        }
        .addOnFailureListener{
            onRegisterFailure(it.message?:"Register Error")
        }
}

