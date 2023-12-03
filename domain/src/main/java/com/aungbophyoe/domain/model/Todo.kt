package com.aungbophyoe.domain.model

import androidx.annotation.Keep

@Keep
data class Todo(
    val id: Long = 0,
    val title: String,
    val isCompleted: Boolean = false,
    val date: String // Format: dd-MM-yyyy HH:mm:ss
)