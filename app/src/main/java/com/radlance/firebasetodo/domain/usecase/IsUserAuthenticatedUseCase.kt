package com.radlance.firebasetodo.domain.usecase

import com.radlance.firebasetodo.domain.repository.AuthRepository
import javax.inject.Inject

class IsUserAuthenticatedUseCase @Inject constructor(private val authRepository: AuthRepository) {
    operator fun invoke(): Boolean {
        return authRepository.isUserAuthenticated()
    }
}