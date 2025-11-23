package com.example.app_1a.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val email: String,
    val pass: String,
    val birthDate: String, // Formato "dd-MM-yyyy"
    val role: String = "user" // "user" o "admin"
)

