package com.aungbophyoe.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aungbophyoe.data.model.TodoEntity

@Database(entities = [TodoEntity::class],version = 2,exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {
    companion object {
        const val dbName = "todo_database"
    }
    abstract fun getTodoDao() : TodoDao
}