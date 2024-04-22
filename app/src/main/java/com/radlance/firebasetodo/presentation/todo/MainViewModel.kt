package com.radlance.firebasetodo.presentation.todo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.radlance.firebasetodo.domain.FireBaseResult
import com.radlance.firebasetodo.domain.usecase.DeleteUserUseCase
import com.radlance.firebasetodo.domain.usecase.IsUserAuthenticatedUseCase
import com.radlance.firebasetodo.domain.usecase.LogoutUserUseCase
import com.radlance.firebasetodo.presentation.auth.FireBaseUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val isUserAuthenticatedUseCase: IsUserAuthenticatedUseCase,
    private val logoutUserUseCase: LogoutUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val mapper: FireBaseResult.Mapper<FireBaseUiState>
) : ViewModel() {
    private val _isUserAuthenticated = MutableLiveData<Boolean>()
    val isUserAuthenticated: LiveData<Boolean>
        get() = _isUserAuthenticated

    fun isUserAuthenticated() {
        _isUserAuthenticated.value = isUserAuthenticatedUseCase()
    }

    fun logoutUser() {
        logoutUserUseCase()
        _isUserAuthenticated.value = isUserAuthenticatedUseCase()
    }

    private val _isSuccessfulDelete = MutableLiveData<FireBaseUiState>()
    val isSuccessfulDelete: LiveData<FireBaseUiState>
        get() = _isSuccessfulDelete

    fun deleteUser(password: String) {
        viewModelScope.launch {
            val fireBaseResult = deleteUserUseCase(password)
            val uiState = fireBaseResult.map(mapper)
            _isSuccessfulDelete.value = uiState
            _isUserAuthenticated.value = false
        }
        viewModelScope.launch {
            deleteUserUseCase(password)
        }
    }
}