package com.example.app_1a.data.api

// Modelo para enviar al Login
data class LoginRequest(
    val username: String,
    val password: String
)

// Modelo para enviar al Registro
data class RegisterRequest(
    val username: String,
    val password: String,
    val birthDate: String
)

// Modelo de respuesta (lo que nos devuelve Spring Boot con el token)
data class AuthResponse(
    val token: String
)