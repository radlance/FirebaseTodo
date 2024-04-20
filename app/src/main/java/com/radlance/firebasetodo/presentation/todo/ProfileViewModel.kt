package com.radlance.firebasetodo.presentation.todo

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.radlance.firebasetodo.domain.FireBaseResult
import com.radlance.firebasetodo.domain.usecase.DeleteUserUseCase
import com.radlance.firebasetodo.domain.usecase.UpdateUserInfoUseCase
import com.radlance.firebasetodo.domain.usecase.LoadUserInfoUseCase
import com.radlance.firebasetodo.presentation.auth.FireBaseUiState
import com.radlance.firebasetodo.presentation.utils.validateFireBaseEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val loadUserInfoUseCase: LoadUserInfoUseCase,
    private val updateUserInfoUseCase: UpdateUserInfoUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val mapper: FireBaseResult.Mapper<FireBaseUiState>
) : ViewModel() {
    private val _isSuccessfulLoadUserInfo = MutableLiveData<FireBaseUiState>()
    val isSuccessfulLoad: LiveData<FireBaseUiState>
        get() = _isSuccessfulLoadUserInfo

    private val _isSuccessfulLoadUserImage = MutableLiveData<FireBaseUiState>()
    val isSuccessfulLoadUserImage: LiveData<FireBaseUiState>
        get() = _isSuccessfulLoadUserImage
    private val _errorInputName = MutableLiveData<Boolean>()

    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    private val _errorInputEmail = MutableLiveData<Boolean>()
    val errorInputEmail: LiveData<Boolean>
        get() = _errorInputEmail

    private fun validateInput(name: String, email: String): Boolean {
        val result = true
        if (name.isBlank()) {
            _errorInputName.value = true
            return false
        }

        if(email.isBlank() || !validateFireBaseEmail(email)) {
            _errorInputEmail.value = true
            return false
        }
        return result
    }

    fun loadUserInfo() {
        viewModelScope.launch {
            val loadResult = loadUserInfoUseCase()
            val uiState = loadResult.map(mapper)
            _isSuccessfulLoadUserInfo.value = uiState
        }
    }

    fun uploadImageUri(name: String, email: String, imageUri: Uri = Uri.EMPTY) {
        val formattedName = parseString(name)
        val formattedEmail = parseString(email)
        if (validateInput(formattedName, formattedEmail)) {
            viewModelScope.launch {
                val loadResult = updateUserInfoUseCase(formattedName, formattedEmail, imageUri)
                val uiState = loadResult.map(mapper)
                _isSuccessfulLoadUserImage.value = uiState
            }
        }
    }

    private fun parseString(string: String?): String = string?.trim() ?: ""

    fun resetInputName() {
        _errorInputName.value = false
    }

    fun resetErrorInputEmail() {
        _errorInputEmail.value = false
    }

    fun deleteUser() {
        deleteUserUseCase()
    }
}