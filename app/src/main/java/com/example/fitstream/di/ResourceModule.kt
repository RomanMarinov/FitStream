package com.example.fitstream.di

import com.example.fitstream.utils.resource.AndroidResourceProvider
import com.example.fitstream.utils.resource.ResourceProvider
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface ResourceModule {
    @Singleton
    @Binds
    fun bindsResource(androidResourceProvider: AndroidResourceProvider): ResourceProvider
}