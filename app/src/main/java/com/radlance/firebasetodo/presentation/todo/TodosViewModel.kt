package com.radlance.firebasetodo.presentation.todo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.radlance.firebasetodo.domain.entity.Todo
import com.radlance.firebasetodo.domain.usecase.AddTodoUseCase
import com.radlance.firebasetodo.domain.usecase.DeleteTodoUseCase
import com.radlance.firebasetodo.domain.usecase.EditTodoUseCase
import com.radlance.firebasetodo.domain.usecase.GetTodosListUseCase
import com.radlance.firebasetodo.domain.usecase.UpdateTodosStatisticUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodosViewModel @Inject constructor(
    private val getTodosListUseCase: GetTodosListUseCase,
    private val addTodoUseCase: AddTodoUseCase,
    private val editTodoUseCase: EditTodoUseCase,
    private val deleteTodoUseCase: DeleteTodoUseCase,
    private val updateTodosStatisticUseCase: UpdateTodosStatisticUseCase
) :
    ViewModel() {
    private val _todosList = MutableLiveData<List<Todo>>()
    val todosList: LiveData<List<Todo>>
        get() = _todosList

    fun getTodosList() {
        viewModelScope.launch {
            _todosList.value = getTodosListUseCase()!!
        }
    }

    fun updateTodosStatistic(todosList: List<Todo>) {
        viewModelScope.launch {
            updateTodosStatisticUseCase(todosList)
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

    fun deleteTodo(todo: Todo) {
        viewModelScope.launch {
            deleteTodoUseCase(todo)
            _todosList.value = getTodosListUseCase()!!
        }
    }

    fun changeCompletedState(todo: Todo) {
        viewModelScope.launch {
            val completedTodo = if (todo.isCompleted == false) {
                todo.copy(isCompleted = true)
            } else {
                todo.copy(isCompleted = false)
            }
            editTodoUseCase(completedTodo)
            _todosList.value = getTodosListUseCase()!!
        }
    }
}
