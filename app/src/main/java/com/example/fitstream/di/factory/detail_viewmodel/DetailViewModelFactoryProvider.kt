package com.example.fitstream.di.factory.detail_viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import javax.inject.Inject

class DetailViewModelFactoryProvider @Inject constructor(
    private val factory: DetailViewModelFactory,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val handle = extras.createSavedStateHandle()
        return factory.create(handle) as T
    }
}