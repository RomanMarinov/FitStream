package com.example.fitstream.data.workout.remote.dto

import com.example.fitstream.domain.model.VideoWorkout

data class VideoWorkoutDTO(
    val id: Int,
    val duration: String,
    val link: String
) {
    fun mapToDomain() : VideoWorkout {
        return VideoWorkout(
            id = id,
            duration = duration,
            link = link
        )
    }
}
