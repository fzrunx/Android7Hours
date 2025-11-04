package com.sesac.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class PostModelSerialize(
    var title: String = "",
    var content: String = "",
    val imageResList: List<Int>? = null,
//    var create_at: Date,
    var status: Boolean = false,
): Parcelable