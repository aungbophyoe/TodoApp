package com.aungbophyoe.domain.repo

import com.aungbophyoe.data.TodoDao
import com.aungbophyoe.data.model.TodoEntity
import com.aungbophyoe.data.repo.TodoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(private val todoDao : TodoDao) : TodoRepository {
    override suspend fun insert(todo: TodoEntity) {
        todoDao.insertTodo(todo)
    }

    override suspend fun update(todo: TodoEntity) {
        todoDao.updateTodo(todo)
    }

    override suspend fun delete(todo: TodoEntity) {
        todoDao.deleteTodo(todo)
    }

    override suspend fun getAllTodos(): Flow<List<TodoEntity>> {
        return todoDao.getAllTodos()
    }

    override suspend fun searchTodos(query: String): Flow<List<TodoEntity>> {
        return todoDao.searchTodos(query)
    }

    override suspend fun searchCompletedTodos(query: String): Flow<List<TodoEntity>> {
        return todoDao.searchCompletedTodos(query)
    }

    override suspend fun searchUnCompletedTodos(query: String): Flow<List<TodoEntity>> {
        return todoDao.searchUncompletedTodos(query)
    }

    override suspend fun getCompletedTodos(): Flow<List<TodoEntity>> {
        return todoDao.getCompletedTodos()
    }

    override suspend fun getUncompletedTodos(): Flow<List<TodoEntity>> {
        return todoDao.getUncompletedTodos()
    }
}