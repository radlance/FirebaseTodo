package com.radlance.firebasetodo.presentation.todo

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.radlance.firebasetodo.domain.FireBaseResult
import com.radlance.firebasetodo.domain.usecase.LoadUserImageUseCase
import com.radlance.firebasetodo.domain.usecase.LoadUserInfoUseCase
import com.radlance.firebasetodo.presentation.auth.FireBaseUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val loadUserInfoUseCase: LoadUserInfoUseCase,
    private val loadUserImageUseCase: LoadUserImageUseCase,
    private val mapper: FireBaseResult.Mapper<FireBaseUiState>
) : ViewModel() {
    private val _isSuccessfulLoadUserInfo = MutableLiveData<FireBaseUiState>()
    val isSuccessfulLoad: LiveData<FireBaseUiState>
        get() = _isSuccessfulLoadUserInfo

    private val _isSuccessfulLoadUserImage = MutableLiveData<FireBaseUiState>()
    val isSuccessfulLoadUserImage: LiveData<FireBaseUiState>
        get() = _isSuccessfulLoadUserImage

    fun loadUserInfo() {
        viewModelScope.launch {
            val loadResult = loadUserInfoUseCase()
            val uiState = loadResult.map(mapper)
            _isSuccessfulLoadUserInfo.value = uiState
        }
    }

    fun loadImageUri(imageUri: Uri) {
        viewModelScope.launch {
            val loadResult = loadUserImageUseCase(imageUri)
            val uiState = loadResult.map(mapper)
            _isSuccessfulLoadUserImage.value = uiState
        }
    }

    //TODO доделать загрузку изображений (intent, state, storage)
}