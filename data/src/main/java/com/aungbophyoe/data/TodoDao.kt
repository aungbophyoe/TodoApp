package com.aungbophyoe.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.aungbophyoe.data.model.TodoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Query("SELECT * FROM todos")
    fun getAllTodos(): Flow<List<TodoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: TodoEntity)

    @Update
    suspend fun updateTodo(todo: TodoEntity)

    @Delete
    suspend fun deleteTodo(todo: TodoEntity)

    @Query("SELECT * FROM todos WHERE LOWER(title) LIKE LOWER(:searchQuery)")
    fun searchTodos(searchQuery: String): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todos WHERE LOWER(title) LIKE LOWER(:searchQuery) AND isCompleted = 1")
    fun searchCompletedTodos(searchQuery: String): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todos WHERE LOWER(title) LIKE LOWER(:searchQuery) AND isCompleted = 0")
    fun searchUncompletedTodos(searchQuery: String): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todos WHERE isCompleted = 1")
    fun getCompletedTodos(): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todos WHERE isCompleted = 0")
    fun getUncompletedTodos(): Flow<List<TodoEntity>>
}