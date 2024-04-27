package com.radlance.firebasetodo.domain.usecase

import com.radlance.firebasetodo.domain.FireBaseResult
import com.radlance.firebasetodo.domain.repository.AppRepository
import javax.inject.Inject

class LoadTasksInfoUseCase @Inject constructor(private val repository: AppRepository) {
    suspend operator fun invoke(): FireBaseResult {
        return repository.loadTasksInfo()
    }
}