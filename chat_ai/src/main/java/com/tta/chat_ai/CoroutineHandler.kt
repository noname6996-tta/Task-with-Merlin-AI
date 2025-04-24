package com.tta.chat_ai

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers

object CoroutineHandler {
    private val coroutineExceptionHandler = CoroutineExceptionHandler() { _, throwable ->
        Logger.e(error = throwable)
    }

    val IOScope = Dispatchers.IO + coroutineExceptionHandler
    val MainScope = Dispatchers.Main + coroutineExceptionHandler
}
