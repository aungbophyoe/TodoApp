package com.aungbophyoe.domain.usecase

import com.aungbophyoe.data.repo.TodoRepository
import com.aungbophyoe.domain.mapper.TodoMapper
import com.aungbophyoe.domain.model.Todo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TodoUseCase @Inject constructor(private val repository: TodoRepository,private val mapper: TodoMapper) {
    suspend fun insert(todo : Todo) {
        repository.insert(mapper.mapToEntity(todo))
    }

    suspend fun update(todo: Todo) {
        repository.update(mapper.mapToEntity(todo))
    }

    suspend fun delete(todo: Todo) {
        repository.delete(mapper.mapToEntity(todo))
    }

    suspend fun getAllTodos(): Flow<List<Todo>> {
        val data = repository.getAllTodos().map { entites ->
            entites.map {
                mapper.mapFromEntity(it)
            }
        }
        return data
    }

    suspend fun searchTodos(query : String) : Flow<List<Todo>> {
        val data = repository.searchTodos(query = query).map { entites ->
            entites.map {
                mapper.mapFromEntity(it)
            }
        }
        return data
    }
}