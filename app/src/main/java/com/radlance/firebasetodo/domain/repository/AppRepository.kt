package com.radlance.firebasetodo.domain.repository

import android.net.Uri
import com.radlance.firebasetodo.domain.FireBaseResult

interface AppRepository {
    suspend fun loadUserInfoUseCase(): FireBaseResult
    suspend fun loadUserInfo(name: String, email: String, imageUrl: Uri): FireBaseResult
    suspend fun deleteUser(password: String): FireBaseResult
}