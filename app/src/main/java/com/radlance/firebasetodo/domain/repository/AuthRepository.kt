package com.radlance.firebasetodo.domain.repository

import com.radlance.firebasetodo.domain.FireBaseResult

interface AuthRepository {
    suspend fun registerUser(name: String, email: String, password: String): FireBaseResult

    suspend fun sendConfirmEmail(): FireBaseResult
    suspend fun loginUser(email: String, password: String): FireBaseResult
    fun isUserAuthenticated(): Boolean
    fun logoutUser()
    fun forgetPassword(email: String): FireBaseResult

}