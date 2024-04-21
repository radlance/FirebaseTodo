package com.radlance.firebasetodo.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.radlance.firebasetodo.domain.usecase.SendConfirmEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmEmailViewModel @Inject constructor(
    private val sendConfirmEmailUseCase: SendConfirmEmailUseCase,
) :
    ViewModel() {

    fun sendConfirmEmail() {
        viewModelScope.launch {
            sendConfirmEmailUseCase()
        }
    }
}