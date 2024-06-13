package com.example.client.Screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.client.ViewModel.ClientViewModel


@Composable
fun ClientApp(viewModel: ClientViewModel = hiltViewModel()) {
    var ip by remember { mutableStateOf(TextFieldValue("192.168.1.100")) }
    var port by remember { mutableStateOf(TextFieldValue("5986")) }
    var isConnected by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        OutlinedTextField(
            value = ip,
            onValueChange = { ip = it },
            label = { Text("Server IP") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = port,
            onValueChange = { port = it },
            label = { Text("Server Port") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (isConnected) {
                viewModel.disconnect()
            } else {
                viewModel.connect(ip.text, port.text.toInt())
            }
            isConnected = !isConnected
        },
            modifier = Modifier.fillMaxWidth(0.8f).height(45.dp)
        ) {
            Text(if (isConnected) "Disconnect" else "Connect")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            viewModel.sendMessage("CHROME_OPENED")
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("http://www.google.com")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        },
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text("Start")
        }
    }
}
