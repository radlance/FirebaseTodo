package com.radlance.firebasetodo.presentation.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.radlance.firebasetodo.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
    }
}