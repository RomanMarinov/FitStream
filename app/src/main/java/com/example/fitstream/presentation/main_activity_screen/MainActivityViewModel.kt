package com.example.fitstream.presentation.main_activity_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitstream.domain.model.VideoWorkout
import com.example.fitstream.domain.model.Workout
import com.example.fitstream.domain.repository.WorkoutRepository
import com.example.fitstream.presentation.util.VideoStreamSingleLiveEvent
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

sealed class VideoUIState {
    data class Success(val workoutVideo: VideoWorkout) : VideoUIState()
    data object Empty : VideoUIState()
    data class Error(val message: String) : VideoUIState()
    data object Loading : VideoUIState()
}

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    private val _workoutsState = MutableStateFlow<WorkoutUiState>(WorkoutUiState.Loading)
    val workoutsState: StateFlow<WorkoutUiState> = _workoutsState

    private var _videWorkoutUIState = VideoStreamSingleLiveEvent<VideoUIState>()
    val videWorkoutUIState: VideoStreamSingleLiveEvent<VideoUIState> = _videWorkoutUIState


    init {
        getWorkouts()
    }

    fun getWorkouts() {
        viewModelScope.launch(Dispatchers.IO) {
            _workoutsState.value = WorkoutUiState.Loading
            val result = workoutRepository.getWorkouts()

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

    fun getVideWorkout(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _videWorkoutUIState.postEvent(VideoUIState.Loading)

            val result = workoutRepository.getVideoWorkout(id = id)
            result.onSuccess { videoWorkout ->
                _videWorkoutUIState.postEvent(VideoUIState.Success(videoWorkout))
            }.onFailure { error ->
                _videWorkoutUIState.postEvent(
                    VideoUIState.Error(
                        error.message ?: "Ошибка загрузки видео"
                    )
                )
            }
        }
    }
}