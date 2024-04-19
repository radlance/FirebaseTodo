package com.radlance.firebasetodo.domain.usecase

import android.net.Uri
import com.radlance.firebasetodo.domain.FireBaseResult
import com.radlance.firebasetodo.domain.repository.AppRepository
import com.radlance.firebasetodo.domain.repository.AuthRepository
import javax.inject.Inject

class LoadUserImageUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(imageUri: Uri): FireBaseResult {
        return authRepository.loadUserImage(imageUri)
    }
}