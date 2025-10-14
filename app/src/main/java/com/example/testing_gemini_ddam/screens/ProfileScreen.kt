package com.example.testing_gemini_ddam.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices

@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    var location by remember { mutableStateOf("No location yet") }
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission Accepted
            fusedLocationClient.lastLocation.addOnSuccessListener { 
                location = if (it != null) {
                    "Lat: ${it.latitude}, Lon: ${it.longitude}"
                } else {
                    "Cannot get location"
                }
            }
        } else {
            // Permission Denied
            location = "Permission denied"
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = location)
        Button(onClick = {
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) -> {
                    // Some works that require permission
                    fusedLocationClient.lastLocation.addOnSuccessListener { 
                         location = if (it != null) {
                            "Lat: ${it.latitude}, Lon: ${it.longitude}"
                        } else {
                            "Cannot get location"
                        }
                    }
                }
                else -> {
                    // Asking for permission
                    launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
        }) {
            Text("Get Location")
        }
    }
}