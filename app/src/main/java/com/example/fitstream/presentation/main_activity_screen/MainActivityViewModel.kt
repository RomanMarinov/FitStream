package com.example.fitstream.presentation.main_activity_screen

import android.util.Log
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

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    private var _workouts: MutableStateFlow<List<Workout>> = MutableStateFlow(emptyList())
    val workouts: StateFlow<List<Workout>> = _workouts

    private var _videWorkout = VideoStreamSingleLiveEvent<VideoWorkout>()
    val videWorkout: VideoStreamSingleLiveEvent<VideoWorkout> = _videWorkout

    init {
        getWorkouts()
    }

    private fun getWorkouts() {
        viewModelScope.launch(Dispatchers.IO) {
            val workouts = workoutRepository.getWorkouts()
            workouts?.let {
                _workouts.value = it
                Log.d("4444", " getWorkouts it=$it")
            }
        }
    }

    fun getVideWorkout(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val videWorkout = workoutRepository.getVideoWorkout(id = id)
            videWorkout?.let {
                Log.d("4444", " getVideWorkout it=" + it)
                _videWorkout.postEvent(it)
            }
        }
    }
}