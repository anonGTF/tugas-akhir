package com.galih.makhrajulhuruf.ui.detail

import android.view.LayoutInflater
import android.widget.MediaController
import com.galih.makhrajulhuruf.base.BaseActivity
import com.galih.makhrajulhuruf.data.model.ArabCharacter
import com.galih.makhrajulhuruf.databinding.ActivityDetailBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : BaseActivity<ActivityDetailBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityDetailBinding =
        ActivityDetailBinding::inflate

    override fun setup() {
        val data: ArabCharacter? = intent.getParcelableExtra(EXTRA_DATA)
        if (data != null) {
            populateData(data)
        }
    }

    private fun populateData(data: ArabCharacter) {
        with(binding) {
            tvName.text = data.name
            Picasso.get().load(data.image).into(imgCharacter)
            video.setVideoPath(data.video)
            val mediaController = MediaController(this@DetailActivity)
            mediaController.setAnchorView(video)
            mediaController.setMediaPlayer(video)
            video.setMediaController(mediaController)
            video.start()

            setupBackButton()
            setTitle("Karakter ${data.name}")
        }
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}