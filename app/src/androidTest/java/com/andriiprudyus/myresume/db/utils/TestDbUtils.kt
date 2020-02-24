package com.andriiprudyus.myresume.db.utils

import android.content.Context
import androidx.annotation.RawRes
import com.google.gson.Gson
import java.lang.reflect.Type

fun readContent(context: Context, @RawRes resource: Int): String {
    return context.resources.openRawResource(resource).use {
        it.bufferedReader().readText()
    }
}

fun <T> fromJson(context: Context, @RawRes resource: Int, type: Type): T {
    return Gson().fromJson(readContent(context, resource), type)
}