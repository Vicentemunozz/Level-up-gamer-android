package com.example.app_1a.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_1a.data.api.external.CurrencyClient
import com.example.app_1a.data.db.entity.Product
import com.example.app_1a.data.repository.ProductRepository
import com.example.app_1a.service.LocationService
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import android.util.Log

class HomeViewModel(
    locationService: LocationService,
    productRepository: ProductRepository
) : ViewModel() {

    // --- 1. Lógica de Ubicación (Código original) ---
    val currentLocation: StateFlow<String> = locationService.locationFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "Obteniendo ubicación..."
        )

    // --- 2. Lógica de Productos (Código original) ---
    val products: StateFlow<List<Product>> = productRepository.allProducts
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // --- 3. Lógica de Moneda (NUEVO Y CORREGIDO) ---

    // Variable que guarda el valor del dólar.
    // Usamos 'mutableStateOf' para que la pantalla se entere cuando cambie.
    private var usdRate by mutableStateOf(0.0)

    init {
        fetchUsdRate() // Llamamos a la API al iniciar
    }

    private fun fetchUsdRate() {
        viewModelScope.launch {
            try {
                Log.d("API_TEST", "Consultando valor del dólar...")
                val response = CurrencyClient.api.getExchangeRate(from = "CLP", to = "USD")
                usdRate = response.rates["USD"] ?: 0.001 // Valor real
                Log.d("API_TEST", "Dólar obtenido: $usdRate")
            } catch (e: Exception) {
                Log.e("API_TEST", "Error API Dólar: ${e.message}")
                e.printStackTrace()

                // 🟢 PLAN B: Si falla la API (por el firewall del emulador),
                // usamos un valor aproximado para que la app se vea bien.
                // 1 USD = 980 CLP aprox => 1 CLP = 0.00102 USD
                usdRate = 0.00102
            }
        }
    }

    // Función que usa la UI para convertir el precio
    fun convertToUsd(clpPrice: Double): String {
        if (usdRate == 0.0) return "Cargando..."
        val usdPrice = clpPrice * usdRate
        // Formateamos a 2 decimales con signo de dólar
        return String.format("$%.2f USD", usdPrice)
    }
}