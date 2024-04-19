package com.radlance.firebasetodo.presentation.auth

import android.content.Context
import android.widget.Toast

interface FireBaseUiState {
    data class Success<T : Any>(val value: T) : FireBaseUiState

    data class Error(val message: String) : FireBaseUiState {
        fun showErrorMessage(context: Context) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
}
