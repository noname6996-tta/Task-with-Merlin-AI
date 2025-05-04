package com.example.taskwithmerlinai.screen.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taskwithmerlinai.ChatAiViewModel
import com.tta.chat_ai.ChatMessage
import com.tta.chat_ai.Logger
import com.tta.chat_ai.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    onBack: () -> Unit,
    chatAiViewModel: ChatAiViewModel = hiltViewModel()
) {
    val uiState by chatAiViewModel.uiState.collectAsState()
    val messages = chatAiViewModel.chatMessages

    var currentText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        chatAiViewModel.addMessage(chatAiViewModel.firstChat)
    }

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is UiState.Error -> {
                Logger.d("Error")
                chatAiViewModel.addMessage(ChatMessage(System.currentTimeMillis(), state.errorMessage, isFromMe = false))
            }

            is UiState.Success -> {
                Logger.d("Success \n ${state.outputText}")
                chatAiViewModel.addMessage(ChatMessage(System.currentTimeMillis(), state.outputText, isFromMe = false))
            }

            else -> Unit
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFEFEF))
    ) {
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

                Row {
                    when (uiState) {
                        is UiState.Loading -> {

                        }
                        is UiState.Success -> {

                        }
                        is UiState.Error -> {
                            Text(text = "SomeThing wrong", color = Color.Red)
                        }
                        UiState.Initial -> {

                        }
                    }
                }

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
                                chatAiViewModel.addMessage(ChatMessage(System.currentTimeMillis(), currentText, isFromMe = true))
                                chatAiViewModel.handleInput(currentText, "")
                                currentText = ""
                            }
                        }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Gửi")
                    }

            IconButton(onClick = { onBack.invoke() }) {
                Icon(Icons.Default.Clear, contentDescription = "Back")
            }
        }
    }
}