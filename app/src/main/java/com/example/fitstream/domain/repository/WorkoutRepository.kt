package com.example.fitstream.domain.repository

import com.example.fitstream.domain.model.VideoWorkout
import com.example.fitstream.domain.model.Workout

interface WorkoutRepository {
    suspend fun getVideoWorkout(id: Int) : VideoWorkout?
    suspend fun getWorkouts() : List<Workout>?
}