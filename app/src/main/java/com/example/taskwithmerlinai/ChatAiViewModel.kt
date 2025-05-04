package com.example.taskwithmerlinai

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import com.tta.chat_ai.ChatMessage
import com.example.taskwithmerlinai.data.model.TaskSendAI
import com.example.taskwithmerlinai.data.respository.TaskRepository
import com.tta.chat_ai.AbsChatAiViewModel
import com.tta.chat_ai.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatAiViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : AbsChatAiViewModel() {

    val firstMessage = "Hi there! I'm Merlin, your virtual assistant for the Todo List app. I can help you add new tasks, update existing ones, or give you a summary of your tasks, including completed, pending, and overdue items. Just tell me what you'd like to do!"
    val firstChat = ChatMessage(0, firstMessage, false)

    private val _chatMessages = mutableStateListOf<ChatMessage>()
    val chatMessages: List<ChatMessage> get() = _chatMessages

    fun addMessage(message: ChatMessage) {
        _chatMessages.add(message)
    }

    fun clearMessages() {
        _chatMessages.clear()
    }

    fun convertMessagesToPrompt(messages: List<ChatMessage>): String {
        return buildString {
            messages.forEach { msg ->
                val role = if (msg.isFromMe) "user" else "assistant"
                appendLine("$role: ${msg.text}")
            }
        }
    }

    private fun defaultPrompt(
        userPrompt: String,
        dataChat: String? = null,
        dataFromDb: String? = null
    ): String {
        return buildString {
            appendLine("You are Merlin, a virtual assistant for a Todo List app. Your job is to help users manage their tasks effectively.")

            dataFromDb?.let {
                appendLine("Here are the current operations in the database. You must read this data to be able to answer most accurately:")
                appendLine(it)
                appendLine()
            }

            dataChat?.let {
                appendLine("This is the chat data that you and the user talked to each other, read this data and make inferences:")
                appendLine(it)
                appendLine()
            }

            appendLine("You must classify the user's input into one of the following actions:")
            appendLine("- add-task: when the user wants to create a new task.")
            appendLine("- update-task: when the user wants to modify an existing task.")
            appendLine("- summarize-tasks: when the user wants an overview of their tasks.")
            appendLine("- unknown: if the input doesn’t match the above actions, reply politely that you only support these features.")

            appendLine("This is the latest user message: \"$userPrompt\"")

            appendLine()
            appendLine("Your response must be a JSON string in the following format:")
            appendLine("{")
            appendLine("  action: \"add-task\" | \"update-task\" | \"summarize-tasks\" | \"unknown\",")
            appendLine("  data: {")
            appendLine("    // For add-task: { title: string, dueDate: string, setNotification: boolean }")
            appendLine("    // For update-task: { idTask: string, title?: string, dueDate?: string, setNotification?: boolean }")
            appendLine("    // For summarize-tasks: { completedCount: number, pendingCount: number, overdueCount: number }")
            appendLine("    // For unknown: {}")
            appendLine("  },")
            appendLine("  message: \"A friendly message to the user\"")
            appendLine("}")
        }
    }


    internal fun handleInput(userPrompt: String, dataFromDb: String? = null) {
        sendPrompt(defaultPrompt(userPrompt, convertMessagesToPrompt(chatMessages), dataFromDb))
        Logger.d(convertMessagesToPrompt(chatMessages))
    }

    fun insertTask(taskDetail: TaskSendAI) {
        viewModelScope.launch {
            taskRepository.insertTask(taskDetail)
        }
    }
}