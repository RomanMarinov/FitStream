package com.example.fitstream.domain.repository

import com.example.fitstream.domain.model.workout.Workout

interface WorkoutRepository {
    suspend fun getWorkouts(): Result<List<Workout>>
}