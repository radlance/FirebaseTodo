package com.radlance.firebasetodo.presentation.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.radlance.firebasetodo.domain.FireBaseResult
import com.radlance.firebasetodo.domain.usecase.RegisterUserUseCase
import com.radlance.firebasetodo.presentation.utils.validateFireBaseEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase,
    private val mapper: FireBaseResult.Mapper<FireBaseUiState>
) : ViewModel() {

    private val _isSuccessfulRegistration = MutableLiveData<FireBaseUiState>()
    val isSuccessfulRegistration: LiveData<FireBaseUiState>
        get() = _isSuccessfulRegistration

    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    private val _errorInputEmail = MutableLiveData<Boolean>()
    val errorInputEmail: LiveData<Boolean>
        get() = _errorInputEmail

    private val _errorInputPassword = MutableLiveData<Boolean>()
    val errorInputPassword: LiveData<Boolean>
        get() = _errorInputPassword

    private val _errorInputConfirmPassword = MutableLiveData<Boolean>()
    val errorInputConfirmPassword: LiveData<Boolean>
        get() = _errorInputConfirmPassword

    fun registerUser(name: String, email: String, password: String, confirmPassword: String) {
        val formattedName = parseString(name)
        val formattedEmail = parseString(email)
        val formattedPassword = parseString(password)
        val formattedConfirmPassword = parseString(confirmPassword)
        if (validateInput(
                formattedName,
                formattedEmail,
                formattedPassword,
                formattedConfirmPassword
            )
        ) {
            viewModelScope.launch {
                val fireBaseResult = registerUserUseCase(formattedName, formattedEmail, formattedPassword)
                val uiState = fireBaseResult.map(mapper)
                _isSuccessfulRegistration.value = uiState
            }
        }
    }

    private fun validateInput(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        val result = true

        if (name.isBlank()) {
            _errorInputName.value = true
            return false
        }

        if (email.isBlank() || !validateFireBaseEmail(email)) {
            _errorInputEmail.value = true
            return false
        }

        if (password.length < 6) {
            _errorInputPassword.value = true
            return false
        }

        if (confirmPassword != password) {
            _errorInputConfirmPassword.value = true
            return false
        }
        return result
    }


    private fun parseString(string: String?): String = string?.trim() ?: ""

    fun resetInputName() {
        _errorInputName.value = false
    }

    fun resetErrorInputEmail() {
        _errorInputEmail.value = false
    }

    fun resetErrorInputPassword() {
        _errorInputPassword.value = false
    }

    fun resetErrorInputConfirmPassword() {
        _errorInputConfirmPassword.value = false
    }
}