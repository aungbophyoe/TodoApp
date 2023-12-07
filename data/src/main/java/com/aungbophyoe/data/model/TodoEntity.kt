package com.aungbophyoe.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todos")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val isCompleted: Boolean = false,
    val date: String, // Format: dd-MM-yyyy HH:mm:ss
    val isUpdated : Boolean = false
)