package com.example.fitstream.data.workout.remote.dto

import com.example.fitstream.domain.model.workout.Workout
import com.example.fitstream.domain.model.workout.WorkoutType

//data class WorkoutDTO(
//    val id: Int,
//    val title: String,
//    val description: String?,
//    val type: Int,
//    val duration: String
//) {
//    fun mapToDomain() : Workout {
//        return Workout(
//            id = id,
//            title = title,
//            description = description,
//            type = type,
//            duration = duration
//        )
//    }
//}

data class WorkoutDTO(
    val id: Int,
    val title: String,
    val description: String?,
    val type: Int,
    val duration: String
) {
    fun mapToDomain() = Workout(
        id = id,
        title = title,
        description = description,
        type = WorkoutType.fromId(type) ?: throw IllegalArgumentException("Unknown workout type: $type"),
        duration = duration
    )
}

