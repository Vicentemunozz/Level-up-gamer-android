package com.example.app_1a.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = ElectricBlue,
    secondary = NeonGreen,
    background = GamerBlack,
    surface = GamerBlack,
    onPrimary = TextWhite,
    onSecondary = TextWhite,
    onBackground = TextWhite,
    onSurface = TextWhite,
)

@Composable
fun App_1ATheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography
    ) {
        // Envolvemos todo en un Surface para asegurar que el fondo se aplique globalmente.
        Surface(color = colorScheme.background) {
            content()
        }
    }
}

