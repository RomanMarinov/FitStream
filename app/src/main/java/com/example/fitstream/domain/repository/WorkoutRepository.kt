package com.example.fitstream.domain.repository

import com.example.fitstream.domain.model.VideoWorkout
import com.example.fitstream.domain.model.Workout

interface WorkoutRepository {
    suspend fun getVideoWorkout(id: Int) : Result<VideoWorkout>
    suspend fun getWorkouts() : Result<List<Workout>>
}