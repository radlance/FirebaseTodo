package com.radlance.firebasetodo.domain.usecase

import android.net.Uri
import com.radlance.firebasetodo.domain.FireBaseResult
import com.radlance.firebasetodo.domain.repository.AuthRepository
import javax.inject.Inject

class UpdateUserInfoUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(name: String, email: String, imageUri: Uri = Uri.EMPTY): FireBaseResult {
        return authRepository.loadUserInfo(name, email, imageUri)
    }
}