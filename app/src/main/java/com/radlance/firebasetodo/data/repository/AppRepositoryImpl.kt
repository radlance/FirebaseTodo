package com.radlance.firebasetodo.data.repository

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.radlance.firebasetodo.domain.FireBaseResult
import com.radlance.firebasetodo.domain.entity.User
import com.radlance.firebasetodo.domain.repository.AppRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor() : AppRepository {
    override suspend fun loadUserInfoUseCase(): FireBaseResult {
        return try {
            val userSnapshot = FirebaseDatabase.getInstance(AuthRepositoryImpl.DATABASE_REFERENCE)
                .getReference(AuthRepositoryImpl.MANAGEMENT_KEY)
                .child(AuthRepositoryImpl.CHILD_USER)
                .child(Firebase.auth.currentUser!!.uid).get().await()

            if (userSnapshot.exists()) {
                val user = userSnapshot.getValue(User::class.java)
                FireBaseResult.Success(user!!)
            } else {
                FireBaseResult.Error("User not found")
            }
        } catch (e: Exception) {
            FireBaseResult.Error(e.message.toString())
        }
    }

    override suspend fun loadUserInfo(name: String, email: String, imageUrl: Uri): FireBaseResult {
        return try {
            val database =
                FirebaseDatabase.getInstance(AuthRepositoryImpl.DATABASE_REFERENCE).getReference(
                    AuthRepositoryImpl.MANAGEMENT_KEY
                )
            val userRef =
                database.child(AuthRepositoryImpl.CHILD_USER).child(Firebase.auth.currentUser!!.uid)
            if (imageUrl != Uri.EMPTY) {
                val storage = Firebase.storage.getReference()
                    .child(getStorageUrl(Firebase.auth.currentUser!!.uid))
                storage.putFile(imageUrl).await()
                val downloadUri = storage.downloadUrl.await()
                userRef.child(AuthRepositoryImpl.PROFILE_IMAGE).setValue(downloadUri.toString())
                FireBaseResult.Success(downloadUri.toString())
            }
            userRef.child(AuthRepositoryImpl.NAME).setValue(name).await()
            userRef.child(AuthRepositoryImpl.EMAIL).setValue(email).await()
            FireBaseResult.Success(Unit)

        } catch (e: Exception) {
            FireBaseResult.Error(e.message ?: "error")
        }
    }

    override suspend fun deleteUser(password: String): FireBaseResult {
        return try {
            Firebase.auth.currentUser?.reauthenticate(
                EmailAuthProvider.getCredential(
                    Firebase.auth.currentUser?.email.toString(),
                    password
                )
            )?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    FirebaseDatabase
                        .getInstance(AuthRepositoryImpl.DATABASE_REFERENCE)
                        .getReference(AuthRepositoryImpl.MANAGEMENT_KEY)
                        .child(AuthRepositoryImpl.CHILD_USER)
                        .child(Firebase.auth.currentUser!!.uid)
                        .removeValue().addOnCompleteListener {
                            Firebase.auth.currentUser?.delete()?.addOnCompleteListener {
                                if (!it.isSuccessful) {
                                    FireBaseResult.Error(it.exception.toString())
                                }
                            }
                        }
                } else {
                    FireBaseResult.Error(task.exception.toString())
                }
            }?.await()
            FireBaseResult.Success(Unit)
        } catch (e: Exception) {
            FireBaseResult.Error(e.message.toString())
        }
    }

    private fun getStorageUrl(uId: String): String {
        return AuthRepositoryImpl.IMAGES_PATH + uId
    }
}