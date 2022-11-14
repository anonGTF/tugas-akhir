package com.galih.rekognisikarakterarab.ui.report

import android.view.LayoutInflater
import com.galih.rekognisikarakterarab.base.BaseActivity
import com.galih.rekognisikarakterarab.databinding.ActivityDetailReportBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailReportActivity : BaseActivity<ActivityDetailReportBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityDetailReportBinding =
        ActivityDetailReportBinding::inflate

    override fun setup() {
    }
}