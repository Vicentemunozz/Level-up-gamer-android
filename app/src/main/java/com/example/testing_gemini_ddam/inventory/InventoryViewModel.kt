package com.example.testing_gemini_ddam.inventory

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.testing_gemini_ddam.data.Product
import com.example.testing_gemini_ddam.data.ProductDatabase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class InventoryViewModel(application: Application) : AndroidViewModel(application) {

    private val productDao = ProductDatabase.getDatabase(application).productDao()

    val products = productDao.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addProduct(name: String, quantity: String, imageUri: String?) {
        val quantityInt = quantity.toIntOrNull() ?: return
        viewModelScope.launch {
            productDao.insert(Product(name = name, quantity = quantityInt, imageUri = imageUri))
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            productDao.delete(product)
        }
    }
}