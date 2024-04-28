package com.radlance.firebasetodo.domain.repository

import android.net.Uri
import com.radlance.firebasetodo.domain.FireBaseResult
import com.radlance.firebasetodo.domain.entity.Todo

interface AppRepository {
    suspend fun loadUserInfo(): FireBaseResult
    suspend fun uploadUserInfo(name: String, email: String, imageUrl: Uri): FireBaseResult
    suspend fun deleteUser(password: String): FireBaseResult
    suspend fun addTodo(todo: Todo)
    suspend fun editTodo(todo: Todo)
    suspend fun deleteTodo(todo: Todo)
    suspend fun getTodosList(): List<Todo>
    suspend fun updateTodosStatistic(todosList: List<Todo>)
    suspend fun loadTasksInfo(): FireBaseResult
}