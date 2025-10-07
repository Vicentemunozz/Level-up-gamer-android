package com.example.intento_proyecto.ui.navigation

// Define las rutas únicas para cada pantalla de la aplicación
sealed class Screen(val route: String) {
    object LoginScreen : Screen("login_screen")
    object RegisterScreen : Screen("register_screen")
    object HomeScreen : Screen("home_screen")
    object ProfileScreen : Screen("profile_screen")
}
