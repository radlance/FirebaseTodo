package com.radlance.firebasetodo.domain.repository

import com.radlance.firebasetodo.domain.FireBaseResult

interface AppRepository {
    suspend fun loadUserInfoUseCase(): FireBaseResult
}