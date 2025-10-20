package com.example.app_1a.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.app_1a.AppViewModelFactory
import com.example.app_1a.service.NotificationService
import com.example.app_1a.ui.viewmodel.HomeViewModel
import com.example.app_1a.ui.viewmodel.ProfileViewModel

data class NavItem(val label: String, val icon: ImageVector, val route: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    userRole: String,
    userId: Int,
    viewModelFactory: AppViewModelFactory,
    notificationService: NotificationService,
    onLogout: () -> Unit
) {
    val navController = rememberNavController()

    val navItems = mutableListOf(
        NavItem("Home", Icons.Default.Home, "home"),
        NavItem("Eventos", Icons.Default.Event, "events"),
        NavItem("Profile", Icons.Default.Person, "profile")
    )

    if (userRole == "admin") {
        navItems.add(NavItem("Admin", Icons.Default.AdminPanelSettings, "admin"))
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                navItems.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = "home",
            Modifier.padding(innerPadding)
        ) {
            composable("home") { HomeScreen(viewModel = viewModel(factory = viewModelFactory)) }

            // CORREGIDO: La llamada a EventsScreen ahora es más simple.
            // Solo le pasamos el notificationService, que es lo único que necesita.
            composable("events") {
                EventsScreen(notificationService = notificationService)
            }

            composable("profile") {
                val profileViewModel: ProfileViewModel = viewModel(factory = viewModelFactory)
                ProfileScreen(viewModel = profileViewModel, userId = userId, onLogout = onLogout)
            }
            if (userRole == "admin") {
                composable("admin") { AdminScreen(viewModel = viewModel(factory = viewModelFactory)) }
            }
        }
    }
}

