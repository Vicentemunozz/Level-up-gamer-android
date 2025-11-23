package com.example.app_1a.util

import android.Manifest
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

// Un Composable para gestionar la solicitud de permisos de forma centralizada.
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionsHandler(
    onPermissionsGranted: () -> Unit
) {
    // Lista de permisos requeridos por la aplicación.
    val permissionsToRequest = mutableListOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
    )
    // El permiso de notificaciones solo se necesita en Android 13 (API 33) o superior.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
    }

    // Estado que rastrea el estado de los permisos (concedidos, denegados).
    val permissionState = rememberMultiplePermissionsState(permissions = permissionsToRequest)

    // LaunchedEffect se ejecuta una vez cuando el Composable entra en la composición.
    // Es el lugar ideal para solicitar permisos.
    LaunchedEffect(key1 = true) {
        if (!permissionState.allPermissionsGranted) {
            permissionState.launchMultiplePermissionRequest()
        }
    }

    // Si todos los permisos están concedidos, llama al callback.
    if (permissionState.allPermissionsGranted) {
        onPermissionsGranted()
    }
}
