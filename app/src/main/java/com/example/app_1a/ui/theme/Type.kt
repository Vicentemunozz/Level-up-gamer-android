package com.example.app_1a.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// NOTA IMPORTANTE: Para usar las fuentes Orbitron y Roboto, debes:
// 1. Descargar los archivos .ttf de Google Fonts.
// 2. Crear una carpeta `font` dentro de `app/src/main/res/`.
// 3. Copiar los archivos .ttf a `res/font/`.
// 4. Crear un archivo `font_family.xml` en `res/font/` para definir las familias.
// Como no puedo agregar archivos, aquí se usa `FontFamily.Default` como sustituto.
// Te dejo el ejemplo de cómo se haría:
// val orbitronFamily = FontFamily(Font(R.font.orbitron_regular))
// val robotoFamily = FontFamily(Font(R.font.roboto_regular))

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.Default, // Reemplazar con orbitronFamily
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp,
        lineHeight = 56.sp,
        letterSpacing = 0.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default, // Reemplazar con orbitronFamily
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default, // Reemplazar con orbitronFamily
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default, // Reemplazar con robotoFamily
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default, // Reemplazar con robotoFamily
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default, // Reemplazar con robotoFamily
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)
