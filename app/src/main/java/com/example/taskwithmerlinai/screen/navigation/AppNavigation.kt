package com.example.taskwithmerlinai.screen.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.taskwithmerlinai.screen.HomeScreen
import com.example.taskwithmerlinai.screen.chat.ChatScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home_screen") {
        composable("home_screen") {
            HomeScreen (onNavigateToDetail = {
                navController.navigate("chat_screen")
            })
        }

        composable("chat_screen") {
            ChatScreen (onBack = {
                navController.popBackStack()
            })
        }
    }
}