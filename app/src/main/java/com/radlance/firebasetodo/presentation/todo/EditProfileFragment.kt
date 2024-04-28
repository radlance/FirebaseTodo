package com.radlance.firebasetodo.presentation.todo

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.radlance.firebasetodo.R
import com.radlance.firebasetodo.databinding.FragmentEditProfileBinding
import com.radlance.firebasetodo.domain.entity.User
import com.radlance.firebasetodo.presentation.auth.FireBaseUiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileFragment : Fragment() {
    private var _binding: FragmentEditProfileBinding? = null
    private val profileViewModel: ProfileViewModel by viewModels()
    private lateinit var user: User
    private var imageUri = ""
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private val binding: FragmentEditProfileBinding
        get() = _binding ?: throw RuntimeException("EditProfileFragment == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
        resultLauncher = createResultLauncher()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleInputErrors()
        setupChangedListeners()
        observeLoadState()
        binding.ivBackButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        setupUserInfo()

        binding.ivProfileIcon.setOnClickListener {
            upLoadImage()
        }

        binding.buttonSave.setOnClickListener {
            saveNewUserInfo()
        }

        binding.tvDeleteAccount.setOnClickListener {
            launchProfileDeleteFragment()
        }
    }

    private fun observeLoadState() {
        profileViewModel.isReadyToSave.observe(viewLifecycleOwner) {
            binding.buttonSave.text = getString(R.string.saving)
        }
    }
    private fun setupChangedListeners() {
        setTextChangedListener(binding.etName, profileViewModel::resetInputName)
//        setTextChangedListener(binding.etEmail, profileViewModel::resetErrorInputEmail)
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

    @Suppress("DEPRECATION")
    private fun parseArgs() {
        requireArguments().getParcelable<User>(KEY_EDIT_PROFILE)?.let {
            user = it
        }
    }

    private fun setupUserInfo() {
        binding.etName.setText(user.name)
//        binding.etEmail.setText(user.email)
        if (user.imageUrl.isNotBlank()) {
            Glide.with(this).load(user.imageUrl).into(binding.ivProfileIcon)
            binding.ivEmptyImage.visibility = View.INVISIBLE
        }
    }

    private fun upLoadImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        resultLauncher.launch(intent)
    }

    private fun createResultLauncher(): ActivityResultLauncher<Intent> {
        var path: Uri?
        return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                path = result.data?.data
                path?.let { path ->
                    binding.ivProfileIcon.setImageURI(path)
                    binding.ivEmptyImage.visibility = View.INVISIBLE
                    imageUri = path.toString()
                }
            }
        }
    }

    private fun launchProfileDeleteFragment() {
        requireActivity()
            .supportFragmentManager
            .beginTransaction()
            .replace(R.id.container_app, ProfileDeleteFragment.newInstance())
            .commit()
    }

    private fun saveNewUserInfo() {
        val imageUrl = if (imageUri.isNotBlank()) {
            profileViewModel.uploadImageUri(
                name = binding.etName.text.toString(),
                email = user.email.toString(),
                imageUri = Uri.parse(imageUri)
            )
            imageUri
        } else {
            profileViewModel.uploadImageUri(
                name = binding.etName.text.toString(),
                email = user.email.toString()
            )
            user.imageUrl
        }
        val user = User(
            name = binding.etName.text.toString(),
            email = user.email.toString(),
            imageUrl = imageUrl
        )
        profileViewModel.isSuccessfulLoadUserImage.observe(viewLifecycleOwner) {
            when (it) {
                is FireBaseUiState.Success<*> -> {
                    requireActivity().supportFragmentManager.apply {
                        beginTransaction()
                            .replace(
                                R.id.container_app,
                                ProfileFragment.newInstanceWithSavedData(user)
                            )
                            .commit()
                    }
                }

                is FireBaseUiState.Error -> {
                    Toast.makeText(
                        requireActivity().applicationContext,
                        getText(R.string.error_saving),
                        Toast.LENGTH_LONG
                    ).show()
                    binding.buttonSave.text = getString(R.string.save)
                }
            }
        }
    }

    private fun handleInputErrors() {

        profileViewModel.errorInputName.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.error_input_name)
            } else {
                null
            }
            binding.tilName.error = message
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val KEY_EDIT_PROFILE = "edit_profile"
        fun newInstance(user: User): EditProfileFragment {
            return EditProfileFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_EDIT_PROFILE, user)
                }
            }
        }
    }
}