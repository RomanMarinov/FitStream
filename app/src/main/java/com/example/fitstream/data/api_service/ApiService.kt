package com.example.fitstream.data.api_service

import com.example.fitstream.data.workout.remote.dto.DetailDTO
import com.example.fitstream.data.workout.remote.dto.WorkoutDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("get_workouts")
    suspend fun getWorkouts(): Response<List<WorkoutDTO>>

    @GET("get_video")
    suspend fun getVideoWorkout(
        @Query("id") id: Int
    ): Response<DetailDTO>
}