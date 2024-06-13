package com.example.server.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.server.ViewModel.ServerViewModel

@Composable
fun ServerApp(viewModel: ServerViewModel = hiltViewModel()) {
    var port by remember { mutableStateOf("5986") }
    var isRunning by remember { mutableStateOf(false) }
    val logs by viewModel.logs.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        OutlinedTextField(
            value = port,
            onValueChange = {port = it},
            shape = RoundedCornerShape(16.dp),
            placeholder = {Text(text = "Write a port", color = Color.LightGray)},
            singleLine = true,
            maxLines = 1,
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        ElevatedButton(
            onClick = {
            if (isRunning) {
                viewModel.stopServer()
            } else {
                viewModel.startServer(port.toInt())
            }
            isRunning = !isRunning
        },
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(0.8f).height(45.dp)
        ) {
            Text(if (isRunning) "Stop" else "Start")
        }

        Spacer(modifier = Modifier.height(16.dp))

        ElevatedButton(onClick = {
                viewModel.loadLogs()
            },
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text("View Logs")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(200.dp)
        ) {
            items(logs) { log ->
                Text(text = log, color = colorScheme.onBackground)
                Spacer(modifier = Modifier.height(4.dp))
            }
        }

    }
}