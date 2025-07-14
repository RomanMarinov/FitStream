package com.example.fitstream.di

import android.content.Context
import com.example.fitstream.utils.resource.AndroidResourceProvider
import com.example.fitstream.utils.resource.ResourceProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ResourceModule {
    @Provides
    @Singleton
    fun providesAndroidResourceProvider(@ApplicationContext context: Context) : ResourceProvider {
        return AndroidResourceProvider(context = context)
    }
}