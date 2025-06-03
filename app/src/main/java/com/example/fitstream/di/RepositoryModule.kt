package com.example.fitstream.di

import com.example.fitstream.data.workout.WorkoutRepositoryImpl
import com.example.fitstream.domain.repository.WorkoutRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindRepository(workoutRepositoryImpl: WorkoutRepositoryImpl): WorkoutRepository
}