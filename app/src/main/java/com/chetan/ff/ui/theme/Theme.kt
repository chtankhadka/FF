package com.chetan.ff.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = dark_primary,
    onPrimary = dark_onprimary,
    primaryContainer = dark_primaryContainer,
    onPrimaryContainer = dark_onprimaryContainer,

    secondary = dark_secondary,
    secondaryContainer = dark_secondaryContainer,
    onSecondary = dark_onsecondary,
    onSecondaryContainer = dark_onsecondaryContainer,

    tertiary = dark_tertairy,
    onTertiary = dark_ontertairy,
    tertiaryContainer = dark_tertairyContainer,
    onTertiaryContainer = dark_ontertairyContainer,

//    background = ,
//    surface =,
//    onBackground = ,
//    onSurface =
)

private val LightColorScheme = lightColorScheme(
    primary = light_primary,
    onPrimary = light_onprimary,
    primaryContainer = light_primaryContainer,
    onPrimaryContainer = light_onprimaryContainer,

    secondary = light_secondary,
    secondaryContainer = light_secondaryContainer,
    onSecondary = light_onsecondary,
    onSecondaryContainer = light_onsecondaryContainer,

    tertiary = light_tertairy,
    onTertiary = light_ontertairy,
    tertiaryContainer = light_tertairyContainer,
    onTertiaryContainer = light_ontertairyContainer,
//    background = ,
//    surface = ,
//    onBackground = ,
//    onSurface =
)

@Composable
fun FFTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
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
        typography = Typography,
        content = content
    )
}