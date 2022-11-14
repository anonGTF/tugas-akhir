package com.galih.rekognisikarakterarab.ui.main.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import com.galih.rekognisikarakterarab.base.BaseFragment
import com.galih.rekognisikarakterarab.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProfileBinding =
        FragmentProfileBinding::inflate

    override fun setup() {
        TODO("Not yet implemented")
    }

}