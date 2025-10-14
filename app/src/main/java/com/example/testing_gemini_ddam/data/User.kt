package com.example.testing_gemini_ddam.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val email: String,
    val username: String,
    val birthDate: String,
    val password: String, // Note: In a real app, hash passwords!
    val isAdmin: Boolean = false
)