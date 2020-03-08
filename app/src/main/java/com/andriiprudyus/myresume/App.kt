package com.andriiprudyus.myresume

import android.app.Application
import com.andriiprudyus.myresume.di.AppComponent
import com.andriiprudyus.myresume.di.DaggerAppComponent
import timber.log.Timber

class App : Application() {

    val appComponent: AppComponent by lazy { initAppComponent() }

    override fun onCreate() {
        super.onCreate()
        initLogging()
    }

    private fun initLogging() {
        if (BuildConfig.isLoggingEnabled) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initAppComponent(): AppComponent {
        return DaggerAppComponent.factory().create(applicationContext)
    }
}