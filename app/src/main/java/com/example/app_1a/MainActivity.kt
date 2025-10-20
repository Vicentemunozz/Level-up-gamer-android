package com.example.app_1a

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.app_1a.data.db.AppDatabase
import com.example.app_1a.data.repository.ProductRepository
import com.example.app_1a.data.repository.UserRepository
import com.example.app_1a.service.LocationService
import com.example.app_1a.service.NotificationService
import com.example.app_1a.ui.screens.LoginScreen
import com.example.app_1a.ui.screens.MainScreen
import com.example.app_1a.ui.screens.RegisterScreen
import com.example.app_1a.ui.theme.App_1ATheme
import com.example.app_1a.ui.viewmodel.AdminViewModel
import com.example.app_1a.ui.viewmodel.AuthViewModel
import com.example.app_1a.ui.viewmodel.HomeViewModel
import com.example.app_1a.ui.viewmodel.ProfileViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database by lazy { AppDatabase.getDatabase(this) }
        val userRepository by lazy { UserRepository(database.userDao()) }
        val productRepository by lazy { ProductRepository(database.productDao()) }
        val locationService by lazy { LocationService(applicationContext) }
        val notificationService by lazy { NotificationService(applicationContext) }


        val viewModelFactory = AppViewModelFactory(userRepository, productRepository, locationService)

        setContent {
            App_1ATheme {
                AppNavigation(viewModelFactory, notificationService)
            }
        }
    }
}

class AppViewModelFactory(
    private val userRepository: UserRepository,
    private val productRepository: ProductRepository,
    private val locationService: LocationService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(userRepository) as T
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(locationService, productRepository) as T
            modelClass.isAssignableFrom(AdminViewModel::class.java) -> AdminViewModel(productRepository) as T
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> ProfileViewModel(userRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

@Composable
fun AppNavigation(viewModelFactory: AppViewModelFactory, notificationService: NotificationService) {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel(factory = viewModelFactory)
    val authState by authViewModel.uiState.collectAsState()

    LaunchedEffect(authState.loginSuccess) {
        if (authState.loginSuccess) {
            navController.navigate("main") {
                popUpTo("login") { inclusive = true }
            }
        } else {
            val currentRoute = navController.currentDestination?.route
            if (currentRoute != "login" && currentRoute != "register") {
                navController.navigate("login") {
                    popUpTo("main") { inclusive = true }
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LaunchedEffect(Unit) { authViewModel.resetAuthState() }
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = { /* Handled by LaunchedEffect */ },
                onNavigateToRegister = { navController.navigate("register") }
            )
        }
        composable("register") {
            LaunchedEffect(Unit) { authViewModel.resetAuthState() }
            RegisterScreen(
                viewModel = authViewModel,
                onRegisterSuccess = { navController.popBackStack() },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }
        composable("main") {
            MainScreen(
                userRole = authState.loggedInUserRole ?: "user",
                userId = authState.loggedInUserId ?: -1,
                viewModelFactory = viewModelFactory,
                // AÑADIMOS EL PARÁMETRO QUE FALTABA
                notificationService = notificationService,
                onLogout = { authViewModel.logout() }
            )
        }
    }
}

