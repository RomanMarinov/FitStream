package com.example.fitstream.data.detail.remote

import com.example.fitstream.data.api_service.ApiService
import com.example.fitstream.data.util.Constants
import com.example.fitstream.domain.model.detail.Detail
import com.example.fitstream.domain.repository.DetailRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetailRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : DetailRepository {
    override suspend fun getVideoWorkout(id: Int): Result<Detail> {
       return try {
            val response = apiService.getVideoWorkout(id = id)
           if (response.isSuccessful) {
               val result = response.body()?.mapToDomain()
               if (result != null) {
                   Result.success(result)
               } else {
                   Result.failure(Exception(Constants.Detail.EMPTY_RESPONSE))
               }
           } else {
               Result.failure(Exception(Constants.Detail.ERROR_RESPONSE))
           }
        } catch (e: Exception) {
            Result.failure(Exception(e.message))
        }
    }
}