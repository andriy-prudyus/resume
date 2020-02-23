package com.andriiprudyus.myresume

import android.app.Application
import com.andriiprudyus.myresume.di.Injector

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Injector.init(this)
    }
}