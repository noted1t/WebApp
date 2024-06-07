package com.doma

import com.doma.plugins.configureDatabases
import com.doma.plugins.configureHTTP
import com.doma.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
//Запуск netty-сервера
fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}
// конфигурация сервера, функций http, сериализацию и БД
fun Application.module() {
    configureHTTP()
    configureSerialization()
    configureDatabases()
}
