package com.ptrby.webapp

import io.ktor.client.engine.*
import io.ktor.client.engine.js.*
import kotlinx.browser.window
//Открывает url сайта
internal actual fun openUrl(url: String?) {
    url?.let { window.open(it) }
}
//Возвращает фабрику Ktor, чтобы использовать в Java Script
internal actual fun provideKtorEngine(): HttpClientEngineFactory<HttpClientEngineConfig> {
    return Js
}