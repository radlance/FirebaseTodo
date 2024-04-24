package com.radlance.firebasetodo.domain.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import com.radlance.firebasetodo.domain.FireBaseResult
import com.radlance.firebasetodo.domain.entity.Todo

interface AppRepository {
    suspend fun loadUserInfoUseCase(): FireBaseResult
    suspend fun loadUserInfo(name: String, email: String, imageUrl: Uri): FireBaseResult
    suspend fun deleteUser(password: String): FireBaseResult

    suspend fun addTodo(todo: Todo)
    suspend fun editTodo(todo: Todo)
    suspend fun deleteTodo(todo: Todo)
    suspend fun getTodosList(): List<Todo>
}