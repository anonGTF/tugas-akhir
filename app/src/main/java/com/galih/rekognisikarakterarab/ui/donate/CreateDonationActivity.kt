package com.galih.rekognisikarakterarab.ui.donate

import android.view.LayoutInflater
import com.galih.rekognisikarakterarab.base.BaseActivity
import com.galih.rekognisikarakterarab.databinding.ActivityCreateDonationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateDonationActivity : BaseActivity<ActivityCreateDonationBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityCreateDonationBinding =
        ActivityCreateDonationBinding::inflate

    override fun setup() {
        TODO("Not yet implemented")
    }
}