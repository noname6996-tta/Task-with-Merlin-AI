package com.example.taskwithmerlinai.data.model

import kotlinx.serialization.json.Json

object Wrapper {
    fun String.wrapperToTaskDetails(): TaskSendAI {
        return try {
            Json.decodeFromString<TaskWrapper>(this).data
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid JSON format for TaskWrapper: ${e.message}", e)
        }
    }
}