package com.galih.rekognisikarakterarab.ui.donate

import android.view.LayoutInflater
import com.galih.rekognisikarakterarab.base.BaseActivity
import com.galih.rekognisikarakterarab.databinding.ActivityWithdrawDonationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WithdrawDonationActivity : BaseActivity<ActivityWithdrawDonationBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityWithdrawDonationBinding =
        ActivityWithdrawDonationBinding::inflate

    override fun setup() {
        TODO("Not yet implemented")
    }
}