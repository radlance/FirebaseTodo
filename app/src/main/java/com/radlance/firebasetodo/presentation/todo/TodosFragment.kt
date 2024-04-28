package com.radlance.firebasetodo.presentation.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.radlance.firebasetodo.R
import com.radlance.firebasetodo.databinding.FragmentTodosBinding
import com.radlance.firebasetodo.domain.entity.Todo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TodosFragment : Fragment() {
    private val viewModel: TodosViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()
    private var _binding: FragmentTodosBinding? = null
    private lateinit var rvTodosList: RecyclerView
    private lateinit var todosListAdapter: TodosListAdapter
    private val binding: FragmentTodosBinding
        get() = _binding ?: throw RuntimeException("TodosFragment == null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTodosList()
        viewModel.todosList.observe(viewLifecycleOwner) { todoList ->
            if (todoList.isEmpty()) {
                viewModel.addTodo()
            }
            binding.pbRv.visibility = View.INVISIBLE
            viewModel.updateTodosStatistic(todoList)
            todosListAdapter.todosList = todoList
            binding.fabAdd.setOnClickListener {
                launchNewTodoFragment(todoList.size + 1)
            }
        }
    }

    private fun setupTodosList() {
        mainViewModel.isUserAuthenticated.observe(viewLifecycleOwner) {
            if (it) {
                viewModel.getTodosList()
                rvTodosList = binding.rvTodos
                todosListAdapter = TodosListAdapter()
                with(rvTodosList) {
                    adapter = todosListAdapter
                }
                setupClickListeners()
            }
        }
    }
    private fun launchNewTodoFragment(todosListSize: Int) {
        requireActivity()
            .supportFragmentManager
            .beginTransaction()
            .replace(R.id.container_app, EditTodoFragment.newAddInstance(todosListSize))
            .addToBackStack(null)
            .commit()
    }

    private fun launchEditTodoFragment(todo: Todo) {
        requireActivity()
            .supportFragmentManager
            .beginTransaction()
            .replace(R.id.container_app, EditTodoFragment.newEditInstance(todo))
            .addToBackStack(null)
            .commit()
    }

    private fun setupClickListeners() {
        todosListAdapter.onCompleteClickListener = {
            viewModel.changeCompletedState(it)
            viewModel.updateTodosStatistic(todosListAdapter.todosList)
        }

        todosListAdapter.onDeleteClickListener = {
            viewModel.deleteTodo(it)
            viewModel.updateTodosStatistic(todosListAdapter.todosList)
        }

        todosListAdapter.onTodoClickListener = {
            launchEditTodoFragment(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): TodosFragment {
            return TodosFragment()
        }
    }
}