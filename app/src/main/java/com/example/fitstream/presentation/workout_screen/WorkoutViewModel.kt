package com.example.fitstream.presentation.workout_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitstream.domain.model.workout.Workout
import com.example.fitstream.domain.model.workout.WorkoutType
import com.example.fitstream.domain.repository.WorkoutRepository
import com.example.fitstream.presentation.util.Constants
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

    private val _baseWorkouts = mutableListOf<Workout>()

    private val workoutsSaved: List<Workout>
        get() = savedStateHandle[Constants.WorkoutKeys.WORKOUTS] ?: emptyList()

    private val workoutsSavedByTitle: List<Workout>
        get() = savedStateHandle[Constants.WorkoutKeys.WORKOUTS_BY_TITLE] ?: emptyList()

    val selectedTypeSaved: String
        get() = savedStateHandle[Constants.WorkoutKeys.SELECTED_TYPE] ?: ""

    private val textQuerySaved: String
        get() = savedStateHandle[Constants.WorkoutKeys.TEXT_QUERY] ?: ""

    init {
        getWorkouts()
    }

    fun getWorkouts() {
        viewModelScope.launch(Dispatchers.IO) {
            _workoutsState.value = WorkoutUiState.Loading
            val result = workoutRepository.getWorkouts()
            result.onSuccess { workouts ->
                if (workouts.isNotEmpty()) {
                    _baseWorkouts.addAll(workouts)
                    setWorkoutsStateSuccess(workouts = workouts)
                } else {
                    _workoutsState.value = WorkoutUiState.Empty
                }
            }.onFailure { error ->
                _workoutsState.value = WorkoutUiState.Error(error.message ?: Constants.GetWorkoutResponse.ERROR)
            }
        }
    }

    fun filteringWorkoutsByType(selectedType: String) {
        savedStateHandle[Constants.WorkoutKeys.SELECTED_TYPE] = selectedType
        val alreadyDescription = _baseWorkouts.any { it.type.description == selectedType }
        if (alreadyDescription) {
            val workoutFilteredByType: List<Workout> =
                _baseWorkouts.filter { it.type.description == selectedType }
            savedStateHandle[Constants.WorkoutKeys.WORKOUTS] = workoutFilteredByType
            setWorkoutsStateSuccess(workouts = workoutFilteredByType)
        } else {
            savedStateHandle[Constants.WorkoutKeys.WORKOUTS] = _baseWorkouts
            setWorkoutsStateSuccess(workouts = _baseWorkouts)
        }
    }

    fun filteringWorkoutsByTitle(textQuery: String) {
        savedStateHandle[Constants.WorkoutKeys.TEXT_QUERY] = textQuery
        val filteringWorkoutsByTitle: List<Workout> = workoutsSaved.filter {
            it.title.contains(
                textQuery,
                ignoreCase = true
            )
        }

        savedStateHandle[Constants.WorkoutKeys.WORKOUTS_BY_TITLE] = filteringWorkoutsByTitle
        val workoutsStart = if (textQuery.isEmpty()) {
            savedStateHandle[Constants.WorkoutKeys.WORKOUTS_BY_TITLE] = emptyList<Workout>()
            workoutsSaved
        } else {
            filteringWorkoutsByTitle
        }

        setWorkoutsStateSuccess(workouts = workoutsStart)
    }

    fun setWorkoutsState(workouts: List<Workout>) {
        if (workoutsSavedByTitle.isEmpty() && textQuerySaved.isEmpty()) {
            savedStateHandle[Constants.WorkoutKeys.WORKOUTS] = workouts
        }
    }

    private fun setWorkoutsStateSuccess(workouts: List<Workout>) {
        val resolvedWorkouts = when {
            workoutsSavedByTitle.isNotEmpty() -> workoutsSavedByTitle
            textQuerySaved.isNotEmpty() -> workoutsSavedByTitle
            workoutsSaved.isNotEmpty() -> workoutsSaved
            else -> workouts
        }

        _workoutsState.value = WorkoutUiState.Success(
            workouts = resolvedWorkouts,
            workoutsType = getSortedWorkoutType()
        )
    }

    private fun getSortedWorkoutType(): List<WorkoutType> {
        val workoutsType = _baseWorkouts
            .map { it.type }
            .distinct()
            .sorted()
            .plus(WorkoutType.ALL)
        return workoutsType
    }
}