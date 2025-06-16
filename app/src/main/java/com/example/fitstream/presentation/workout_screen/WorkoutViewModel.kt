package com.example.fitstream.presentation.workout_screen

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitstream.domain.model.workout.Workout
import com.example.fitstream.domain.model.workout.WorkoutType
import com.example.fitstream.domain.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class WorkoutUiState {
    data object Loading : WorkoutUiState()
    data class Success(val workouts: List<Workout>, val workoutsType: List<WorkoutType>) :
        WorkoutUiState()

    data class Error(val message: String) : WorkoutUiState()
    data object Empty : WorkoutUiState()
}

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {


    private val _workoutsState = MutableStateFlow<WorkoutUiState>(WorkoutUiState.Loading)
    val workoutsState: StateFlow<WorkoutUiState> = _workoutsState

    private val _workouts = mutableListOf<Workout>()
    val workouts: List<Workout>
        get() = savedStateHandle["workouts"] ?: emptyList()

    init {
        getWorkouts()
    }

    fun getWorkouts() {
        viewModelScope.launch(Dispatchers.IO) {
            _workoutsState.value = WorkoutUiState.Loading
            val result = workoutRepository.getWorkouts()
            result.onSuccess { workouts ->
                if (workouts.isNotEmpty()) {
                    Log.d("4444", " getWorkouts workouts=" + workouts)

                    _workouts.addAll(workouts)
                    setWorkoutsStateSuccess(workouts = workouts)
                } else {
                    _workoutsState.value = WorkoutUiState.Empty
                }
            }.onFailure { error ->
                _workoutsState.value = WorkoutUiState.Error(error.message ?: "ошибка")
            }
        }
    }

    fun filteringWorkoutsByType(selectedType: String) {
        val alreadyDescription = _workouts.any { it.type.description == selectedType }
        if (!alreadyDescription) {
            setWorkoutsStateSuccess(workouts = _workouts)
        } else {
            val filtered: List<Workout> = _workouts.filter { it.type.description == selectedType }
            setWorkoutsStateSuccess(workouts = filtered)
        }
    }

    fun filteringWorkoutsByTitle(textQuery: String) {
        if (textQuery.isEmpty()) {
            setWorkoutsStateSuccess(workouts = _workouts)
        } else {


            val filteringWorkoutsByTitle: List<Workout> = workouts.filter {
                it.title.contains(
                    textQuery,
                    ignoreCase = true
                )
            }
            setWorkoutsStateSuccess(workouts = filteringWorkoutsByTitle)
        }
    }

    fun setWorkoutsState(workouts: List<Workout>) {
        savedStateHandle["workouts"] = workouts
    }

    private fun setWorkoutsStateSuccess(workouts: List<Workout>) {
        _workoutsState.value = WorkoutUiState.Success(
            workouts = workouts,
            workoutsType = getSortedWorkoutType()
        )
    }

    private fun getSortedWorkoutType(): List<WorkoutType> {
        val workoutsType = _workouts
            .map { it.type }
            .distinct()
            .sorted()
        return workoutsType
    }
}