package com.stealthlynk.client.android.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Define colors to match StealthLynk iOS app
val Primary = Color(0xFF10b981) // teal/green
val Accent = Color(0xFF059669) // darker teal/green
val Background = Color(0xFF0c0c16) // very dark navy/black
val SecondaryBg = Color(0xFF191924) // slightly lighter dark shade
val BorderColor = Color(0xFF252532) // border color
val TextColor = Color(0xFFFFFFFF) // white
val SuccessColor = Color(0xFF10b981) // same as primary
val ErrorColor = Color(0xFFf44336) // red
val WarningColor = Color(0xFFff9800) // orange

// Dark color scheme (we'll use dark theme only to match iOS app)
private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    secondary = Accent,
    background = Background,
    surface = SecondaryBg,
    error = ErrorColor,
    onPrimary = TextColor,
    onSecondary = TextColor,
    onBackground = TextColor,
    onSurface = TextColor,
    onError = TextColor
)

@Composable
fun StealthLynkTheme(
    darkTheme: Boolean = true, // Always use dark theme to match iOS app
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
