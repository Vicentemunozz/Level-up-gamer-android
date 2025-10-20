package com.example.app_1a.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_1a.data.db.entity.Product
import com.example.app_1a.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AdminUiState(
    val productName: String = "",
    val productPrice: String = "",
    val error: String? = null
)

class AdminViewModel(private val repository: ProductRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState: StateFlow<AdminUiState> = _uiState.asStateFlow()

    val allProducts: StateFlow<List<Product>> = repository.allProducts
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onProductNameChange(name: String) {
        _uiState.update { it.copy(productName = name) }
    }

    fun onProductPriceChange(price: String) {
        _uiState.update { it.copy(productPrice = price) }
    }

    fun addProduct() {
        val name = _uiState.value.productName.trim()
        val price = _uiState.value.productPrice.toDoubleOrNull()

        if (name.isEmpty() || price == null || price <= 0) {
            _uiState.update { it.copy(error = "Nombre o precio inválidos.") }
            return
        }

        viewModelScope.launch {
            repository.insertProduct(Product(name = name, price = price))
            _uiState.update { AdminUiState() }
        }
    }
}

