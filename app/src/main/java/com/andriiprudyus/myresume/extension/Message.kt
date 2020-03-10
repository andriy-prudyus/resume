package com.andriiprudyus.myresume.extension

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun showErrorSnackbar(view: View, e: Throwable, duration: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(view, e.toLocalizedErrorMessage(view.context), duration).show()
}