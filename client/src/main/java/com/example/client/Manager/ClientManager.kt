package com.example.client.Manager

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClientManager @Inject constructor() {
    private val client = HttpClient(CIO) {
        install(WebSockets)
        install(HttpTimeout) {
            requestTimeoutMillis = 30000 // 30 seconds
            connectTimeoutMillis = 30000 // 30 seconds
            socketTimeoutMillis = 30000 // 30 seconds
        }
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
