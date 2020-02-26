package com.andriiprudyus.myresume.utils

import java.text.DateFormat
import java.util.*

fun formattedDate(timestamp: Long, dateFormat: Int = DateFormat.DEFAULT): String {
    return if (timestamp < 1) "" else DateFormat.getDateInstance(dateFormat).format(Date(timestamp))
}