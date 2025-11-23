package com.example.app_1a.util

import android.util.Patterns

object Validator {

    fun validateEmail(email: String): Boolean {
        return if (email.isBlank()) {
            false
        } else {
            // Nota: En pruebas unitarias puras, Patterns.EMAIL_ADDRESS a veces da problemas
            // porque es parte de Android. Usaremos una regex simple para el test.
            val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
            emailRegex.matches(email)
        }
    }

    fun validatePassword(pass: String): Boolean {
        return pass.isNotBlank() && pass.length >= 6
    }

    fun validatePrice(price: Double): Boolean {
        return price > 0
    }
}