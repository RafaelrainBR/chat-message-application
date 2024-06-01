package com.rafaelrain.chatmessage.backend

import com.rafaelrain.chatmessage.backend.plugins.configureSerialization
import com.rafaelrain.chatmessage.backend.plugins.configureSockets
import io.ktor.server.application.Application

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureSockets()
}
