package com.aungbophyoe.domain.di

import com.aungbophyoe.data.TodoDao
import com.aungbophyoe.data.repo.TodoRepository
import com.aungbophyoe.domain.mapper.TodoMapper
import com.aungbophyoe.domain.repo.TodoRepositoryImpl
import com.aungbophyoe.domain.usecase.TodoUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {
    @Provides
    fun provideTodoRepository(todoDao: TodoDao): TodoRepositoryImpl {
        return TodoRepositoryImpl(todoDao)
    }

    @Provides
    fun provideTodoUseCase(repository: TodoRepository,mapper: TodoMapper) : TodoUseCase {
        return TodoUseCase(repository, mapper)
    }
}