package com.example.taskwithmerlinai.model

data class ChatMessage(
    val id: Long,
    val text: String,
    val isFromMe: Boolean
)