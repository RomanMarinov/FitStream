package com.example.fitstream.di

import com.example.fitstream.data.detail.remote.DetailRepositoryImpl
import com.example.fitstream.data.workout.WorkoutRepositoryImpl
import com.example.fitstream.domain.repository.DetailRepository
import com.example.fitstream.domain.repository.WorkoutRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindWorkoutRepository(workoutRepositoryImpl: WorkoutRepositoryImpl): WorkoutRepository

    @Binds
    @Singleton
    fun bindDetailRepository(detailRepositoryImpl: DetailRepositoryImpl): DetailRepository
}