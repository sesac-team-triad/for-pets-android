package com.teamtriad.forpets.util

import java.text.SimpleDateFormat
import java.util.Locale

fun Long.formatDate(): String {
    val dateFormat = SimpleDateFormat("MM/dd", Locale.getDefault())
    return dateFormat.format(this)
}

fun Long.formatDateWithYear(): String {
    val dateFormat = SimpleDateFormat("yy/MM/dd", Locale.getDefault())
    return dateFormat.format(this)
}