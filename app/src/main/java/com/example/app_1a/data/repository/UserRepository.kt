package com.example.app_1a.data.repository


import android.util.Log // <--- IMPORTANTE
import com.example.app_1a.data.api.LoginRequest
import com.example.app_1a.data.api.RegisterRequest
import com.example.app_1a.data.api.AuthResponse
import com.example.app_1a.data.api.RetrofitClient
import com.example.app_1a.data.db.entity.User

class UserRepository {

    suspend fun login(email: String, pass: String): Result<AuthResponse> {
        return try {
            val request = LoginRequest(username = email, password = pass)
            val response = RetrofitClient.apiService.login(request)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                // 🔴 IMPRIMIR ERROR DE RESPUESTA
                Log.e("API_TEST", "Error Login Code: ${response.code()} Body: ${response.errorBody()?.string()}")
                Result.failure(Exception("Error de Login: ${response.code()}"))
            }
        } catch (e: Exception) {
            // 🔴 IMPRIMIR ERROR DE CONEXIÓN
            Log.e("API_TEST", "Error Login Exception: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun register(user: User): Result<AuthResponse> {
        return try {
            val request = RegisterRequest(
                username = user.email,
                password = user.pass,
                birthDate = user.birthDate
            )
            val response = RetrofitClient.apiService.register(request)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                // 🔴 IMPRIMIR ERROR DE RESPUESTA
                Log.e("API_TEST", "Error Register Code: ${response.code()} Body: ${response.errorBody()?.string()}")
                Result.failure(Exception("Error de Registro: ${response.code()}"))
            }
        } catch (e: Exception) {
            // 🔴 IMPRIMIR ERROR DE CONEXIÓN
            Log.e("API_TEST", "Error Register Exception: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }

    // Métodos antiguos (los dejamos para que no rompa el resto de la app por ahora,
    // pero ya no se usan para la autenticación real)
    suspend fun findUserByEmail(email: String): User? { return null }
    suspend fun insertUser(user: User) { }
    suspend fun updateUser(user: User) { }
    suspend fun findUserById(userId: Int): User? { return null }
}