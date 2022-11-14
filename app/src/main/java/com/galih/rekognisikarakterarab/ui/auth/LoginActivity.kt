package com.galih.rekognisikarakterarab.ui.auth

import android.view.LayoutInflater
import com.galih.rekognisikarakterarab.base.BaseActivity
import com.galih.rekognisikarakterarab.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityLoginBinding =
        ActivityLoginBinding::inflate

    override fun setup() {
        TODO("Not yet implemented")
    }

}