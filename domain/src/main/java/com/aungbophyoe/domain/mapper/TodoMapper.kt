package com.aungbophyoe.domain.mapper

import com.aungbophyoe.data.model.TodoEntity
import com.aungbophyoe.domain.model.Todo
import javax.inject.Inject

class TodoMapper @Inject constructor() : EntityMapper<TodoEntity, Todo> {
    override fun mapFromEntity(entity: TodoEntity): Todo {
        return Todo(
            id = entity.id, title = entity.title,isCompleted = entity.isCompleted, date = entity.date, isUpdated = entity.isUpdated
        )
    }

    override fun mapToEntity(domainModel: Todo): TodoEntity {
        return TodoEntity(
            id = domainModel.id, title = domainModel.title,isCompleted = domainModel.isCompleted, date = domainModel.date, isUpdated = domainModel.isUpdated
        )
    }
}