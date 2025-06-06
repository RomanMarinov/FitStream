package com.example.fitstream.data.workout

import com.example.fitstream.data.util.ApiService
import com.example.fitstream.domain.model.VideoWorkout
import com.example.fitstream.domain.model.Workout
import com.example.fitstream.domain.repository.WorkoutRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : WorkoutRepository {
    override suspend fun getVideoWorkout(id: Int): Result<VideoWorkout> {
       return try {
            val response = apiService.getVideoWorkout(id = id)
           if (response.isSuccessful) {
               val result = response.body()?.mapToDomain()
               if (result != null) {
                   Result.success(result)
               } else {
                   Result.failure(Exception("Пустой ответ от сервера"))
               }
           } else {
               Result.failure(Exception("Ошибка запроса"))
           }
        } catch (e: Exception) {
            Result.failure(Exception(e.message))
        }
    }

    override suspend fun getWorkouts(): Result<List<Workout>> {
        return try {
            val response = apiService.getWorkouts()

            if (response.isSuccessful) {
                val result = response.body()?.map { it.mapToDomain( ) }
                if (!result.isNullOrEmpty()) {
                    Result.success(result)
                } else {
                    Result.success(emptyList())
                }
            } else {
                Result.failure(Exception("Ошибка"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}