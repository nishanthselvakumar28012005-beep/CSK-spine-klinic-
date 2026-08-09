package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
  primary = GoldAccent,
  onPrimary = NavyPrimary,
  primaryContainer = NavyLight,
  onPrimaryContainer = Color.White,
  secondary = RedAccent,
  onSecondary = Color.White,
  tertiary = GoldDark,
  background = NavyPrimary,
  onBackground = Color.White,
  surface = NavyLight,
  onSurface = Color.White,
  surfaceVariant = NavyPrimary,
  onSurfaceVariant = Color(0xFFCBD5E1),
  outline = Color(0xFF475569)
)

private val LightColorScheme = lightColorScheme(
  primary = NavyPrimary,
  onPrimary = Color.White,
  primaryContainer = GoldAccent,
  onPrimaryContainer = NavyPrimary,
  secondary = GoldDark,
  onSecondary = Color.White,
  tertiary = RedAccent,
  onTertiary = Color.White,
  background = SlateBackground,
  onBackground = NavyPrimary,
  surface = SlateSurface,
  onSurface = NavyPrimary,
  surfaceVariant = Color(0xFFF1F5F9),
  onSurfaceVariant = Color(0xFF475569),
  outline = SlateBorder
)

@Composable
fun SpineKlinicTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}

