package com.example.testing_gemini_ddam.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.testing_gemini_ddam.screens.HomeScreen
import com.example.testing_gemini_ddam.screens.InventoryScreen
import com.example.testing_gemini_ddam.screens.LoginScreen
import com.example.testing_gemini_ddam.screens.ProfileScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("inventory") { InventoryScreen(navController) }
        composable("profile") { ProfileScreen(navController) }
    }
}