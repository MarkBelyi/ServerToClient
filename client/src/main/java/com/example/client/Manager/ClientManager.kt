package com.example.client.Manager

import io.ktor.client.*
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.url
import io.ktor.serialization.kotlinx.json.*
import io.ktor.websocket.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClientManager @Inject constructor() {
    private val client = HttpClient(CIO) {
        install(WebSockets)
        install(ContentNegotiation) {
            json()
        }
    }
    private var session: WebSocketSession? = null

    suspend fun connect(ip: String, port: Int) {
        withContext(Dispatchers.IO) {
            session = client.webSocketSession {
                url("ws://$ip:$port/ws")
            }
        }
    }

    suspend fun disconnect() {
        withContext(Dispatchers.IO) {
            session?.close()
            session = null
        }
    }

    suspend fun sendMessage(message: String) {
        withContext(Dispatchers.IO) {
            session?.send(Frame.Text(message))
        }
    }
}