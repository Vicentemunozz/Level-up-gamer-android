package com.example.app_1a.data.api.external

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// 1. La Interfaz
interface CurrencyApi {
    @GET("latest")
    suspend fun getExchangeRate(
        @Query("from") from: String = "CLP",
        @Query("to") to: String = "USD"
    ): CurrencyResponse
}

// 2. El Cliente
object CurrencyClient {
    // Asegúrate de que termine en /
    private const val BASE_URL = "https://api.frankfurter.app/"

    val api: CurrencyApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrencyApi::class.java)
    }
}