package com.ptrby.webapp.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.materialkolor.DynamicMaterialTheme
import com.materialkolor.PaletteStyle
//Хранит состояние темы
internal val LocalThemeIsDark = compositionLocalOf { mutableStateOf(true) }
//Задает тему приложения, использует Material design если это возможно
@Composable
internal fun AppTheme(
    seedColor: Color,
    paletteStyle: PaletteStyle = PaletteStyle.TonalSpot,
    content: @Composable () -> Unit
) {
    val systemIsDark = isSystemInDarkTheme()
    val isDarkState = remember { mutableStateOf(systemIsDark) }
    CompositionLocalProvider(
        LocalThemeIsDark provides isDarkState
    ) {
        val isDark by isDarkState
        SystemAppearance(!isDark)
        MaterialTheme(
            content = {
                DynamicMaterialTheme(
                    animate = true,
                    seedColor = seedColor,
                    useDarkTheme = isDark,
                    style = paletteStyle,
                    content = { Surface(content = content) }
                )
            }
        )
    }
}

@Composable
internal expect fun SystemAppearance(isDark: Boolean)
