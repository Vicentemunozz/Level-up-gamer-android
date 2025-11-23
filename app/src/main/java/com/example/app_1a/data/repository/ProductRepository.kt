package com.example.app_1a.data.repository

import android.util.Log // 🟢 IMPORTAR ESTO
import com.example.app_1a.data.api.RetrofitClient
import com.example.app_1a.data.db.entity.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProductRepository {

    val allProducts: Flow<List<Product>> = flow {
        try {
            val apiProducts = RetrofitClient.apiService.getProducts()
            emit(apiProducts)
        } catch (e: Exception) {
            Log.e("API_TEST", "Error cargando productos: ${e.message}")
            emit(emptyList())
        }
    }

    suspend fun insertProduct(product: Product) {
        try {
            val response = RetrofitClient.apiService.createProduct(product)
            Log.d("API_TEST", "Crear producto: $response") // Ver si se creó
        } catch (e: Exception) {
            Log.e("API_TEST", "Error creando: ${e.message}")
            e.printStackTrace()
        }
    }

    // 🟢 MODIFICADO: Ahora imprime el resultado
    suspend fun updateProduct(product: Product) {
        try {
            // El ID nunca debe ser nulo al actualizar, usamos 0 por seguridad
            val response = RetrofitClient.apiService.updateProduct(product.id ?: 0, product)
            Log.d("API_TEST", "Actualizar producto: $response")
        } catch (e: Exception) {
            Log.e("API_TEST", "Error actualizando: ${e.message}")
            e.printStackTrace()
        }
    }

    // 🟢 MODIFICADO: Ahora revisa si funcionó
    suspend fun deleteProduct(productId: Int) {
        try {
            val response = RetrofitClient.apiService.deleteProduct(productId)
            if (response.isSuccessful) {
                Log.d("API_TEST", "Eliminado con éxito ID: $productId")
            } else {
                Log.e("API_TEST", "Fallo al eliminar ID: $productId. Código: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("API_TEST", "Excepción al eliminar: ${e.message}")
            e.printStackTrace()
        }
    }
}