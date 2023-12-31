package com.aungbophyoe.domain.usecase

import com.aungbophyoe.data.model.TodoEntity
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

    suspend fun getCompletedTodos() : Flow<List<Todo>> {
        val data = repository.getCompletedTodos().map { entites ->
            entites.map {
                mapper.mapFromEntity(it)
            }
        }
        return data
    }

    suspend fun getUnCompletedTodos() : Flow<List<Todo>> {
        val data = repository.getUncompletedTodos().map { entites ->
            entites.map {
                mapper.mapFromEntity(it)
            }
        }
        return data
    }

    suspend fun searchTodos(query : String) : Flow<List<Todo>> {
        val searchQuery = "%$query%"
        val data = repository.searchTodos(query = searchQuery).map { entites ->
            entites.map {
                mapper.mapFromEntity(it)
            }
        }
        return data
    }

    suspend fun searchCompletedTodos(query:String): Flow<List<Todo>> {
        val searchQuery = "%$query%"
        val data = repository.searchCompletedTodos(query = searchQuery).map { entites ->
            entites.map {
                mapper.mapFromEntity(it)
            }
        }
        return data
    }
    suspend fun searchUnCompletedTodos(query:String): Flow<List<Todo>> {
        val searchQuery = "%$query%"
        val data = repository.searchUnCompletedTodos(query = searchQuery).map { entites ->
            entites.map {
                mapper.mapFromEntity(it)
            }
        }
        return data
    }
}