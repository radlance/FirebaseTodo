package com.radlance.firebasetodo.presentation.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.radlance.firebasetodo.domain.FireBaseResult
import com.radlance.firebasetodo.domain.usecase.LoginUserUseCase
import com.radlance.firebasetodo.presentation.utils.validateFireBaseEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUserUseCase: LoginUserUseCase,
    private val mapper: FireBaseResult.Mapper<FireBaseUiState>,
) : ViewModel() {
    private val _errorInputEmail = MutableLiveData<Boolean>()
    val errorInputEmail: LiveData<Boolean>
        get() = _errorInputEmail

    private val _errorInputPassword = MutableLiveData<Boolean>()
    val errorInputPassword: LiveData<Boolean>
        get() = _errorInputPassword

    private val _isSuccessfulLogin = MutableLiveData<FireBaseUiState>()
    val isSuccessfulLogin: LiveData<FireBaseUiState>
        get() = _isSuccessfulLogin

    private fun validateInput(email: String, password: String): Boolean {
        val result = true
        if (email.isBlank() || !validateFireBaseEmail(email)) {
            _errorInputEmail.value = true

            return false
        }

        if (password.length < 6) {
            _errorInputPassword.value = true
            return false
        }
        return result
    }

    fun loginUser(email: String, password: String) {
        val formattedEmail = parseString(email)
        val formattedPassword = parseString(password)

        if (validateInput(formattedEmail, formattedPassword)) {
            viewModelScope.launch {
                val fireBaseResult = loginUserUseCase(formattedEmail, formattedPassword)
                val uiState = fireBaseResult.map(mapper)
                _isSuccessfulLogin.value = uiState
            }
        }
    }

    private fun parseString(string: String?): String = string?.trim() ?: ""

    fun resetErrorInputEmail() {
        _errorInputEmail.value = false
    }

    fun resetErrorInputPassword() {
        _errorInputPassword.value = false
    }
}