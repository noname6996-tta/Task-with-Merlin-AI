package com.example.taskwithmerlinai.screen.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskwithmerlinai.ChatAiViewModel
import com.example.taskwithmerlinai.model.ChatMessage
import com.tta.chat_ai.Logger
import com.tta.chat_ai.UiState

@Composable
fun ChatScreen(
    chatAiViewModel: ChatAiViewModel = viewModel()
) {
    val uiState by chatAiViewModel.uiState.collectAsState()
    val messages = remember { mutableStateListOf<ChatMessage>() }
    var currentText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFEFEF))
    ) {
        when (val state = uiState) {
            is UiState.Loading -> {
                Logger.d("Loading")
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            is UiState.Error -> {
                Logger.d("Error")
                messages.add(ChatMessage(System.currentTimeMillis(), state.errorMessage, isFromMe = false))

            }

            is UiState.Success -> {
                Logger.d("Success")
                messages.add(ChatMessage(System.currentTimeMillis(), state.outputText, isFromMe = false))
            }

            else -> {}
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            reverseLayout = true
        ) {
            itemsIndexed(messages.reversed()) { _, message ->
                ChatBubble(message)
                Spacer(modifier = Modifier.height(4.dp))
            }
        }

        Divider()

        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = currentText,
                onValueChange = { currentText = it },
                modifier = Modifier
                    .weight(1f)
                    .background(Color.White, shape = RoundedCornerShape(16.dp)),
                placeholder = { Text("Nhập tin nhắn...") },
                shape = RoundedCornerShape(16.dp),
                maxLines = 3,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White
                )
            )

            IconButton(
                onClick = {
                    if (currentText.isNotBlank()) {
                        messages.add(ChatMessage(System.currentTimeMillis(), currentText, isFromMe = true))
                        chatAiViewModel.sendPrompt(currentText)
                        currentText = ""
                    }
                }
            ) {
                Icon(Icons.Default.Send, contentDescription = "Gửi")
            }
        }
    }
}