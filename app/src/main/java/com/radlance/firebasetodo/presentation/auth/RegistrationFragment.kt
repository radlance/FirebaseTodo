package com.radlance.firebasetodo.presentation.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import com.google.android.material.textfield.TextInputLayout
import com.radlance.firebasetodo.R
import com.radlance.firebasetodo.databinding.FragmentRegistrationBinding
import com.radlance.firebasetodo.presentation.todo.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationFragment : Fragment() {
    private val viewModel: RegistrationViewModel by viewModels()
    private var _binding: FragmentRegistrationBinding? = null
    private val binding: FragmentRegistrationBinding
        get() = _binding ?: throw RuntimeException("FragmentWelcomeBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivBackButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        registerUser()
        observeRegistrationResult()
        binding.tvSingIn.setOnClickListener {
            launchLoginFragment()
        }
        handleInputErrors()
        setupChangedListeners()
    }

    private fun registerUser() {
        binding.buttonRegister.setOnClickListener {
            viewModel.registerUser(
                name = binding.etName.text.toString(),
                email = binding.etEmail.text.toString(),
                password = binding.etPassword.text.toString(),
                confirmPassword = binding.etConfirmPassword.text.toString()
            )
        }
    }

    private fun setupChangedListeners() {
        setTextChangedListener(binding.etName, viewModel::resetInputName)
        setTextChangedListener(binding.etEmail, viewModel::resetErrorInputEmail)
        setTextChangedListener(binding.etPassword, viewModel::resetErrorInputPassword)
        setTextChangedListener(binding.etConfirmPassword, viewModel::resetErrorInputConfirmPassword)
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

    private fun handleInputErrors() {
        handleInputError(viewModel.errorInputName, binding.tilName, R.string.error_input_name)
        handleInputError(viewModel.errorInputEmail, binding.tilEmail, R.string.error_input_email)
        handleInputError(
            viewModel.errorInputPassword,
            binding.tilPassword,
            R.string.error_password_length
        )
        handleInputError(
            viewModel.errorInputConfirmPassword,
            binding.tilConfirmPassword,
            R.string.error_confirm_password
        )
    }

    private fun handleInputError(
        error: LiveData<Boolean>,
        view: TextInputLayout,
        errorMessage: Int
    ) {
        error.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(errorMessage)
            } else {
                null
            }
            view.error = message
        }
    }

    private fun observeRegistrationResult() {
        viewModel.isSuccessfulRegistration.observe(viewLifecycleOwner) {
            when (it) {
                is FireBaseUiState.Success<*> -> {
                    val intent =
                        Intent(requireActivity().applicationContext, MainActivity::class.java)
                    startActivity(intent)
                }

                is FireBaseUiState.Error -> it.showErrorMessage(requireActivity().applicationContext)
            }
        }
    }

    private fun launchLoginFragment() {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.container_auth, LoginFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): RegistrationFragment {
            return RegistrationFragment()
        }
    }
}