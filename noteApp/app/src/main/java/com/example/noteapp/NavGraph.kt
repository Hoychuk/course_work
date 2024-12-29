package com.example.noteapp

import CalendarScreen
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.noteapp.ui.authentification.LoginScreen
import com.example.noteapp.ui.authentification.RegisterScreen
import com.example.noteapp.ui.note.EditNoteScreen
import com.example.noteapp.ui.note.NewNoteScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "loginScreen") {

        composable("registerScreen") {
            RegisterScreen(
                onLoginClick = { navController.navigate("loginScreen") },
                onRegisterSuccess = { navData ->
                    navController.navigate("calendarScreen/${navData.uid}") }
            )
        }

        composable("loginScreen") {
            LoginScreen(
                onRegisterClick = { navController.navigate("registerScreen") },
                onLoginSuccess = { navData ->
                    navController.navigate("calendarScreen/${navData.uid}") }
            )
        }

        composable("calendarScreen/{uid}",
            arguments = listOf(
                navArgument(name = "uid"){
            type = NavType.StringType },
                /*navArgument(name = "email"){
            type = NavType.StringType
        },*/
            )) { navEntry ->
            var uid = navEntry.arguments?.getString("uid")
            //val email = navEntry.arguments?.getString("email")
            if (uid?.last() == '}'){
                Log.d("last}", "Note saved successfully for UID: $uid")
                uid = uid.dropLast(1)
            }
            Log.d("Graphcalendar", "Note saved successfully for UID: $uid")
            if (/*email != null &&*/ uid != null) {
                CalendarScreen(
                    onLogoutClick = { navController.navigate("loginScreen") },
                    onNewNoteClick = { navData ->
                        navController.navigate("newNoteScreen/${navData.uid}/${navData.date}") },
                    onEditNoteClick = { navData ->
                        navController.navigate("editNoteScreen/${navData.uid}" +
                                "/${navData.noteId}/${navData.title}/${navData.date}" +
                                "/${navData.content}") },
                    uid = uid,
                    //email = email
                )
            }
        }

        composable("newNoteScreen/{uid}/{date}",
            arguments = listOf(
                navArgument(name = "uid"){
                    type = NavType.StringType
                },
                navArgument(name = "date"){
                    type = NavType.StringType
                }
            )
        ) { navEntry ->
            val uid = navEntry.arguments?.getString("uid")
            val date = navEntry.arguments?.getString("date")
            if(uid != null && date != null) {
                NewNoteScreen(
                    onBackClick = {navData->
                        navController.navigate("calendarScreen/${navData.uid}") },
                    uid = uid,
                    date = date,
                )
            }
        }

        composable("editNoteScreen/{uid}/{noteId}/{title}/{date}/{content}",
            arguments = listOf(
                navArgument(name = "uid"){
                    type = NavType.StringType
                },
                navArgument(name = "noteId"){
                    type = NavType.StringType
                },
                navArgument(name = "title"){
                    type = NavType.StringType
                },
                navArgument(name = "date"){
                    type = NavType.StringType
                },
                navArgument(name = "content"){
                    type = NavType.StringType
                },
                /*navArgument(name = "imageUrl"){
                    type = NavType.StringType
                }*/
            )
        ) { navEntry ->
            val uid = navEntry.arguments?.getString("uid")
            val noteId = navEntry.arguments?.getString("noteId")
            val title = navEntry.arguments?.getString("title")
            val date = navEntry.arguments?.getString("date")
            val content = navEntry.arguments?.getString("content")
            //val imageUrl = navEntry.arguments?.getString("imageUrl")
            if(uid != null && noteId != null && title != null && date != null && content != null) {
                EditNoteScreen(
                    onBackClick = { navData ->
                        navController.navigate("calendarScreen/${navData.uid}}")
                        Log.d("graphedit", "Note saved successfully for UID: $uid")
                    },
                    uid = uid,
                    noteId = noteId,
                    title = title,
                    date = date,
                    content = content,
                )
            }
        }
    }
}