package com.example.noteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            NavGraph(navController = navController)

            /*NavHost(
                navController = navController,
                startDestination = LoginScreenObject){

                composable<LoginScreenObject> {
                    LoginScreen(){navData ->
                        navController.navigate(navData)
                    }
                }
                composable<RegisterScreenObject> {
                    RegisterScreen(){ navData ->
                        navController.navigate(navData)
                    }
                }
                composable<CalendarScreenDataObject> {navEntry ->
                    val navData = navEntry.toRoute<CalendarScreenDataObject>()
                    CalendarScreen()
                }
                composable<NewNoteScreenObject> {
                    NewNoteScreen()
                }
                composable("NewNoteScreen") {

                }
            }*/
        }
    }
}
