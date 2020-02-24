package com.andriiprudyus.myresume.sharedPreferences

import android.content.Context
import android.content.Context.MODE_PRIVATE

class CompanySharedPreferences(
    context: Context
) : BaseSharedPreferences(context, "CompanyPreferences", MODE_PRIVATE) {

    companion object {
        private const val LAST_LOAD_DATA_TIMESTAMP = "last_load_data_timestamp"
    }

    var lastLoadDataTimestamp: Long
        get() = getLong(LAST_LOAD_DATA_TIMESTAMP)
        set(value) {
            putLong(LAST_LOAD_DATA_TIMESTAMP, value)
        }
}