package com.radlance.firebasetodo.presentation.todo

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.radlance.firebasetodo.domain.FireBaseResult
import com.radlance.firebasetodo.domain.usecase.LoadTasksInfoUseCase
import com.radlance.firebasetodo.domain.usecase.LoadUserInfoUseCase
import com.radlance.firebasetodo.domain.usecase.UpdateUserInfoUseCase
import com.radlance.firebasetodo.presentation.auth.FireBaseUiState
import com.radlance.firebasetodo.presentation.utils.validateFireBaseEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val loadUserInfoUseCase: LoadUserInfoUseCase,
    private val updateUserInfoUseCase: UpdateUserInfoUseCase,
    private val loadTasksInfoUseCase: LoadTasksInfoUseCase,
    private val mapper: FireBaseResult.Mapper<FireBaseUiState>,
) : ViewModel() {
    private val _isSuccessfulLoadUserInfo = MutableLiveData<FireBaseUiState>()
    val isSuccessfulLoad: LiveData<FireBaseUiState>
        get() = _isSuccessfulLoadUserInfo

    private val _isSuccessfulLoadTasksInfo = MutableLiveData<FireBaseUiState>()
    val isSuccessfulLoadTasksInfo: LiveData<FireBaseUiState>
        get() = _isSuccessfulLoadTasksInfo

    private val _isSuccessfulLoadUserImage = MutableLiveData<FireBaseUiState>()
    val isSuccessfulLoadUserImage: LiveData<FireBaseUiState>
        get() = _isSuccessfulLoadUserImage
    private val _errorInputName = MutableLiveData<Boolean>()

    private val _isReadyToSave = MutableLiveData<Boolean>()
    val isReadyToSave: LiveData<Boolean>
        get() = _isReadyToSave

    val errorInputName: LiveData<Boolean>
        get() = _errorInputName


    private fun validateInput(name: String, email: String): Boolean {
        val result = true
        if (name.isBlank()) {
            _errorInputName.value = true
            return false
        }

        if (email.isBlank() || !validateFireBaseEmail(email)) {
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

    fun loadTasksInfo() {
        viewModelScope.launch {
            val loadResult = loadTasksInfoUseCase()
            val uiState = loadResult.map(mapper)
            _isSuccessfulLoadTasksInfo.value = uiState
        }
    }
    fun uploadImageUri(name: String, email: String, imageUri: Uri = Uri.EMPTY) {
        val formattedName = parseString(name)
        val formattedEmail = parseString(email)
        if (validateInput(formattedName, formattedEmail)) {
            _isReadyToSave.value = true
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
}