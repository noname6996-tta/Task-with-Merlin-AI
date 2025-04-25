package com.example.taskwithmerlinai.data.model

data class ChatMessage(
    val id: Long,
    val text: String,
    val isFromMe: Boolean
)