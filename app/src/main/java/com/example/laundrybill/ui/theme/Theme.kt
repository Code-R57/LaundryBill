package com.example.laundrybill.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@SuppressLint("ConflictingOnColor")
private val DarkColorPalette = darkColors(
    primary = Purple500,
    primaryVariant = Purple500,
    secondary = Purple200,
    background = BackgroundDark,
    onBackground = Color.White,
    onSecondary = Color.LightGray,
    surface = Color.DarkGray,
    onSurface = Color.White
)

@SuppressLint("ConflictingOnColor")
private val LightColorPalette = lightColors(
    primary = Purple700,
    primaryVariant = Purple500,
    secondary = Purple500,
    background = BackgroundLight,
    onBackground = Color.Black,
    onSecondary = Color.Black,
    surface = CardLight,
    onSurface = Color.Black
)

@Composable
fun LaundryBillTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}