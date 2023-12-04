package com.aungbophyoe.mytodo.ui

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

@HiltViewModel
class TodoViewModel  @Inject constructor(private val todoUseCase: TodoUseCase) : MVIViewModel<TodoState, TodoSideEffect>() {
    override val container: Container<TodoState, TodoSideEffect> = container(
        TodoState()
    )

    init {
        fetchAllTodos()
    }
    private fun fetchAllTodos() = intent {
        postSideEffect(TodoSideEffect.Loading)
        todoUseCase.getAllTodos().collectLatest {
            reduce {
                state.copy(
                    data = it
                )
            }
        }
    }
}

data class TodoState (
    val data : List<Todo> = emptyList()
)

sealed class TodoSideEffect {
    object Loading : TodoSideEffect()

    data class ShowError(val message : String) : TodoSideEffect()
}
