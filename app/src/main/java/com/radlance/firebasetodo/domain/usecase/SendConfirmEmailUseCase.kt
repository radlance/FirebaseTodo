package com.radlance.firebasetodo.domain.usecase

import com.radlance.firebasetodo.domain.FireBaseResult
import com.radlance.firebasetodo.domain.repository.AuthRepository
import javax.inject.Inject

class SendConfirmEmailUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(): FireBaseResult {
        return repository.sendConfirmEmail()
    }
}