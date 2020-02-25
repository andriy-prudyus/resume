package com.andriiprudyus.myresume

import android.app.Application
import com.andriiprudyus.myresume.di.Injector
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Injector.init(this)
        initLogging()
    }

    private fun initLogging() {
        if (BuildConfig.isLoggingEnabled) {
            Timber.plant(Timber.DebugTree())
        }
    }
}