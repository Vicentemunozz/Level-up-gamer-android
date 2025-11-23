package com.example.app_1a.data.api.external

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// 1. La Interfaz de la API Externa
interface FreeToGameApi {
    @GET("api/games?sort-by=popularity") // Traemos los juegos populares
    suspend fun getPopularGames(): List<ExternalGame>
}

// 2. El Cliente Retrofit Exclusivo para esta API
object ExternalRetrofitClient {
    private const val BASE_URL = "https://www.freetogame.com/"

    val api: FreeToGameApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FreeToGameApi::class.java)
    }
}