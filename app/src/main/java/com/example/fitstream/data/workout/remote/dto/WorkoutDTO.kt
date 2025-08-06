package com.example.fitstream.data.workout.remote.dto

import com.example.fitstream.domain.model.workout.Workout
import com.example.fitstream.domain.model.workout.WorkoutType
import com.google.gson.annotations.SerializedName

data class WorkoutDTO(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("type")
    val type: Int,
    @SerializedName("duration")
    val duration: String
) {
    fun mapToDomain() = Workout(
        id = id,
        title = title,
        description = description,
        type = WorkoutType.fromId(type)
            ?: throw IllegalArgumentException("Unknown workout type: $type"),
        duration = duration
    )
}

