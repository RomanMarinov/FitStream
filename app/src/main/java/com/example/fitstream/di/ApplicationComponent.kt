package com.example.fitstream.di

import android.content.Context
import com.example.fitstream.presentation.detail_screen.DetailFragment
import com.example.fitstream.presentation.workout_screen.WorkoutFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, RepositoryModule::class, ResourceModule::class])
interface ApplicationComponent {

    fun inject(fragment: WorkoutFragment)
    fun inject(fragment: DetailFragment)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder
        fun build(): ApplicationComponent
    }
}
