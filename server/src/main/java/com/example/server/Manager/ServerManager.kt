package com.example.server.Manager

import android.content.Context
import android.util.Log
import com.example.server.Database.LogDatabase
import com.example.server.Database.LogEntry
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServerManager @Inject constructor(
    private val context: Context,
    private val database: LogDatabase
) {
    private val serverScope = CoroutineScope(Dispatchers.IO)
    private var server: NettyApplicationEngine? = null

    fun startServer(port: Int) {
        serverScope.launch {
            try {
                server = embeddedServer(Netty, host = "0.0.0.0", port = port) {
                    install(WebSockets)
                    install(ContentNegotiation) {
                        json()
                    }
                    routing {
                        webSocket("/ws") {
                            try {
                                for (frame in incoming) {
                                    if (frame is Frame.Text) {
                                        val receivedText = frame.readText()
                                        saveLog(receivedText)
                                        handleClientMessage(receivedText)
                                    }
                                }
                            } finally {
                                close(CloseReason(CloseReason.Codes.NORMAL, "Client disconnected"))
                            }
                        }
                    }
                }
                server?.start(wait = false)
                Log.d("ServerManager", "Server started on port $port")
            } catch (e: Exception) {
                Log.e("ServerManager", "Failed to start server", e)
            }
        }
    }

    private fun handleClientMessage(message: String) {
        when (message) {
            "SWIPE_UP" -> {
                Log.d("ServerManager", "Handling SWIPE_UP")
                performSwipeUp()
            }
            "SWIPE_DOWN" -> {
                Log.d("ServerManager", "Handling SWIPE_DOWN")
                performSwipeDown()
            }
            "SWIPE_RIGHT" -> {
                Log.d("ServerManager", "Handling SWIPE_RIGHT")
                performSwipeRight()
            }
            "SWIPE_LEFT" -> {
                Log.d("ServerManager", "Handling SWIPE_LEFT")
                performSwipeLeft()
            }
            else -> {
                Log.d("ServerManager", "Unknown command received: $message")
            }
        }
    }

    private fun performSwipeUp() {
        Log.d("ServerManager", "Performing swipe up")
    }

    private fun performSwipeDown() {
        Log.d("ServerManager", "Performing swipe down")
    }

    private fun performSwipeRight() {
        Log.d("ServerManager", "Performing swipe right")
    }

    private fun performSwipeLeft() {
        Log.d("ServerManager", "Performing swipe left")
    }

    private suspend fun saveLog(message: String) {
        withContext(Dispatchers.IO) {
            database.logDao().insert(LogEntry(0, System.currentTimeMillis(), message))
        }
    }

    fun stopServer() {
        serverScope.coroutineContext.cancelChildren()
        server?.stop(1000, 1000)
        Log.d("ServerManager", "Server stopped")
    }

    suspend fun getAllLogs(): List<LogEntry> {
        return withContext(Dispatchers.IO) {
            database.logDao().getAllLogs()
        }
    }
}
