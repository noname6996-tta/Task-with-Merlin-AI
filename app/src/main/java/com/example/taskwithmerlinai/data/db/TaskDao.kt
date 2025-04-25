package com.example.taskwithmerlinai.data.db

import androidx.room.Dao
import com.example.taskwithmerlinai.data.model.TaskSendAI

@Dao
interface TaskDao : BaseDao<TaskSendAI> {

}