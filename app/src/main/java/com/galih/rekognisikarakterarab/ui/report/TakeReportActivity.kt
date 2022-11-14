package com.galih.rekognisikarakterarab.ui.report

import android.view.LayoutInflater
import com.galih.rekognisikarakterarab.base.BaseActivity
import com.galih.rekognisikarakterarab.databinding.ActivityTakeReportBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TakeReportActivity : BaseActivity<ActivityTakeReportBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityTakeReportBinding =
        ActivityTakeReportBinding::inflate

    override fun setup() {
    }
}