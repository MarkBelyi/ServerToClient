package com.example.server.ViewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.server.Manager.ServerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServerViewModel @Inject constructor(
    private val serverManager: ServerManager
) : ViewModel() {

    private val _logs = MutableStateFlow<List<String>>(emptyList())
    val logs: StateFlow<List<String>> = _logs

    fun startServer(port: Int) {
        serverManager.startServer(port)
    }

    fun stopServer() {
        serverManager.stopServer()
    }

    fun loadLogs() {
        viewModelScope.launch {
            val logEntries = serverManager.getAllLogs()
            _logs.value = logEntries.map { it.message }
        }
    }
}