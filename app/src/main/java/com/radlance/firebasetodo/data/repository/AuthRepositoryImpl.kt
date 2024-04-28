package com.radlance.firebasetodo.data.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.radlance.firebasetodo.domain.FireBaseResult
import com.radlance.firebasetodo.domain.entity.User
import com.radlance.firebasetodo.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor() : AuthRepository {
    override suspend fun registerUser(
        name: String,
        email: String,
        password: String
    ): FireBaseResult {
        return try {
            Firebase.auth.createUserWithEmailAndPassword(email, password).await()
            addNewUserToDataBase(name, email)
            FireBaseResult.Success(Unit)
        } catch (e: Exception) {
            FireBaseResult.Error(e.message ?: "error")
        }
    }

    private fun addNewUserToDataBase(name: String, email: String, profileImage: String = "") {
        FirebaseDatabase
            .getInstance(DATABASE_REFERENCE)
            .getReference(MANAGEMENT_KEY)
            .child(CHILD_USER)
            .child(Firebase.auth.currentUser!!.uid)
            .setValue(User(name, email, profileImage))
    }

    override suspend fun sendConfirmEmail(): FireBaseResult {
        return try {
            Firebase.auth.currentUser?.sendEmailVerification()?.await()
            FireBaseResult.Success(Unit)
        } catch (e: Exception) {
            FireBaseResult.Error(e.message.toString())
        }
    }

    override suspend fun loginUser(email: String, password: String): FireBaseResult {
        return try {
            Firebase.auth.signInWithEmailAndPassword(email, password).await()
            if(Firebase.auth.currentUser?.isEmailVerified == true) {
                FireBaseResult.Success(Unit)
            } else {
                FireBaseResult.Error("email not confirmed")
            }
        } catch (e: Exception) {
            FireBaseResult.Error(e.message ?: "error")
        }
    }

    override fun isUserAuthenticated(): Boolean {
        return Firebase.auth.currentUser != null && Firebase.auth.currentUser?.isEmailVerified == true
    }

    override fun logoutUser() {
        Firebase.auth.signOut()
    }

    override fun forgetPassword(email: String): FireBaseResult {
        return try {
            Firebase.auth.sendPasswordResetEmail(email)
            FireBaseResult.Success(Unit)
        } catch (e: Exception) {
            FireBaseResult.Error(e.message ?: "error")
        }
    }

    companion object {
        const val NAME = "name"
        const val EMAIL = "email"
        const val DATABASE_REFERENCE =
            "https://fir-todo-b41b5-default-rtdb.europe-west1.firebasedatabase.app/"
        const val MANAGEMENT_KEY = "Management"
        const val CHILD_USER = "users"
        const val PROFILE_IMAGE = "imageUrl"
        const val IMAGES_PATH = "/images"
    }
}