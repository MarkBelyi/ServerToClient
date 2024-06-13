package com.example.server.ViewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.server.Manager.ServerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
        println("Start server button clicked.")
        viewModelScope.launch(Dispatchers.IO) {
            serverManager.startServer(port)
        }
    }

    fun stopServer() {
        println("Stop server button clicked.")
        viewModelScope.launch(Dispatchers.IO) {
            serverManager.stopServer()
        }
    }

    fun loadLogs() {
        viewModelScope.launch {
            val logs = serverManager.getAllLogs().map { it.message }
            _logs.value = logs
        }
    }
}
