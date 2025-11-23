package com.example.app_1a.data.api

import com.example.app_1a.data.db.entity.Product
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @GET("api/products")
    suspend fun getProducts(): List<Product>

    @POST("api/products")
    suspend fun createProduct(@Body product: Product): Product

    // 🟢 NUEVO: Modificar producto
    @PUT("api/products/{id}")
    suspend fun updateProduct(@Path("id") id: Int, @Body product: Product): Product

    // 🟢 NUEVO: Eliminar producto
    @DELETE("api/products/{id}")
    suspend fun deleteProduct(@Path("id") id: Int): Response<Unit>
}