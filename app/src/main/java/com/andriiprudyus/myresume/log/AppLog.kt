package com.andriiprudyus.myresume.log

import android.util.Log
import com.andriiprudyus.myresume.BuildConfig

object AppLog {

    private const val TAG = "MY_RESUME"

    fun d(message: String) {
        if (BuildConfig.isLoggingEnabled) {
            Log.d(TAG, message)
        }
    }

    fun e(throwable: Throwable) {
        if (BuildConfig.isLoggingEnabled) {
            Log.e(TAG, throwable.message, throwable)
        }
    }
}