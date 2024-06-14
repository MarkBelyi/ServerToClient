package com.example.client.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.client.Manager.ClientManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch


@HiltViewModel
class ClientViewModel @Inject constructor(
    private val clientManager: ClientManager
) : ViewModel() {

    fun connect(ip: String, port: Int) {
        viewModelScope.launch {
            clientManager.connect(ip, port)
        }
    }

    fun disconnect() {
        viewModelScope.launch {
            clientManager.disconnect()
        }
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            clientManager.sendMessage(message)
        }
    }
}