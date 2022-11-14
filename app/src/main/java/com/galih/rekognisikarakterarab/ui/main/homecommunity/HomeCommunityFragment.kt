package com.galih.rekognisikarakterarab.ui.main.homecommunity

import android.view.LayoutInflater
import android.view.ViewGroup
import com.galih.rekognisikarakterarab.base.BaseFragment
import com.galih.rekognisikarakterarab.databinding.FragmentHomeCommunityBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeCommunityFragment : BaseFragment<FragmentHomeCommunityBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeCommunityBinding =
        FragmentHomeCommunityBinding::inflate

    override fun setup() {
        TODO("Not yet implemented")
    }
}