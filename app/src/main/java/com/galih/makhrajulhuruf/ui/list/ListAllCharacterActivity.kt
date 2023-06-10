package com.galih.makhrajulhuruf.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.galih.makhrajulhuruf.base.BaseActivity
import com.galih.makhrajulhuruf.databinding.ActivityListAllCharacterBinding
import com.galih.makhrajulhuruf.ui.detail.DetailActivity
import com.galih.makhrajulhuruf.utils.characters
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListAllCharacterActivity : BaseActivity<ActivityListAllCharacterBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityListAllCharacterBinding =
        ActivityListAllCharacterBinding::inflate

    private val characterAdapter: ArabCharacterAdapter by lazy { ArabCharacterAdapter() }

    override fun setup() {
        setupBackButton()
        setTitle("List Semua Karakter")
        setupRecyclerView()
        characterAdapter.differ.submitList(characters)
    }

    private fun setupRecyclerView() {
        with(binding.rvCharacters) {
            adapter = characterAdapter
            layoutManager = LinearLayoutManager(this@ListAllCharacterActivity)
        }

        characterAdapter.setOnItemClickListener {
            goToActivity(DetailActivity::class.java, Bundle().apply {
                putParcelable(DetailActivity.EXTRA_DATA, it)
            })
        }
    }
}