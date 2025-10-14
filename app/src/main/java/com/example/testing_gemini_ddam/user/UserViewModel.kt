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

    fun login(email: String) {
        viewModelScope.launch {
            val user = userDao.getUser(email)
            if (user != null) {
                _loginState.value = LoginState.LoggedIn
            } else {
                _loginState.value = LoginState.Error("User not found")
            }
        }
    }

    fun register(email: String) {
        viewModelScope.launch {
            userDao.insert(User(email = email))
            _loginState.value = LoginState.LoggedIn
        }
    }
}

sealed class LoginState {
    object LoggedIn : LoginState()
    object LoggedOut : LoginState()
    data class Error(val message: String) : LoginState()
}