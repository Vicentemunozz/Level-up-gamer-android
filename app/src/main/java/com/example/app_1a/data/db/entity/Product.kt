package com.example.app_1a.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null, // 🟢 CAMBIO: Agregamos el signo ? y lo igualamos a null
    val name: String,
    val price: Double
)