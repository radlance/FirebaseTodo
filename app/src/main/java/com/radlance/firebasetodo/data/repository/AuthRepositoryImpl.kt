package com.radlance.firebasetodo.data.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
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

    override suspend fun loadUserInfo(name: String, email: String, imageUrl: Uri): FireBaseResult {
        return try {
            val database = FirebaseDatabase.getInstance(DATABASE_REFERENCE).getReference(MANAGEMENT_KEY)
            val userRef = database.child(CHILD_USER).child(Firebase.auth.currentUser!!.uid)
            if (imageUrl != Uri.EMPTY) {
                val storage = Firebase.storage.getReference().child(getStorageUrl(Firebase.auth.currentUser!!.uid))
                storage.putFile(imageUrl).await()
                val downloadUri = storage.downloadUrl.await()
                userRef.child(PROFILE_IMAGE).setValue(downloadUri.toString())
                FireBaseResult.Success(downloadUri.toString())
            }

            userRef.child(NAME).setValue(name).await()
            userRef.child(EMAIL).setValue(email).await()
            FireBaseResult.Success(Unit)
//            FirebaseDatabase
//                .getInstance(DATABASE_REFERENCE)
//                .getReference(MANAGEMENT_KEY)
//                .child(CHILD_USER)
//                .child(Firebase.auth.currentUser!!.uid)
//                .child(PROFILE_IMAGE).setValue(downloadUri.toString()).await()

        } catch (e: Exception) {
            FireBaseResult.Error(e.message ?: "error")
        }
    }

    override suspend fun loginUser(email: String, password: String): FireBaseResult {
        return try {
            Firebase.auth.signInWithEmailAndPassword(email, password).await()
            FireBaseResult.Success(Unit)
        } catch (e: Exception) {
            FireBaseResult.Error(e.message ?: "error")
        }
    }

    override fun isUserAuthenticated(): Boolean {
        return Firebase.auth.currentUser != null
    }

    override fun logoutUser() {
        Firebase.auth.signOut()
    }

    override fun forgetPassword(email: String): FireBaseResult {
        return try {
            Firebase.auth.sendPasswordResetEmail(email)
            FireBaseResult.Success(Unit)
        }
        catch (e: Exception) {
            FireBaseResult.Error(e.message ?: "error")
        }
    }



    private fun getStorageUrl(uId: String): String {
        return IMAGES_PATH + uId
    }

    companion object {
        private const val NAME = "name"
        private const val EMAIL = "email"
        const val DATABASE_REFERENCE =
            "https://fir-todo-b41b5-default-rtdb.europe-west1.firebasedatabase.app/"
        const val MANAGEMENT_KEY = "Management"
        const val CHILD_USER = "users"
        private const val PROFILE_IMAGE = "imageUrl"
        private const val IMAGES_PATH = "/images"
    }
}