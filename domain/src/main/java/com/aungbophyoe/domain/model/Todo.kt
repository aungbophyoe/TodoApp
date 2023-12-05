package com.aungbophyoe.domain.model

import androidx.annotation.Keep

@Keep
data class Todo(
    val id: Long = 0,
    var title: String,
    var isCompleted: Boolean = false,
    var date: String // Format: dd-MM-yyyy HH:mm:ss
)