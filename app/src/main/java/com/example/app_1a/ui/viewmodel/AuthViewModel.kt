package com.example.app_1a.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_1a.data.db.entity.User
import com.example.app_1a.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

data class AuthUiState(
    val email: String = "",
    val pass: String = "",
    val birthDate: String = "", // Almacenará la fecha como "dd-MM-yyyy"
    val emailError: String? = null,
    val passError: String? = null,
    val birthDateError: String? = null,
    val registrationSuccess: Boolean = false,
    val loginSuccess: Boolean = false,
    val authError: String? = null,
    val loggedInUserRole: String? = null,
    val loggedInUserId: Int? = null
)

class AuthViewModel(private val repository: UserRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, emailError = validateEmail(email)) }
    }

    fun onPassChange(pass: String) {
        _uiState.update { it.copy(pass = pass, passError = validatePassword(pass)) }
    }

    // Esta función ahora recibe la fecha ya formateada desde el DatePicker
    fun onBirthDateChange(date: String) {
        _uiState.update { it.copy(birthDate = date, birthDateError = null) }
    }

    private fun validateEmail(email: String): String? {
        return when {
            email.isBlank() -> "El email no puede estar vacío."
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Formato de email inválido."
            else -> null
        }
    }

    private fun validatePassword(pass: String): String? {
        return when {
            pass.isBlank() -> "La contraseña no puede estar vacía."
            pass.length < 6 -> "La contraseña debe tener al menos 6 caracteres."
            else -> null
        }
    }

    private fun validateBirthDate(date: String): String? {
        if (date.isBlank()) return "Debes seleccionar una fecha."

        return try {
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val birthDate = LocalDate.parse(date, formatter)
            if (Period.between(birthDate, LocalDate.now()).years < 18) {
                "Debes ser mayor de 18 años para registrarte."
            } else {
                null
            }
        } catch (e: DateTimeParseException) {
            "Fecha inválida."
        }
    }

    fun register() {
        val email = _uiState.value.email
        val pass = _uiState.value.pass
        val birthDate = _uiState.value.birthDate

        val emailError = validateEmail(email)
        val passError = validatePassword(pass)
        val birthDateError = validateBirthDate(birthDate)

        _uiState.update { it.copy(emailError = emailError, passError = passError, birthDateError = birthDateError) }

        if (emailError == null && passError == null && birthDateError == null) {
            viewModelScope.launch {
                val existingUser = repository.findUserByEmail(email)
                if (existingUser != null) {
                    _uiState.update { it.copy(authError = "El email ya está registrado.") }
                } else {
                    val newUser = User(
                        email = email,
                        pass = pass,
                        birthDate = birthDate
                    )
                    repository.insertUser(newUser)
                    _uiState.update { it.copy(registrationSuccess = true) }
                }
            }
        }
    }

    fun login() {
        val emailError = validateEmail(_uiState.value.email)
        val passError = validatePassword(_uiState.value.pass)
        _uiState.update { it.copy(emailError = emailError, passError = passError) }

        if (emailError == null && passError == null) {
            viewModelScope.launch {
                val user = repository.findUserByEmail(_uiState.value.email)
                if (user == null || user.pass != _uiState.value.pass) {
                    _uiState.update { it.copy(authError = "Email o contraseña incorrectos.") }
                } else {
                    _uiState.update {
                        it.copy(
                            loginSuccess = true,
                            loggedInUserRole = user.role,
                            loggedInUserId = user.id
                        )
                    }
                }
            }
        }
    }

    fun resetAuthState() {
        _uiState.value = AuthUiState()
    }

    fun logout() {
        _uiState.value = AuthUiState()
    }
}

