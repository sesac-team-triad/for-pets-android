package com.teamtriad.forpets.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.formatDate(): String {
    val dateFormat = SimpleDateFormat("MM/dd", Locale.getDefault())
    return dateFormat.format(this)
}

fun Long.formatDateWithYear(): String {
    val dateFormat = SimpleDateFormat("yy/MM/dd", Locale.getDefault())
    return dateFormat.format(this)
}

private val monToMm = mapOf(
    "Jan" to "01", "Feb" to "02", "Mar" to "03", "Apr" to "04", "May" to "05", "Jun" to "06",
    "Jul" to "07", "Aug" to "08", "Sep" to "09", "Oct" to "10", "Nov" to "11", "Dec" to "12"
)

fun Date.toYyyyMmDd() = toString().let {
    it.takeLast(4) + monToMm[it.substring(4, 7)] + it.substring(8, 10)
}