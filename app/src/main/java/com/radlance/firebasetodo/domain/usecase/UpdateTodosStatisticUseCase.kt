package com.radlance.firebasetodo.domain.usecase

import com.radlance.firebasetodo.domain.entity.Todo
import com.radlance.firebasetodo.domain.repository.AppRepository
import javax.inject.Inject

class UpdateTodosStatisticUseCase @Inject constructor(private val repository: AppRepository) {
    suspend operator fun invoke(todosList: List<Todo>) {
        return repository.updateTodosStatistic(todosList)
    }
}