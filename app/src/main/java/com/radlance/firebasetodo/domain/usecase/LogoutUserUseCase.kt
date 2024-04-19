package com.radlance.firebasetodo.domain.usecase

import com.radlance.firebasetodo.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUserUseCase @Inject constructor(private val authRepository: AuthRepository) {
    operator fun invoke() {
        authRepository.logoutUser()
    }
}