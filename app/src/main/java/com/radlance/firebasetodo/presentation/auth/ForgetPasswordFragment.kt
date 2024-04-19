package com.radlance.firebasetodo.presentation.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.radlance.firebasetodo.R
import com.radlance.firebasetodo.databinding.FragmentForgetPasswordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgetPasswordFragment : Fragment() {
    private val viewModel: ForgetPasswordViewModel by viewModels()
    private var _binding: FragmentForgetPasswordBinding? = null
    private val binding: FragmentForgetPasswordBinding
        get() = _binding ?: throw RuntimeException("FragmentForgetPasswordBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgetPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivBackButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        observeSendingResult()
        binding.buttonSend.setOnClickListener {
            sendEmail()
        }
        setEmailChangedListener()
        handleEmailError()
    }

    private fun setEmailChangedListener() {
        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputEmail()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

    }

    private fun sendEmail() {
        viewModel.sendEmail(binding.etEmail.text.toString())
    }

    private fun observeSendingResult() {
        viewModel.isSuccessfulSendEmail.observe(viewLifecycleOwner) {
            when (it) {
                is FireBaseUiState.Success<*> -> {
                    Toast.makeText(
                        requireActivity().applicationContext,
                        getText(R.string.instruction_message),
                        Toast.LENGTH_LONG
                    ).show()
                }

                is FireBaseUiState.Error -> {
                    Toast.makeText(
                        requireActivity().applicationContext,
                        getText(R.string.error_sending),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun handleEmailError() {
        viewModel.errorInputEmail.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.error_input_email)
            } else {
                null
            }
            binding.tilEmail.error = message
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance(): ForgetPasswordFragment {
            return ForgetPasswordFragment()
        }
    }
}