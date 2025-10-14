package com.example.testing_gemini_ddam.user

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.testing_gemini_ddam.data.ProductDatabase
import com.example.testing_gemini_ddam.data.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val userDao = ProductDatabase.getDatabase(application).userDao()

    private val _loginState = MutableStateFlow<LoginState>(LoginState.LoggedOut)
    val loginState = _loginState.asStateFlow()

    init {
        viewModelScope.launch {
            userDao.insert(User(email = "user@gmail.com", username = "john doe", birthDate = "01/01/2000", password = "admin"))
            userDao.insert(User(email = "admin@gmail.com", username = "userAdmin", birthDate = "01/01/2000", password = "123", isAdmin = true))
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val user = userDao.getUser(email, password)
            if (user != null) {
                _loginState.value = LoginState.LoggedIn(user)
            } else {
                _loginState.value = LoginState.Error("Invalid credentials")
            }
        }
    }

    fun register(email: String, username: String, birthDate: String, password: String) {
        viewModelScope.launch {
            userDao.insert(User(email = email, username = username, birthDate = birthDate, password = password))
            _loginState.value = LoginState.LoggedIn(User(email, username, birthDate, password))
        }
    }

    fun logout() {
        _loginState.value = LoginState.LoggedOut
    }
}

sealed class LoginState {
    data class LoggedIn(val user: User) : LoginState()
    object LoggedOut : LoginState()
    data class Error(val message: String) : LoginState()
}