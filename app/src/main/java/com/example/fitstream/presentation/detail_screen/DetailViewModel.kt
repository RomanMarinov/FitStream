package com.example.fitstream.presentation.detail_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitstream.domain.model.detail.Detail
import com.example.fitstream.domain.repository.DetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class VideoUIState {
    data class Success(val workoutVideo: Detail) : VideoUIState()
    data object Empty : VideoUIState()
    data class Error(val message: String) : VideoUIState()
    data object Loading : VideoUIState()
}

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val detailRepository: DetailRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val position: Long
        get() = savedStateHandle["position"] ?: 0L

    val playWhenReady: Boolean
        get() = savedStateHandle["playWhenReady"] ?: true

    private var _videoWorkoutUIState = MutableStateFlow<VideoUIState>(VideoUIState.Loading)
    val videoWorkoutUIState: StateFlow<VideoUIState> = _videoWorkoutUIState


    fun getVideWorkout(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = detailRepository.getVideoWorkout(id = id)
            result.onSuccess { videoWorkout ->
                _videoWorkoutUIState.value = VideoUIState.Success(videoWorkout)
            }.onFailure { error ->
                _videoWorkoutUIState.value = VideoUIState.Error(error.message ?: "Ошибка загрузки видео")
            }
        }
    }

    fun setPlaybackState(position: Long, playWhenReady: Boolean) {
        savedStateHandle["position"] = position
        savedStateHandle["playWhenReady"] = playWhenReady
    }
}