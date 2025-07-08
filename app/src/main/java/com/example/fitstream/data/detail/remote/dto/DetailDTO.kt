package com.example.fitstream.data.detail.remote.dto

import com.example.fitstream.domain.model.detail.Detail
import com.google.gson.annotations.SerializedName

data class DetailDTO(
    @SerializedName("id")
    val id: Int,
    @SerializedName("duration")
    val duration: String,
    @SerializedName("link")
    val link: String
) {
    fun mapToDomain() : Detail {
        return Detail(
            id = id,
            duration = duration,
            link = link
        )
    }
}
