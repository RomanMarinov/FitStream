package com.example.fitstream.domain.repository

import com.example.fitstream.domain.model.detail.Detail

interface DetailRepository {
    suspend fun getVideoWorkout(id: Int) : Result<Detail>
}