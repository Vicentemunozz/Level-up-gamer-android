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

data class ProfileUiState(
    val user: User? = null,
    val newPassword: String = "",
    val newBirthDate: String = "",
    val passwordError: String? = null,
    val birthDateError: String? = null,
    val updateSuccess: Boolean = false,
    val isLoading: Boolean = true
)

class ProfileViewModel(private val repository: UserRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun loadUserData(userId: Int) {
        viewModelScope.launch {
            val user = repository.findUserById(userId)
            _uiState.update {
                it.copy(
                    user = user,
                    newBirthDate = user?.birthDate ?: "",
                    isLoading = false
                )
            }
        }
    }

    fun onNewPasswordChange(pass: String) {
        _uiState.update { it.copy(newPassword = pass, passwordError = validatePassword(pass)) }
    }

    fun onNewBirthDateChange(date: String) {
        _uiState.update { it.copy(newBirthDate = date, birthDateError = validateBirthDate(date)) }
    }

    private fun validatePassword(pass: String): String? {
        if (pass.isBlank()) return null
        return if (pass.length < 6) "La contraseña debe tener al menos 6 caracteres." else null
    }

    private fun validateBirthDate(date: String): String? {
        if (date.isBlank()) return "La fecha no puede estar vacía."
        return try {
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val birthDate = LocalDate.parse(date, formatter)
            if (Period.between(birthDate, LocalDate.now()).years < 18) {
                "Debes ser mayor de 18 años."
            } else {
                null
            }
        } catch (e: DateTimeParseException) {
            "Formato de fecha inválido (dd-MM-yyyy)."
        }
    }

    fun updateUserData() {
        val currentUser = _uiState.value.user ?: return
        val newPassword = _uiState.value.newPassword
        val newBirthDate = _uiState.value.newBirthDate

        val passError = validatePassword(newPassword)
        val birthDateError = validateBirthDate(newBirthDate)

        _uiState.update { it.copy(passwordError = passError, birthDateError = birthDateError) }

        if (passError == null && birthDateError == null) {
            viewModelScope.launch {
                val updatedUser = currentUser.copy(
                    pass = if (newPassword.isNotBlank()) newPassword else currentUser.pass,
                    birthDate = newBirthDate
                )
                repository.updateUser(updatedUser)
                _uiState.update { it.copy(updateSuccess = true, user = updatedUser, newPassword = "") }
            }
        }
    }

    fun resetUpdateStatus() {
        _uiState.update { it.copy(updateSuccess = false) }
    }
}

