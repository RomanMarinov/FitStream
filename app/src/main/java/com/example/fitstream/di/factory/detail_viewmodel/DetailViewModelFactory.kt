package com.example.fitstream.di.factory.detail_viewmodel

import androidx.lifecycle.SavedStateHandle
import com.example.fitstream.presentation.detail_screen.DetailViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory

@AssistedFactory
interface DetailViewModelFactory {
    fun create(
        @Assisted savedStateHandle: SavedStateHandle,
    ): DetailViewModel
}