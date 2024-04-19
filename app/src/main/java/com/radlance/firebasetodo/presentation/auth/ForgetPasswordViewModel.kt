package com.radlance.firebasetodo.presentation.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.radlance.firebasetodo.domain.FireBaseResult
import com.radlance.firebasetodo.domain.usecase.ForgetPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ForgetPasswordViewModel @Inject constructor(
    private val forgetPasswordUseCase: ForgetPasswordUseCase,
    private val mapper: FireBaseResult.Mapper<FireBaseUiState>
) : ViewModel() {
    private val emailRegex =
        Regex("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$")
    private val _isSuccessfulSendEmail = MutableLiveData<FireBaseUiState>()
    val isSuccessfulSendEmail: LiveData<FireBaseUiState>
        get() = _isSuccessfulSendEmail

    private val _errorInputEmail = MutableLiveData<Boolean>()
    val errorInputEmail: LiveData<Boolean>
        get() = _errorInputEmail

    private fun validateInput(email: String): Boolean {
        val result = true
        if (email.isBlank() || !email.matches(emailRegex)) {
            _errorInputEmail.value = true
            return false
        }
        return result
    }

    fun resetErrorInputEmail() {
        _errorInputEmail.value = false
    }

    private fun parseString(string: String?): String = string?.trim() ?: ""

    fun sendEmail(email: String) {
        if (validateInput(parseString(email))) {
            val sendResult = forgetPasswordUseCase(email)
            val uiState = sendResult.map(mapper)
            _isSuccessfulSendEmail.value = uiState
        }
    }
}