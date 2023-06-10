package com.galih.makhrajulhuruf.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import com.galih.makhrajulhuruf.base.BaseAdapter
import com.galih.makhrajulhuruf.data.model.ArabCharacter
import com.galih.makhrajulhuruf.databinding.ItemCharacterBinding
import com.squareup.picasso.Picasso

class ArabCharacterAdapter : BaseAdapter<ItemCharacterBinding, ArabCharacter>() {
    override val bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> ItemCharacterBinding =
        ItemCharacterBinding::inflate

    override fun bind(binding: ItemCharacterBinding, item: ArabCharacter) {
        with(binding) {
            tvName.text = item.name
            Picasso.get().load(item.image).into(imgCharacter)
        }
    }
}