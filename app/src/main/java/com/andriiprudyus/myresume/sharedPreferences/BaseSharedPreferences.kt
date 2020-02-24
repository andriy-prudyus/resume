package com.andriiprudyus.myresume.sharedPreferences

import android.content.Context
import android.content.SharedPreferences

abstract class BaseSharedPreferences(
    private val context: Context,
    private val fileName: String,
    private val mode: Int
) {

    private val preferences: SharedPreferences
        get() = context.getSharedPreferences(fileName, mode)

    fun getLong(key: String, defaultValue: Long = 0L): Long = preferences.getLong(key, defaultValue)

    fun getInt(key: String, defaultValue: Int = 0): Int = preferences.getInt(key, defaultValue)

    fun getFloat(key: String, defaultValue: Float = 0f): Float {
        return preferences.getFloat(key, defaultValue)
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return preferences.getBoolean(key, defaultValue)
    }

    fun getString(key: String, defaultValue: String? = null): String? {
        return preferences.getString(key, defaultValue)
    }

    fun putLong(key: String, value: Long) {
        preferences.edit().putLong(key, value).apply()
    }

    fun putInt(key: String, value: Int) {
        preferences.edit().putInt(key, value).apply()
    }

    fun putFloat(key: String, value: Float) {
        preferences.edit().putFloat(key, value).apply()
    }

    fun putBoolean(key: String, value: Boolean) {
        preferences.edit().putBoolean(key, value).apply()
    }

    fun putString(key: String, value: String) {
        preferences.edit().putString(key, value).apply()
    }
}