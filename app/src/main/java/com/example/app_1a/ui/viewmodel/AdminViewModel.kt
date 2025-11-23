package com.example.app_1a.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_1a.data.db.entity.Product
import com.example.app_1a.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

data class AdminUiState(
    val productName: String = "",
    val productPrice: String = "",
    val error: String? = null,
    val selectedProduct: Product? = null,
    val isLoading: Boolean = false // Para mostrar carga si quieres
)

class AdminViewModel(private val repository: ProductRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState: StateFlow<AdminUiState> = _uiState.asStateFlow()

    // Usamos MutableStateFlow para la lista para poder actualizarla manualmente
    private val _allProducts = MutableStateFlow<List<Product>>(emptyList())
    val allProducts: StateFlow<List<Product>> = _allProducts.asStateFlow()

    init {
        loadProducts() // Cargar al iniciar
    }

    // Función para recargar la lista desde el servidor
    fun loadProducts() {
        viewModelScope.launch {
            repository.allProducts.collect { products ->
                _allProducts.value = products
            }
        }
    }

    fun onProductNameChange(name: String) {
        _uiState.update { it.copy(productName = name) }
    }

    fun onProductPriceChange(price: String) {
        _uiState.update { it.copy(productPrice = price) }
    }

    fun selectProduct(product: Product) {
        _uiState.update {
            it.copy(
                selectedProduct = product,
                productName = product.name,
                productPrice = product.price.toString()
            )
        }
    }

    fun clearSelection() {
        _uiState.update { AdminUiState() }
    }

    // ... (dentro de AdminViewModel)

    fun saveProduct() {
        val name = _uiState.value.productName.trim()
        val price = _uiState.value.productPrice.toDoubleOrNull()
        val selected = _uiState.value.selectedProduct

        if (name.isEmpty() || price == null || price <= 0) {
            _uiState.update { it.copy(error = "Datos inválidos.") }
            return
        }

        viewModelScope.launch {
            try {
                if (selected == null) {
                    // CREAR NUEVO
                    repository.insertProduct(Product(id = null, name = name, price = price))
                } else {
                    // MODIFICAR EXISTENTE
                    val updatedProduct = selected.copy(name = name, price = price)
                    repository.updateProduct(updatedProduct)
                }

                // 🟢 FIX ANTI-CRASH: Esperamos 300ms para que la animación del botón termine
                delay(300)

                // Recargar y Limpiar
                loadProducts()
                clearSelection()
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Error al guardar: ${e.message}") }
            }
        }
    }

    fun deleteProduct(productId: Int) {
        viewModelScope.launch {
            try {
                repository.deleteProduct(productId)

                // 🟢 FIX ANTI-CRASH: Esperamos un poco también al borrar
                delay(300)

                loadProducts()
            } catch (e: Exception) {
                // Manejar error si es necesario
            }
        }
    }
}