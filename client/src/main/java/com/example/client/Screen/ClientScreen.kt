package com.example.client.Screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import com.example.client.ViewModel.ClientViewModel

@Composable
fun ClientApp(viewModel: ClientViewModel = hiltViewModel()) {
    var ip by remember { mutableStateOf(TextFieldValue("192.168.1.100")) }
    var port by remember { mutableStateOf(TextFieldValue("5986")) }
    var isConnected by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        BasicTextField(
            value = ip,
            onValueChange = { ip = it },
            decorationBox = { innerTextField ->
                Column {
                    Text("IP Address")
                    innerTextField()
                }
            }
        )

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
            if (isConnected) {
                viewModel.disconnect()
            } else {
                viewModel.connect(ip.text, port.text.toInt())
            }
            isConnected = !isConnected
        }) {
            Text(if (isConnected) "Disconnect" else "Connect")
        }

        Button(onClick = {
            viewModel.sendMessage("open_chrome")
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("http://www.google.com")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            it.context.startActivity(intent)
        }) {
            Text("Start")
        }
    }
}