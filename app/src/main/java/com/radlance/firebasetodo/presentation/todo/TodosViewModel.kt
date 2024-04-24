package com.radlance.firebasetodo.presentation.todo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.radlance.firebasetodo.domain.entity.Todo
import com.radlance.firebasetodo.domain.usecase.AddTodoUseCase
import com.radlance.firebasetodo.domain.usecase.EditTodoUseCase
import com.radlance.firebasetodo.domain.usecase.GetTodosListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodosViewModel @Inject constructor(
    private val getTodosListUseCase: GetTodosListUseCase,
    private val addTodoUseCase: AddTodoUseCase,
    private val editTodoUseCase: EditTodoUseCase
) :
    ViewModel() {
        private val _todosList = MutableLiveData<List<Todo>>()
        val todosList: LiveData<List<Todo>>
            get() = _todosList

    init {
        getTodosList()
    }
    private fun getTodosList() {
        viewModelScope.launch {
            _todosList.value = getTodosListUseCase()!!
        }
    }

    fun addTodo() {
        viewModelScope.launch {
            for (i in 1..10) {
                async {
                    addTodoUseCase(Todo(i, "title $i", false))
                }.await()
            }
            _todosList.value = getTodosListUseCase()!!
        }
    }

    fun changeCompletedState(todo: Todo) {
        viewModelScope.launch {
            val completedTodo = todo.copy(completed = true)
            editTodoUseCase(completedTodo)
            _todosList.value = getTodosListUseCase()!!
        }
    }
}
