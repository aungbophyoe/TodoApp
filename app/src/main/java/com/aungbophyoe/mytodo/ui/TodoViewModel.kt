package com.aungbophyoe.mytodo.ui

import android.util.Log
import com.aungbophyoe.arc.MVIViewModel
import com.aungbophyoe.domain.model.Todo
import com.aungbophyoe.domain.usecase.TodoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@HiltViewModel
class TodoViewModel  @Inject constructor(private val todoUseCase: TodoUseCase) : MVIViewModel<TodoState, TodoSideEffect>() {
    override val container: Container<TodoState, TodoSideEffect> = container(
        TodoState()
    )

    fun setFilterState(type: FilterType = FilterType.ALL) = intent {
        reduce {
            state.copy(
                type = type
            )
        }
    }

    init {
        fetchAllTodos()
    }
    fun fetchAllTodos() = intent {
        postSideEffect(TodoSideEffect.Loading)
        try {
            Log.d("fetch all","${state.type}")
            /*val data = when(state.type){
                FilterType.ALL -> {
                    todoUseCase.getAllTodos()
                }
                FilterType.COMPLETED -> {
                    todoUseCase.getCompletedTodos()
                }
                FilterType.UNCOMPLETED -> {
                    todoUseCase.getUnCompletedTodos()
                }
            }*/
            todoUseCase.getAllTodos().collectLatest {
                if(it.isEmpty()) {
                    postSideEffect(TodoSideEffect.Empty)
                } else {
                    Log.d("successed","${state.type} ${it.size}")
                    reduce {
                        state.copy(
                            data = it
                        )
                    }
                    postSideEffect(TodoSideEffect.Success)
                }
            }
        } catch (e : Exception) {
            postSideEffect(TodoSideEffect.ShowError("${e.message}"))
        }
    }

    fun addTodo(text : String) = intent {
        postSideEffect(TodoSideEffect.Loading)
        try {
            val data = Todo(
                title = text,
                date = getCurrentDateTime()
            )
            todoUseCase.insert(data)
            postSideEffect(TodoSideEffect.Success)
        } catch (e : Exception) {
            postSideEffect(TodoSideEffect.ShowError("${e.message}"))
        }
    }

    fun updateTodo(todo: Todo,position : Int) = intent {
        postSideEffect(TodoSideEffect.Loading)
        try {
            todo.let {
                it.date = getCurrentDateTime()
            }
            todoUseCase.update(todo)
            postSideEffect(TodoSideEffect.UpdateSuccess(position))
        } catch (e : Exception) {
            postSideEffect(TodoSideEffect.ShowError("${e.message}"))
        }
    }

    fun deleteTodo(todo: Todo) = intent {
        postSideEffect(TodoSideEffect.Loading)
        try {
            todoUseCase.delete(todo)
            postSideEffect(TodoSideEffect.Success)
        } catch (e : Exception) {
            postSideEffect(TodoSideEffect.ShowError("${e.message}"))
        }
    }

    fun searchTodo(key:String) = intent {
        postSideEffect(TodoSideEffect.Loading)
        try {
           /* val data = when(state.type){
                FilterType.ALL -> {
                    todoUseCase.searchTodos(key)
                }
                FilterType.COMPLETED -> {
                    todoUseCase.searchCompletedTodos(key)
                }
                FilterType.UNCOMPLETED -> {
                    todoUseCase.searchUnCompletedTodos(key)
                }
            }*/
            todoUseCase.searchTodos(key).collectLatest {
                if(it.isEmpty()) {
                    postSideEffect(TodoSideEffect.Empty)
                } else {
                    reduce {
                        state.copy(
                            data = it
                        )
                    }
                    postSideEffect(TodoSideEffect.Success)
                }
            }
        } catch (e : Exception) {
            postSideEffect(TodoSideEffect.ShowError("${e.message}"))
        }
    }
}

private fun getCurrentDateTime(): String {
    val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH)
    return dateFormat.format(Date())
}

enum class FilterType {
    ALL, COMPLETED, UNCOMPLETED
}

data class TodoState (
    val data : List<Todo> = emptyList(),
    val type : FilterType = FilterType.ALL
)

sealed class TodoSideEffect {
    object Loading : TodoSideEffect()

    data class ShowError(val message : String) : TodoSideEffect()

    object Success : TodoSideEffect()
    object Empty : TodoSideEffect()

    data class UpdateSuccess(val position: Int) : TodoSideEffect()
}
