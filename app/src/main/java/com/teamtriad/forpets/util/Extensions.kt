package com.teamtriad.forpets.util

import java.util.Date

private val monToMm = mapOf(
    "Jan" to "01", "Feb" to "02", "Mar" to "03", "Apr" to "04", "May" to "05", "Jun" to "06",
    "Jul" to "07", "Aug" to "08", "Sep" to "09", "Oct" to "10", "Nov" to "11", "Dec" to "12"
)

fun Date.toYyyyMmDd() = toString().let {
    it.takeLast(4) + monToMm[it.substring(4, 7)] + it.substring(8, 10)
}