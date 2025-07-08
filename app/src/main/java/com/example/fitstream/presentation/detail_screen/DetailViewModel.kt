package com.example.fitstream.presentation.detail_screen

import android.content.Context
import androidx.annotation.OptIn
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import com.example.fitstream.R
import com.example.fitstream.domain.model.detail.Detail
import com.example.fitstream.domain.repository.DetailRepository
import com.example.fitstream.presentation.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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

@OptIn(UnstableApi::class)
@HiltViewModel
class DetailViewModel @OptIn(UnstableApi::class)
@Inject constructor(
    private val detailRepository: DetailRepository,
    private val savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context
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
                    VideoUIState.Error(error.message ?: context.getString(R.string.error_download_video))
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