package com.radlance.firebasetodo.presentation.todo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.radlance.firebasetodo.domain.entity.Todo
import com.radlance.firebasetodo.domain.usecase.EditTodoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditTodoViewModel @Inject constructor(private val editTodoUseCase: EditTodoUseCase) :
    ViewModel() {
    private val _errorInputTodo = MutableLiveData<Boolean>()
    val errorInputTodo: LiveData<Boolean>
        get() = _errorInputTodo
    fun editTodo(todo: Todo) {
        val formattedTodo = todo.copy(title = parseString(todo.title))
        if (validateTodo(formattedTodo)) {
            viewModelScope.launch {
                editTodoUseCase(formattedTodo)
            }
        }
    }

    private fun validateTodo(todo: Todo): Boolean {
        var result = true
        if (todo.title?.isBlank() == true) {
            _errorInputTodo.value = true
            result = false
        }
        return result
    }

    private fun parseString(string: String?) = string?.trim() ?: ""
    fun resetErrorInputTodo() {
        _errorInputTodo.value = false
    }
}