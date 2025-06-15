package com.example.fitstream.domain.model.detail

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Detail(
    val id: Int,
    val duration: String,
    val link: String
) : Parcelable
