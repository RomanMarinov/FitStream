package com.example.fitstream.data.workout

import com.example.fitstream.data.api_service.ApiService
import com.example.fitstream.data.util.Constants
import com.example.fitstream.domain.model.workout.Workout
import com.example.fitstream.domain.repository.WorkoutRepository
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : WorkoutRepository {

    override suspend fun getWorkouts(): Result<List<Workout>> {
        return try {
            val response = apiService.getWorkouts()
            if (response.isSuccessful) {
                val result = response.body()?.map { it.mapToDomain() }
                if (!result.isNullOrEmpty()) {
                    Result.success(result)
                } else {
                    Result.success(emptyList())
                }
            } else {
                Result.failure(Exception(Constants.Workout.ERROR_RESPONSE))
            }
        } catch (e: Exception) {
            val exceptionMessage = when (e) {
                is IOException -> Constants.Workout.ERROR_NETWORK
                is HttpException -> Constants.Workout.ERROR_FROM_SERVER.plus(e.code())
                else -> Constants.Workout.ERROR_UNKNOWN
            }
            Result.failure(Exception(exceptionMessage))
        }
    }
}