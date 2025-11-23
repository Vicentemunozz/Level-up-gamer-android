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

// 🟢 IMPORTACIÓN CORRECTA PARA LA LIBRERÍA DE ANDROID
import com.auth0.android.jwt.JWT

data class AuthUiState(
    val email: String = "",
    val pass: String = "",
    val birthDate: String = "",
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

    fun onBirthDateChange(date: String) {
        _uiState.update { it.copy(birthDate = date, birthDateError = null) }
    }

    private fun validateEmail(email: String): String? {
        return when {
            email.isBlank() -> "El email no puede estar vacío."
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Formato inválido."
            else -> null
        }
    }

    private fun validatePassword(pass: String): String? {
        return if (pass.length < 6) "Mínimo 6 caracteres." else null
    }

    private fun validateBirthDate(date: String): String? {
        if (date.isBlank()) return "Selecciona una fecha."
        return try {
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val birthDate = LocalDate.parse(date, formatter)
            if (Period.between(birthDate, LocalDate.now()).years < 18) "Debes ser mayor de 18." else null
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
                val newUser = User(email = email, pass = pass, birthDate = birthDate)
                val result = repository.register(newUser)

                if (result.isSuccess) {
                    _uiState.update { it.copy(registrationSuccess = true, authError = null) }
                } else {
                    val errorMsg = result.exceptionOrNull()?.message ?: "Error al registrar"
                    _uiState.update { it.copy(authError = errorMsg) }
                }
            }
        }
    }

    // --- LOGIN CORREGIDO PARA LIBRERÍA ANDROID ---
    fun login() {
        val email = _uiState.value.email
        val pass = _uiState.value.pass

        val emailError = validateEmail(email)
        val passError = validatePassword(pass)
        _uiState.update { it.copy(emailError = emailError, passError = passError) }

        if (emailError == null && passError == null) {
            viewModelScope.launch {
                val result = repository.login(email, pass)

                if (result.isSuccess) {
                    val authResponse = result.getOrNull()
                    val token = authResponse?.token

                    // 🟢 NUEVO: Guardamos el token en el cliente Retrofit para usarlo después
                    if (token != null) {
                        com.example.app_1a.data.api.RetrofitClient.authToken = token
                    }

                    // Variables para guardar lo que extraigamos
                    var userRole = "user"
                    var userId = 0

                    if (token != null) {
                        try {
                            // 🟢 CORRECCIÓN AQUÍ: Usamos el constructor JWT(token)
                            val jwt = JWT(token)

                            // Extraemos los datos (Claims)
                            // Nota: "sub" suele ser el ID en Spring Boot, y "role" el rol.
                            val subject = jwt.subject
                            userId = subject?.toIntOrNull() ?: 1

                            val roleClaim = jwt.getClaim("role")
                            // Si el claim no existe o es nulo, usamos "user" por defecto
                            userRole = roleClaim.asString() ?: "user"

                        } catch (e: Exception) {
                            // Si el token está mal formado, atrapamos el error pero dejamos pasar el login (como usuario normal)
                            e.printStackTrace()
                        }
                    }

                    // Actualizamos la UI con el rol real
                    _uiState.update {
                        it.copy(
                            loginSuccess = true,
                            authError = null,
                            loggedInUserRole = userRole,
                            loggedInUserId = userId
                        )
                    }
                } else {
                    _uiState.update { it.copy(authError = "Email o contraseña incorrectos.") }
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