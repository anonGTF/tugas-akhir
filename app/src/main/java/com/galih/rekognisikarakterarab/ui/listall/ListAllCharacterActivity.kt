package com.galih.rekognisikarakterarab.ui.listall

import android.view.LayoutInflater
import com.galih.rekognisikarakterarab.base.BaseActivity
import com.galih.rekognisikarakterarab.databinding.ActivityListAllCharacterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListAllCharacterActivity : BaseActivity<ActivityListAllCharacterBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityListAllCharacterBinding =
        ActivityListAllCharacterBinding::inflate

    override fun setup() {
    }
}