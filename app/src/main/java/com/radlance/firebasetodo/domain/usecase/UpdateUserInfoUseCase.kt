package com.radlance.firebasetodo.domain.usecase

import android.net.Uri
import com.radlance.firebasetodo.domain.FireBaseResult
import com.radlance.firebasetodo.domain.repository.AppRepository
import com.radlance.firebasetodo.domain.repository.AuthRepository
import javax.inject.Inject

class UpdateUserInfoUseCase @Inject constructor(private val appRepository: AppRepository) {
    suspend operator fun invoke(name: String, email: String, imageUri: Uri = Uri.EMPTY): FireBaseResult {
        return appRepository.loadUserInfo(name, email, imageUri)
    }
}