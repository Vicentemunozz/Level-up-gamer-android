package com.example.app_1a.data.api

import com.example.app_1a.util.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    // Variable donde guardamos el token al hacer login
    var authToken: String? = null

    // Configuración del Cliente HTTP
    private val client = OkHttpClient.Builder()
        // Tiempos de espera largos para Render (Plan Gratis)
        .connectTimeout(2, TimeUnit.MINUTES)
        .readTimeout(2, TimeUnit.MINUTES)
        .writeTimeout(2, TimeUnit.MINUTES)

        // 🟢 INTERCEPTOR: Esto es lo que te falta o está fallando
        // Se encarga de pegar el token en la cabecera de cada petición
        .addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()

            // Si tenemos un token guardado, lo enviamos
            if (authToken != null) {
                requestBuilder.header("Authorization", "Bearer $authToken")
            }

            val request = requestBuilder.build()
            chain.proceed(request)
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .client(client) // Usamos nuestro cliente configurado
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}