package com.example.fitstream

import android.app.Application
import com.example.fitstream.di.ApplicationComponent
import com.example.fitstream.di.DaggerApplicationComponent

class App : Application() {
    lateinit var appComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationComponent
            .builder()
            .context(applicationContext)
            .build()

    }
}