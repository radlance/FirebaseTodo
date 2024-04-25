package com.radlance.firebasetodo.presentation.todo

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.radlance.firebasetodo.R
import com.radlance.firebasetodo.databinding.FragmentDeleteAccountBinding
import com.radlance.firebasetodo.presentation.auth.FireBaseUiState
import com.radlance.firebasetodo.presentation.auth.StartActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileDeleteFragment : Fragment() {
    private var _binging: FragmentDeleteAccountBinding? = null
    private val binding: FragmentDeleteAccountBinding
        get() = _binging ?: throw RuntimeException("ProfileDeleteFragment")
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binging = FragmentDeleteAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setPasswordChangedListener()
        observeDeleteUserResult()
        binding.ivBackButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.buttonDelete.setOnClickListener {
            mainViewModel.deleteUser(binding.etPassword.text.toString())
        }
    }

    private fun setPasswordChangedListener() {
        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.tilPassword.error = null
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
    private fun observeDeleteUserResult() {
        mainViewModel.isSuccessfulDelete.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is FireBaseUiState.Success<*> -> {
                    mainViewModel.isUserAuthenticated.observe(viewLifecycleOwner) {
                        if (!it) {
                            val intent = Intent(requireActivity().applicationContext, StartActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
                        }
                    }
                }

                is FireBaseUiState.Error -> {
                    binding.tilPassword.error = getString(R.string.incorrect_password)
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binging = null
    }

    companion object {
        fun newInstance(): ProfileDeleteFragment {
            return ProfileDeleteFragment()
        }
    }
}