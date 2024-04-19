package com.radlance.firebasetodo.presentation.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.radlance.firebasetodo.domain.usecase.IsUserAuthenticatedUseCase
import com.radlance.firebasetodo.domain.usecase.LogoutUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val isUserAuthenticatedUseCase: IsUserAuthenticatedUseCase,
    private val logoutUserUseCase: LogoutUserUseCase
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
}