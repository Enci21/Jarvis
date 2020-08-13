package com.example.jarvis

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class Jarvis : Application() {


    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@Jarvis)
            modules(listOf(appModule))
        }
    }
}