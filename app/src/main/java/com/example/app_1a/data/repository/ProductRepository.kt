package com.example.app_1a.data.repository

import com.example.app_1a.data.db.dao.ProductDao
import com.example.app_1a.data.db.entity.Product
import kotlinx.coroutines.flow.Flow

class ProductRepository(private val productDao: ProductDao) {

    val allProducts: Flow<List<Product>> = productDao.getAllProducts()

    suspend fun insertProduct(product: Product) {
        productDao.insertProduct(product)
    }
}

