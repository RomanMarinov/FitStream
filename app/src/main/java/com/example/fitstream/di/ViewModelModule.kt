package com.example.fitstream.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
object ViewModelModule {

    @Provides
    fun provideContext(context: Context): Context = context
}