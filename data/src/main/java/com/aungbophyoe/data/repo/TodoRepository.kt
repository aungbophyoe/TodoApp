package com.aungbophyoe.data.repo

import com.aungbophyoe.data.model.TodoEntity
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    suspend fun insert(todo: TodoEntity)
    suspend fun update(todo: TodoEntity)
    suspend fun delete(todo: TodoEntity)
    suspend fun getAllTodos() : Flow<List<TodoEntity>>

    suspend fun searchTodos(query:String): Flow<List<TodoEntity>>
    suspend fun searchCompletedTodos(query:String): Flow<List<TodoEntity>>
    suspend fun searchUnCompletedTodos(query:String): Flow<List<TodoEntity>>

    suspend fun getCompletedTodos() : Flow<List<TodoEntity>>
    suspend fun getUncompletedTodos() : Flow<List<TodoEntity>>
}