package com.radlance.firebasetodo.presentation.todo

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.radlance.firebasetodo.R
import com.radlance.firebasetodo.databinding.FragmentEditTodoBinding
import com.radlance.firebasetodo.domain.entity.Todo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditTodoFragment : Fragment() {
    private val viewModel: EditTodoViewModel by viewModels()
    private var _binding: FragmentEditTodoBinding? = null
    private val binding: FragmentEditTodoBinding
        get() = _binding ?: throw RuntimeException("EditTodoFragment == null")
    private lateinit var todo: Todo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivBackButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        handleInputError()
        setupChangeListener()
        binding.etTitle.setText(todo.title)
        binding.buttonSave.setOnClickListener {
            viewModel.editTodo(todo.copy(title = binding.etTitle.text.toString()))
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun setupChangeListener() {
        binding.etTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputTodo()
            }

            override fun afterTextChanged(s: Editable?) {}

        })
    }

    private fun handleInputError() {
        viewModel.errorInputTodo.observe(viewLifecycleOwner) {
            val message = if(it) {
                getString(R.string.error_todo)
            } else {
                null
            }
            binding.tilTitle.error = message
        }
    }

    @Suppress("DEPRECATION")
    private fun parseArgs() {
        requireArguments().getParcelable<Todo>(KEY_EDIT_TODO)?.let {
            todo = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val KEY_EDIT_TODO = "edit_todo"
        fun newInstance(todo: Todo): EditTodoFragment {
            return EditTodoFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_EDIT_TODO, todo)
                }
            }
        }
    }
}