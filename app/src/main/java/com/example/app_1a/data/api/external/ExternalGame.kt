package com.example.app_1a.data.api.external

import com.google.gson.annotations.SerializedName

data class ExternalGame(
    val id: Int,
    val title: String,
    @SerializedName("thumbnail") val thumbnail: String, // La API usa "thumbnail" para la imagen
    @SerializedName("short_description") val description: String,
    val genre: String,
    val platform: String
)