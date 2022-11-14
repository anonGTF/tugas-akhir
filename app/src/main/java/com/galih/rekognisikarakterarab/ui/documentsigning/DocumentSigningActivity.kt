package com.galih.rekognisikarakterarab.ui.documentsigning

import android.view.LayoutInflater
import com.galih.rekognisikarakterarab.base.BaseActivity
import com.galih.rekognisikarakterarab.databinding.ActivityDocumentSigningBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DocumentSigningActivity : BaseActivity<ActivityDocumentSigningBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityDocumentSigningBinding =
        ActivityDocumentSigningBinding::inflate

    override fun setup() {
        TODO("Not yet implemented")
    }
}