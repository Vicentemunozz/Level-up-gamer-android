package com.example.app_1a.data.api.external

data class CurrencyResponse(
    val amount: Double,
    val base: String,
    val date: String,
    val rates: Map<String, Double> // Aquí vendrá el valor: "USD": 0.0012
)