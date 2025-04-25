package com.example.taskwithmerlinai.data.model

@Serializable
data class TaskWrapper(
    val action: String,
    val data: TaskData
)