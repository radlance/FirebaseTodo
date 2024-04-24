package com.radlance.firebasetodo.domain.usecase

import com.radlance.firebasetodo.domain.entity.Todo
import com.radlance.firebasetodo.domain.repository.AppRepository
import javax.inject.Inject

class DeleteTodoUseCase @Inject constructor(private val appRepository: AppRepository) {
    suspend operator fun invoke(todo: Todo) {
        return appRepository.deleteTodo(todo)
    }
}