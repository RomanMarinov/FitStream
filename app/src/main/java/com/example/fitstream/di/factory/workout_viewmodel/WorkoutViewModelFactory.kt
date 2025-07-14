package com.example.fitstream.di.factory.workout_viewmodel

import androidx.lifecycle.SavedStateHandle
import com.example.fitstream.presentation.workout_screen.WorkoutViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory

@AssistedFactory
interface WorkoutViewModelFactory {
    fun create(@Assisted savedStateHandle: SavedStateHandle): WorkoutViewModel
}