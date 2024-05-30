package com.radlance.firebasetodo.presentation.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.radlance.firebasetodo.R
import com.radlance.firebasetodo.databinding.FragmentLoginBinding
import com.radlance.firebasetodo.presentation.todo.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private val viewModel: LoginViewModel by viewModels()
    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding
        get() = _binding ?: throw RuntimeException("FragmentWelcomeBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }
        binding.tvSingIn.setOnClickListener {
            launchRegistrationFragment()
        }
        binding.buttonLogin.setOnClickListener {
            authenticateUser()
        }
        observeLoginResult()
        handleInputErrors()
        setupChangedListeners()
        binding.tvForgetPassword.setOnClickListener {
            launchForgetPasswordFragment()
        }
    }

    private fun setupChangedListeners() {
        setTextChangedListener(binding.etEmail, viewModel::resetErrorInputEmail)
        setTextChangedListener(binding.etPassword, viewModel::resetErrorInputPassword)
    }

    private fun setTextChangedListener(editText: EditText, resetErrorFunction: () -> Unit) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                resetErrorFunction.invoke()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun launchRegistrationFragment() {
        findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
    }

    private fun launchForgetPasswordFragment() {
        findNavController().navigate(R.id.action_loginFragment_to_forgetPasswordFragment)
    }

    private fun authenticateUser() {
        viewModel.loginUser(
            email = binding.etEmail.text.toString(),
            password = binding.etPassword.text.toString()
        )
    }

    private fun observeLoginResult() {
        viewModel.isSuccessfulLogin.observe(viewLifecycleOwner) {
            when (it) {
                is FireBaseUiState.Success<*> -> {
                    val intent =
                        Intent(requireActivity().applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }

                is FireBaseUiState.Error -> it.showErrorMessage(requireActivity().applicationContext)
            }
        }
    }

    private fun handleInputErrors() {
        viewModel.errorInputEmail.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.error_input_email)
            } else {
                null
            }
            binding.tilEmail.error = message
        }

        viewModel.errorInputPassword.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.error_password_length)
            } else {
                null
            }
            binding.tilPassword.error = message
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}