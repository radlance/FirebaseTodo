package com.radlance.firebasetodo.presentation.todo

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.radlance.firebasetodo.R
import com.radlance.firebasetodo.databinding.ActivityMainBinding
import com.radlance.firebasetodo.presentation.auth.StartActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observeCurrentUser()
        if (isFragmentContainerEmpty()) {
            replaceFragment(TodosFragment.newInstance())
        }
        launchBottomNavigationFragment()
//        logout()
    }

    override fun onResume() {
        super.onResume()
        viewModel.isUserAuthenticated()
    }

    private fun isFragmentContainerEmpty(): Boolean {
        return supportFragmentManager.findFragmentById(R.id.container_app) == null
    }

    private fun observeCurrentUser() {
        viewModel.isUserAuthenticated.observe(this) {
            if (!it) {
                val intent = Intent(this, StartActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun launchBottomNavigationFragment() {
        binding.bottomNavMain.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.item_profile -> {
                    replaceFragment(ProfileFragment.newInstance())
                    true
                }
                R.id.item_todo_list -> {
                    replaceFragment(TodosFragment.newInstance())
                    true
                }
            else -> throw RuntimeException("Undefined menu item $menuItem")
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container_app, fragment)
            .commit()
    }

//    private fun logout() {
//        binding.ivLogout.setOnClickListener {
//            viewModel.logoutUser()
//        }
//    }
}