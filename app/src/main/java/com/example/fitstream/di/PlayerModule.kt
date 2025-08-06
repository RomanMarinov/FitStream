package com.example.fitstream.di

import android.content.Context
import com.example.fitstream.presentation.detail_screen.ExoPlayerFacade
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PlayerModule(private val context: Context) {
    @Provides
    @Singleton
    fun providesExoPlayerFacade(): ExoPlayerFacade {
        return ExoPlayerFacade(context = context)
    }
}