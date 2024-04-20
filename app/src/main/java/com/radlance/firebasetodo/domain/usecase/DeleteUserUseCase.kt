package com.radlance.firebasetodo.domain.usecase

import com.radlance.firebasetodo.domain.FireBaseResult
import com.radlance.firebasetodo.domain.repository.AppRepository
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(private val appRepository: AppRepository) {
    operator fun invoke(): FireBaseResult {
        return appRepository.deleteUser()
    }
}