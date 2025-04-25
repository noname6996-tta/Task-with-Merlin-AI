package com.example.taskwithmerlinai

import androidx.lifecycle.viewModelScope
import com.example.taskwithmerlinai.data.model.TaskSendAI
import com.example.taskwithmerlinai.data.respository.TaskRepository
import com.tta.chat_ai.AbsChatAiViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatAiViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : AbsChatAiViewModel() {

    private fun defaultPrompt(
        userPrompt: String,
        dataChat: String? = null,
        dataFromDb: String? = null
    ): String {
        return buildString {
            appendLine("You are an AI assistant in a smart ToDo list application.")
            appendLine("Your job is to classify the user's input into one of the following actions:")
            appendLine("- add-task: when the user wants to add a new task")
            appendLine("- update-task: when the user wants to change an existing task")
            appendLine("- summarize-tasks: when the user wants a summary of tasks")
            appendLine()
            appendLine("Read the user's input and respond in JSON format like:")
            appendLine("{")
            appendLine("  \"action\": \"add-task\" | \"update-task\" | \"summarize-tasks\",")
            appendLine("  \"data\": {")
            appendLine("    // If action is \"add-task\":")
            appendLine("    // { title: string, dueDate: string, setNotification: boolean }")
            appendLine()
            appendLine("    // If action is \"update-task\":")
            appendLine("    // { idTask: string, title: string (optional), dueDate: string (optional), setNotification: boolean (optional) }")
            appendLine()
            appendLine("    // If action is \"summarize-tasks\":")
            appendLine("    // { completedCount: number, pendingCount: number, overdueCount: number }")
            appendLine("  }")
            appendLine("}")
            appendLine()
            appendLine("User input: \"$userPrompt\"")
            dataChat?.let {
                appendLine("Current chat: $it")
            }
            dataFromDb?.let {
                appendLine("Data from db $it")
            }
        }
    }


    internal fun handleInput(userPrompt: String, dataChat: String? = null, dataFromDb: String? = null) {
        sendPrompt(defaultPrompt(userPrompt, dataChat, dataFromDb))
    }

    fun insertTask(taskDetail: TaskSendAI) {
        viewModelScope.launch {
            taskRepository.insertTask(taskDetail)
        }
    }
}