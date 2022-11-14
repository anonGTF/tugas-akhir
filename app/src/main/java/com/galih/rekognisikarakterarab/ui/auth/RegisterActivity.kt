package com.galih.rekognisikarakterarab.ui.auth

import android.view.LayoutInflater
import com.galih.rekognisikarakterarab.base.BaseActivity
import com.galih.rekognisikarakterarab.databinding.ActivityRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : BaseActivity<ActivityRegisterBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityRegisterBinding =
        ActivityRegisterBinding::inflate

    override fun setup() {
        TODO("Not yet implemented")
    }
}