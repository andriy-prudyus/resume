package com.andriiprudyus.myresume.extension

import android.content.Context
import com.andriiprudyus.myresume.R
import com.andriiprudyus.myresume.error.AppException
import com.andriiprudyus.network.exception.ErrorCode
import com.andriiprudyus.network.exception.NetworkException
import com.andriiprudyus.myresume.error.ErrorCode as AppErrorCode

fun Throwable.toLocalizedErrorMessage(context: Context): String {
    return when (this) {
        is NetworkException -> context.getString(toStringRes())
        is AppException -> context.getString(toStringRes())
        else -> localizedMessage ?: context.getString(R.string.error_unknown)
    }
}

fun NetworkException.toStringRes(): Int {
    return when (errorCode) {
        ErrorCode.HTTP_NOT_FOUND -> R.string.error_incorrect_url
        ErrorCode.HTTP_INTERNAL_ERROR -> R.string.error_internal_server_error
        ErrorCode.NO_INTERNET -> R.string.error_no_internet
        ErrorCode.UNKNOWN -> R.string.error_unknown
    }
}

fun AppException.toStringRes(): Int {
    return when (errorCode) {
        AppErrorCode.INVALID_FILE_CONTENT -> R.string.error_invalid_file_content
    }
}