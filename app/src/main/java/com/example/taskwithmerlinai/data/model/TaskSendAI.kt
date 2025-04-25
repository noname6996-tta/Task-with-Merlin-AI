package com.example.taskwithmerlinai.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.taskwithmerlinai.data.Constants.TABLE_NAME_TASK

@Entity(tableName = TABLE_NAME_TASK)
data class TaskSendAI(
    @PrimaryKey(autoGenerate = true)
    var idTask: Int? = null,
    var titleTask: String? = null,
    var dueDate: String? = null,
    var setNotification : Boolean = false
)