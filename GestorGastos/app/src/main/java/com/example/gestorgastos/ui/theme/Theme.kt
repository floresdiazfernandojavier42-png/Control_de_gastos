package com.example.gestorgastos.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Esquema = lightColorScheme(
    primary = Verde,
    onPrimary = Color.White,
    primaryContainer = VerdeClaro,
    onPrimaryContainer = Verde,
    background = Fondo,
    surface = Color.White,
    error = Rojo
)

@Composable
fun GestorGastosTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = Esquema, typography = Typografia, content = content)
}
