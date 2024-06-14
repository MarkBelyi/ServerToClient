package com.example.client.Manager

import android.util.Log
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.plugins.*
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
        install(HttpTimeout) {
            connectTimeoutMillis = 15000
            requestTimeoutMillis = 15000
            socketTimeoutMillis = 15000
        }
    }
    private var session: WebSocketSession? = null

    suspend fun connect(ip: String, port: Int) {
        withContext(Dispatchers.IO) {
            try {
                session = client.webSocketSession {
                    url("ws://$ip:$port/ws")
                }
                Log.d("ClientManager", "Connected to ws://$ip:$port/ws")
            } catch (e: Exception) {
                Log.e("ClientManager", "Failed to connect to ws://$ip:$port/ws", e)
            }
        }
    }

    suspend fun disconnect() {
        withContext(Dispatchers.IO) {
            try {
                session?.close()
                session = null
                Log.d("ClientManager", "Disconnected")
            } catch (e: Exception) {
                Log.e("ClientManager", "Failed to disconnect", e)
            }
        }
    }

    suspend fun sendMessage(message: String) {
        withContext(Dispatchers.IO) {
            try {
                session?.send(Frame.Text(message))
                Log.d("ClientManager", "Message sent: $message")
            } catch (e: Exception) {
                Log.e("ClientManager", "Failed to send message", e)
            }
        }
    }
}

