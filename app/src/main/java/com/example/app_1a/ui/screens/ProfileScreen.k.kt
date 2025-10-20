package com.example.app_1a.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.app_1a.ui.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    userId: Int,
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = userId) {
        viewModel.loadUserData(userId)
    }

    LaunchedEffect(key1 = uiState.updateSuccess) {
        if (uiState.updateSuccess) {
            snackbarHostState.showSnackbar("¡Perfil actualizado con éxito!")
            viewModel.resetUpdateStatus()
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Mi Perfil", style = MaterialTheme.typography.headlineMedium)
                Spacer(Modifier.height(24.dp))

                OutlinedTextField(
                    value = uiState.user?.email ?: "",
                    onValueChange = {},
                    label = { Text("Email (no se puede cambiar)") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = uiState.newPassword,
                    onValueChange = { viewModel.onNewPasswordChange(it) },
                    label = { Text("Nueva Contraseña (dejar en blanco para no cambiar)") },
                    isError = uiState.passwordError != null,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                uiState.passwordError?.let { Text(it, color = MaterialTheme.colorScheme.error) }

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = uiState.newBirthDate,
                    onValueChange = { viewModel.onNewBirthDateChange(it) },
                    label = { Text("Fecha de Nacimiento (dd-MM-yyyy)") },
                    isError = uiState.birthDateError != null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                uiState.birthDateError?.let { Text(it, color = MaterialTheme.colorScheme.error) }

                Spacer(Modifier.height(32.dp))

                Button(onClick = { viewModel.updateUserData() }, modifier = Modifier.fillMaxWidth()) {
                    Text("Guardar Cambios")
                }
                Spacer(Modifier.height(16.dp))
                OutlinedButton(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
                    Text("Cerrar Sesión")
                }
            }
        }
    }
}

