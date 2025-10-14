package com.example.testing_gemini_ddam.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Login : BottomNavItem("login", Icons.Default.AccountCircle, "Login/Registro")
    object Profile : BottomNavItem("profile", Icons.Default.Person, "Perfil")
    object Inventory : BottomNavItem("inventory", Icons.Default.List, "Inventario")
    object Admin : BottomNavItem("admin", Icons.Default.Build, "Administrador")
}