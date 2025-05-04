package com.tta.chat_ai

data class ChatMessage(
    val id: Long,
    val text: String,
    val isFromMe: Boolean
)