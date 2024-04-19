package com.radlance.firebasetodo.presentation.todo

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.radlance.firebasetodo.R
import com.radlance.firebasetodo.databinding.FragmentProfileBinding
import com.radlance.firebasetodo.domain.entity.User
import com.radlance.firebasetodo.presentation.auth.FireBaseUiState
import com.radlance.firebasetodo.presentation.auth.MainViewModel
import com.radlance.firebasetodo.presentation.auth.StartActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private val mainViewModel: MainViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()
    private var user: User? = null
    private var imageUri: String = ""
    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
        get() = _binding ?: throw RuntimeException("ProfileFragment == null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }
        if (user == null) {
            observeLoadResult()
        } else {
            setupSavedInfo()
        }
        binding.ivLogout.setOnClickListener {
            observeCurrentUser()
            mainViewModel.logoutUser()
        }

        binding.buttonEditProfile.setOnClickListener {
            launchEditProfileFragment()
        }
    }

    private fun observeLoadResult() {
        profileViewModel.loadUserInfo()
        profileViewModel.isSuccessfulLoad.observe(viewLifecycleOwner) {
            binding.pbImage.visibility = View.VISIBLE
            when (it) {
                is FireBaseUiState.Success<*> -> {
                    binding.tvName.text = (it.value as User).name
                    binding.tvEmail.text = it.value.email
                    if (it.value.imageUrl.isNotBlank()) {
                        Glide.with(this).load(it.value.imageUrl).into(binding.ivProfileIcon)
                    }
                    binding.pbImage.visibility = View.INVISIBLE
                    imageUri = it.value.imageUrl
                }

                is FireBaseUiState.Error -> {
                    it.showErrorMessage(requireActivity().applicationContext)
                }
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun parseArgs() {
        try {
            if (requireArguments().containsKey(NEW_INFO_KEY)) {
                requireArguments().getParcelable<User>(NEW_INFO_KEY)?.let {
                    user = it
                }
            }
        } catch (_: Exception) {
        }
    }

    private fun setupSavedInfo() {
        user?.let { user ->
            binding.tvName.text = user.name
            binding.tvEmail.text = user.email
            if(user.imageUrl.contains("firebasestorage")) {
                Glide.with(this).load(user.imageUrl).into(binding.ivProfileIcon)
            }
            else {
                binding.ivProfileIcon.setImageURI(Uri.parse(user.imageUrl))
            }
            imageUri = user.imageUrl
            binding.pbImage.visibility = View.INVISIBLE
        }
    }
//    private fun observeCurrentIcon() {
//        profileViewModel.isSuccessfulLoadUserImage.observe(viewLifecycleOwner) {
//            when (it) {
//                is FireBaseUiState.Success<*> -> {
//                    Glide.with(this).load(it.value).into(binding.ivProfileIcon)
//                }
//            }
//        }
//    }

    private fun observeCurrentUser() {
        mainViewModel.isUserAuthenticated.observe(viewLifecycleOwner) {
            if (!it) {
                val intent = Intent(requireActivity().applicationContext, StartActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }

    private fun launchEditProfileFragment() {
        requireActivity()
            .supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.container_app,
                EditProfileFragment.newInstance(
                    User(
                        binding.tvName.text.toString(),
                        binding.tvEmail.text.toString(),
                        imageUri
                    )
                )
            )
            .addToBackStack(FRAGMENT_NAME)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val FRAGMENT_NAME = "ProfileFragment"
        private const val NEW_INFO_KEY = "new_info_key"
        fun newInstanceWithSavedData(user: User): ProfileFragment {
            return ProfileFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(NEW_INFO_KEY, user)
                }
            }
        }

        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }
}