package com.radlance.firebasetodo.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.radlance.firebasetodo.R
import com.radlance.firebasetodo.databinding.FragmentVerifyEmailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfirmEmailFragment : Fragment() {
    private var _binding: FragmentVerifyEmailBinding? = null
    private val binding: FragmentVerifyEmailBinding
        get() = _binding ?: throw RuntimeException("ConfirmEmailFragment == null")

    private val viewModel: ConfirmEmailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVerifyEmailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }
        super.onViewCreated(view, savedInstanceState)
        viewModel.sendConfirmEmail()
        binding.buttonLogin.setOnClickListener {
            launchLoginFragment()
        }
    }

    private fun launchLoginFragment() {
        requireActivity().supportFragmentManager.apply {
            beginTransaction()
            .replace(R.id.container_auth, LoginFragment.newInstance())
            .commit()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): ConfirmEmailFragment {
            return ConfirmEmailFragment()
        }
    }
}