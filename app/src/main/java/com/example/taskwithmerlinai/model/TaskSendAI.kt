package com.example.taskwithmerlinai.model

data class TaskSendAI(
    var idTask: String? = null,
    var titleTask: String? = null,
    var dueDate: String? = null,
    var setNotification : Boolean = false
)