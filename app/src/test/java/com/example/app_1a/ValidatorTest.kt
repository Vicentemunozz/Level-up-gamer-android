package com.example.app_1a

import com.example.app_1a.util.Validator
import org.junit.Test
import org.junit.Assert.*

class ValidatorTest {

    // PRUEBA 1: Verificar que un email válido sea aceptado
    @Test
    fun email_isCorrect() {
        val email = "admin@duoc.cl"
        val result = Validator.validateEmail(email)
        assertTrue("El email debería ser válido", result)
    }

    // PRUEBA 2: Verificar que una contraseña corta sea rechazada
    @Test
    fun password_isTooShort() {
        val password = "123" // Menos de 6 caracteres
        val result = Validator.validatePassword(password)
        assertFalse("La contraseña corta debería ser inválida", result)
    }

    // PRUEBA 3: Verificar que un precio negativo sea rechazado
    @Test
    fun price_isNegative() {
        val price = -5000.0
        val result = Validator.validatePrice(price)
        assertFalse("Un precio negativo no debe ser válido", result)
    }
}