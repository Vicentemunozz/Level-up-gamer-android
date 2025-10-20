package com.example.app_1a.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_1a.data.db.entity.Product
import com.example.app_1a.data.repository.ProductRepository
import com.example.app_1a.service.LocationService
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    locationService: LocationService,
    productRepository: ProductRepository
) : ViewModel() {

    val currentLocation: StateFlow<String> = locationService.locationFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "Obteniendo ubicación..."
        )

    val products: StateFlow<List<Product>> = productRepository.allProducts
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}

