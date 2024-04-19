package com.radlance.firebasetodo.data.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.radlance.firebasetodo.domain.FireBaseResult
import com.radlance.firebasetodo.domain.entity.User
import com.radlance.firebasetodo.domain.repository.AppRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(): AppRepository {
    override suspend fun loadUserInfoUseCase(): FireBaseResult {
        return try {
            val userSnapshot = FirebaseDatabase.getInstance(AuthRepositoryImpl.DATABASE_REFERENCE)
                .getReference(AuthRepositoryImpl.MANAGEMENT_KEY)
                .child(AuthRepositoryImpl.CHILD_USER)
                .child(Firebase.auth.currentUser!!.uid).get().await()

            if(userSnapshot.exists()) {
                val user = userSnapshot.getValue(User::class.java)
                FireBaseResult.Success(user!!)
            } else {
                FireBaseResult.Error("User not found")
            }
        } catch (e: Exception) {
            FireBaseResult.Error(e.message.toString())
        }
    }


    companion object {
        private const val NAME = "name"
        private const val EMAIL = "email"
    }
}