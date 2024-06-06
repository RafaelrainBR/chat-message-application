package com.rafaelrain.message.backend.application.ktor.plugins

import com.rafaelrain.message.backend.room.storage.RoomStorage
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun Application.configureKoin() {
    install(Koin) {
        modules(roomModule())
    }
}

private fun roomModule() =
    module {
        single { RoomStorage() }
    }
