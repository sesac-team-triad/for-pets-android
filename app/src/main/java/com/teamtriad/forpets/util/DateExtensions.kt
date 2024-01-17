package com.teamtriad.forpets.util

import java.text.SimpleDateFormat
import java.util.Locale

fun Long.formatDate(): String {
    val dateFormat = SimpleDateFormat("MM/dd", Locale.getDefault())
    return dateFormat.format(this)
}