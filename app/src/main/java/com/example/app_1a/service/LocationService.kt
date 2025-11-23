package com.example.app_1a.service

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.google.android.gms.location.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.concurrent.TimeUnit

// Servicio para gestionar la obtención de la ubicación del dispositivo.
class LocationService(context: Context) {

    // Cliente para interactuar con el proveedor de ubicación fusionada.
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    // Expone la ubicación como un Flow de Kotlin, permitiendo observar cambios de forma reactiva.
    // callbackFlow es ideal para APIs basadas en callbacks como la de ubicación.
    @SuppressLint("MissingPermission")
    val locationFlow: Flow<String> = callbackFlow {
        // Configuración de la solicitud de ubicación.
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            TimeUnit.SECONDS.toMillis(10) // Intervalo de actualización: 10 segundos
        ).build()

        // Callback para recibir las actualizaciones de ubicación.
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    // Intenta enviar la nueva ubicación al Flow.
                    // Se formatea a una cadena "Lat: XX.XXX, Lon: YY.YYY".
                    trySend("Lat: ${location.latitude}, Lon: ${location.longitude}")
                }
            }
        }

        // Inicia las actualizaciones de ubicación.
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )

        // Se ejecuta cuando el Flow es cancelado (cuando el observador se va).
        // Detiene las actualizaciones de ubicación para ahorrar batería.
        awaitClose {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }
}
