package com.example.taskwithmerlinai.data.respository

import com.example.taskwithmerlinai.data.db.TaskDao
import com.example.taskwithmerlinai.data.model.TaskSendAI
import javax.inject.Inject
import kotlin.text.insert

class TaskRepository @Inject constructor(private val dao: TaskDao) : TaskRepositoryImpl{
    override suspend fun insertTask(taskDetail: TaskSendAI) {
        return dao.insert(taskDetail)
    }
}