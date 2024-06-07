package com.doma.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*

fun Application.configureHTTP() {
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowCredentials = true
        allowHeader(HttpHeaders.Authorization)
        allowHeader("user_session")
        allowHeader(HttpHeaders.ContentType)
        anyHost()
    }
}
