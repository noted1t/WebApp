package com.ptrby.webapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.ptrby.webapp.base.mainscreen.MainScreen
import com.ptrby.webapp.theme.AppTheme
import io.ktor.client.engine.*

@Composable
internal fun App(seedColor: Color) = AppTheme(seedColor) {
    MainScreen()
}

internal expect fun openUrl(url: String?)
internal expect fun provideKtorEngine(): HttpClientEngineFactory<HttpClientEngineConfig>