package com.keepin.android.presentation.auth

import android.os.Bundle
import com.keepin.android.R
import com.keepin.android.base.BindingActivity
import com.keepin.android.databinding.ActivityAuthBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : BindingActivity<ActivityAuthBinding>(R.layout.activity_auth) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
