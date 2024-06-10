package com.example.server.Manager

import android.content.Context
import androidx.room.Room
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.websocket.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServerManager @Inject constructor(context: Context) {

    private val serverScope = CoroutineScope(Dispatchers.IO)
    private var server: NettyApplicationEngine? = null

    // Initialize Room database
    private val database: LogDatabase = Room.databaseBuilder(
        context.applicationContext,
        LogDatabase::class.java, "log_database"
    ).build()

    fun startServer(port: Int) {
        serverScope.launch {
            server = embeddedServer(Netty, port = port) {
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
        }
    }

    private suspend fun handleClientMessage(message: String) {
        // Обработка сообщений от клиента
    }

    private suspend fun saveLog(message: String) {
        withContext(Dispatchers.IO) {
            database.logDao().insert(LogEntry(0, System.currentTimeMillis(), message))
        }
    }

    fun stopServer() {
        serverScope.launch {
            server?.stop(1000, 1000)
        }
    }

    suspend fun getAllLogs(): List<LogEntry> {
        return withContext(Dispatchers.IO) {
            database.logDao().getAllLogs()
        }
    }
}