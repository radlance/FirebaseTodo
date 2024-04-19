package com.radlance.firebasetodo.presentation.auth

import com.radlance.firebasetodo.domain.FireBaseResult
import javax.inject.Inject

class FireBaseUiMapper @Inject constructor() : FireBaseResult.Mapper<FireBaseUiState> {
    override fun <E: Any> mapSuccess(value: E): FireBaseUiState {
        return FireBaseUiState.Success(value)
    }

    override fun mapError(message: String): FireBaseUiState {
        return FireBaseUiState.Error(message)
    }
}