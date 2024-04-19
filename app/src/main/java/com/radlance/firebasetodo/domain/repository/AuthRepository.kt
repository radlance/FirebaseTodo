package com.radlance.firebasetodo.domain.repository

import android.net.Uri
import com.radlance.firebasetodo.domain.FireBaseResult

interface AuthRepository {
    suspend fun registerUser(name: String, email: String, password: String): FireBaseResult
    suspend fun loginUser(email: String, password: String): FireBaseResult
    fun isUserAuthenticated(): Boolean
    fun logoutUser()
    fun forgetPassword(email: String): FireBaseResult
    suspend fun loadUserInfo(name: String, email: String, imageUrl: Uri): FireBaseResult
}