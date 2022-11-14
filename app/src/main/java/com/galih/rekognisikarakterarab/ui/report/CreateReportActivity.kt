package com.galih.rekognisikarakterarab.ui.report

import android.view.LayoutInflater
import com.galih.rekognisikarakterarab.base.BaseActivity
import com.galih.rekognisikarakterarab.databinding.ActivityCreateReportBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateReportActivity : BaseActivity<ActivityCreateReportBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityCreateReportBinding =
        ActivityCreateReportBinding::inflate

    override fun setup() {
    }
}