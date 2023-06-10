package com.galih.makhrajulhuruf.data.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.galih.makhrajulhuruf.base.BaseModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArabCharacter(
    val name: String,
    @DrawableRes
    val image: Int,
    val video: String,
    val audio: String,
    val labels: List<String>
): BaseModel(name.hashCode()), Parcelable
