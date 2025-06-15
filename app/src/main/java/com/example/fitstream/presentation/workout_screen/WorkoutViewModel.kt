package com.example.fitstream.presentation.workout_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitstream.domain.model.detail.Detail
import com.example.fitstream.domain.model.workout.Workout
import com.example.fitstream.domain.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class WorkoutUiState {
    data object Loading : WorkoutUiState()
    data class Success(val workouts: List<Workout>) : WorkoutUiState()
    data class Error(val message: String) : WorkoutUiState()
    data object Empty : WorkoutUiState()
}

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    private val _workoutsState = MutableStateFlow<WorkoutUiState>(WorkoutUiState.Loading)
    val workoutsState: StateFlow<WorkoutUiState> = _workoutsState

    init {
        getWorkouts()
    }

    fun getWorkouts() {
        viewModelScope.launch(Dispatchers.IO) {
            _workoutsState.value = WorkoutUiState.Loading
            val result = workoutRepository.getWorkouts()
            Log.d("4444" , " result=" + result)
            result.onSuccess { workouts ->
                if (workouts.isNotEmpty()) {
                    _workoutsState.value = WorkoutUiState.Success(workouts)
                } else {
                    _workoutsState.value = WorkoutUiState.Empty
                }
            }.onFailure { error ->
                _workoutsState.value = WorkoutUiState.Error(error.message ?: "ошибка")
            }
        }
    }
}