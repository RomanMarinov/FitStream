package com.example.fitstream.presentation.detail_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitstream.R
import com.example.fitstream.domain.model.detail.Detail
import com.example.fitstream.domain.repository.DetailRepository
import com.example.fitstream.presentation.util.Constants
import com.example.fitstream.utils.resource.ResourceProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class VideoUIState {
    data class Success(val workoutVideo: Detail) : VideoUIState()
    data object Empty : VideoUIState()
    data class Error(val message: String) : VideoUIState()
    data object Loading : VideoUIState()
}

class DetailViewModel @AssistedInject constructor(
    private val detailRepository: DetailRepository,
    private val resourceProvider: ResourceProvider,
    @Assisted private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val isPlaying: Boolean
        get() = savedStateHandle[Constants.PlayerKeys.IS_PLAYING] ?: false

    val position: Long
        get() = savedStateHandle[Constants.PlayerKeys.POSITION] ?: 0L

    val playWhenReady: Boolean
        get() = savedStateHandle[Constants.PlayerKeys.PLAY_WHEN_READY] ?: true

    private var _videoWorkoutUIState = MutableStateFlow<VideoUIState>(VideoUIState.Loading)
    val videoWorkoutUIState: StateFlow<VideoUIState> = _videoWorkoutUIState

    fun getVideoWorkout(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = detailRepository.getVideoWorkout(id = id)
            result.onSuccess { videoWorkout ->
                _videoWorkoutUIState.value = VideoUIState.Success(videoWorkout)
            }.onFailure { error ->
                _videoWorkoutUIState.value =
                    VideoUIState.Error(
                        error.message
                            ?: resourceProvider.getResourceString(R.string.error_download_video)
                    )
            }
        }
    }

    fun setCurrentPlayback(currentPlayback: CurrentPlayback) {
        savedStateHandle[Constants.PlayerKeys.POSITION] = currentPlayback.position
        savedStateHandle[Constants.PlayerKeys.PLAY_WHEN_READY] = currentPlayback.playWhenReady
    }

    fun setPlayingState(isPlaying: Boolean) {
        savedStateHandle[Constants.PlayerKeys.IS_PLAYING] = isPlaying
    }
}