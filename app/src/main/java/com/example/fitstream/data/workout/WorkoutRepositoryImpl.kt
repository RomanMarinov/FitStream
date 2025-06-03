package com.example.fitstream.data.workout

import com.example.fitstream.data.util.ApiService
import com.example.fitstream.data.workout.remote.dto.VideoWorkoutDTO
import com.example.fitstream.domain.model.VideoWorkout
import com.example.fitstream.domain.model.Workout
import com.example.fitstream.domain.repository.WorkoutRepository
import com.google.android.exoplayer2.util.Log
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : WorkoutRepository {
    override suspend fun getVideoWorkout(id: Int): VideoWorkout? {
       return try {
            val response = apiService.getVideoWorkout(id = id)
            if (response.isSuccessful) {
                val result = response.body()
                result?.mapToDomain()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("Exception", "try catch getVideoWorkout e=$e")
            null
        }
    }

    override suspend fun getWorkouts(): List<Workout>? {
        return try {
            val response = apiService.getWorkouts()
            if (response.isSuccessful) {
                val result = response.body()
                result?.map { it.mapToDomain() }
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("Exception", "try catch getVideoWorkout e=$e")
            null
        }
    }
}