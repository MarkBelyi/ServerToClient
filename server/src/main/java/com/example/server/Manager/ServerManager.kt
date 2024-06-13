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
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
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
            server = embeddedServer(Netty, port = port) {
                install(WebSockets)
                install(ContentNegotiation) {
                    json()
                }
                routing {
                    webSocket("/ws") {
                        handleWebSocket(this)
                    }
                }
            }
            server?.start(wait = false)
        }
    }

    private suspend fun handleWebSocket(session: WebSocketSession) {
        try {
            for (frame in session.incoming) {
                if (frame is Frame.Text) {
                    val receivedText = frame.readText()
                    saveLog(receivedText)
                    handleClientMessage(session, receivedText)
                }
            }
        } finally {
            session.close(CloseReason(CloseReason.Codes.NORMAL, "Client disconnected"))
        }
    }

    private fun handleClientMessage(session: WebSocketSession, message: String) {
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
            "CHROME_OPENED" -> {
                Log.d("ServerManager", "Chrome is opened on client")
                startSendingGestures(session)
            }
            else -> {
                Log.d("ServerManager", "Unknown command received: $message")
            }
        }
    }

    private fun startSendingGestures(session: WebSocketSession) {
        serverScope.launch {
            while (true) {
                val gestures = listOf("SWIPE_UP", "SWIPE_DOWN", "SWIPE_RIGHT", "SWIPE_LEFT")
                gestures.forEach { gesture ->
                    session.send(Frame.Text(gesture))
                    saveLog("Sent gesture command: $gesture")
                    delay(2000)
                }
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
    }

    suspend fun getAllLogs(): List<LogEntry> {
        return withContext(Dispatchers.IO) {
            database.logDao().getAllLogs()
        }
    }
}
