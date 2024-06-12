package com.example.server.Screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.server.ViewModel.ServerViewModel

@Composable
fun ServerApp(viewModel: ServerViewModel = hiltViewModel()) {
    var port by remember { mutableStateOf(TextFieldValue("5986")) }
    var isRunning by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        BasicTextField(
            value = port,
            onValueChange = { port = it },
            decorationBox = { innerTextField ->
                Column {
                    Text("Port")
                    innerTextField()
                }
            }
        )

        Button(onClick = {
            if (isRunning) {
                viewModel.stopServer()
            } else {
                viewModel.startServer(port.text.toInt())
            }
            isRunning = !isRunning
        }) {
            Text(if (isRunning) "Stop" else "Start")
        }

        Button(onClick = {
            viewModel.loadLogs()
        }) {
            Text("View Logs")
        }

        val logs by viewModel.logs.collectAsState()
        logs.forEach {
            Text(text = it)
        }
    }
}