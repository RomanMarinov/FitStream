package com.example.fitstream.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VideoWorkout(
    val id: Int,
    val duration: String,
    val link: String
) : Parcelable
