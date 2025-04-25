package com.example.taskwithmerlinai.data.respository

import com.example.taskwithmerlinai.data.model.TaskSendAI

interface TaskRepositoryImpl {
    suspend fun insertTask(taskDetail: TaskSendAI)
}