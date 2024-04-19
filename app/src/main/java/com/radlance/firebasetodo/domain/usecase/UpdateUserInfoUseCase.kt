package com.radlance.firebasetodo.domain.usecase

import com.radlance.firebasetodo.domain.FireBaseResult
import com.radlance.firebasetodo.domain.repository.AuthRepository
import javax.inject.Inject

class UpdateUserInfoUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(name: String, email: String): FireBaseResult {
        return repository.updateUserInfo(name, email)
    }
}