package com.galih.rekognisikarakterarab.ui.main.homeuser

import android.view.LayoutInflater
import android.view.ViewGroup
import com.galih.rekognisikarakterarab.base.BaseFragment
import com.galih.rekognisikarakterarab.databinding.FragmentHomeUserBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeUserFragment : BaseFragment<FragmentHomeUserBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeUserBinding =
        FragmentHomeUserBinding::inflate

    override fun setup() {
        TODO("Not yet implemented")
    }

}