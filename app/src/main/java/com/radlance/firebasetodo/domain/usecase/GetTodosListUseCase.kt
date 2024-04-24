package com.radlance.firebasetodo.domain.usecase

import androidx.lifecycle.LiveData
import com.radlance.firebasetodo.domain.entity.Todo
import com.radlance.firebasetodo.domain.repository.AppRepository
import javax.inject.Inject

class GetTodosListUseCase @Inject constructor(private val appRepository: AppRepository) {
    suspend operator fun invoke(): List<Todo> {
        return appRepository.getTodosList()
    }
}