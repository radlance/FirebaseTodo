package com.radlance.firebasetodo.presentation.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.radlance.firebasetodo.databinding.FragmentTodosBinding

class TodosFragment : Fragment() {
    private var _binding: FragmentTodosBinding? = null
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