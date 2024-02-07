package com.teamtriad.forpets.util

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val monToMm = mapOf(
    "Jan" to "01", "Feb" to "02", "Mar" to "03", "Apr" to "04", "May" to "05", "Jun" to "06",
    "Jul" to "07", "Aug" to "08", "Sep" to "09", "Oct" to "10", "Nov" to "11", "Dec" to "12"
)

fun Long.formatDate(): String {
    val dateFormat = SimpleDateFormat("MM/dd", Locale.getDefault())
    return dateFormat.format(this)
}

fun Long.formatDateWithYear(): String {
    val dateFormat = SimpleDateFormat("yy/MM/dd", Locale.getDefault())
    return dateFormat.format(this)
}

fun String.toHttps(): String {
    return if (startsWith("HTTPS://", true)) this
    else if (startsWith("HTTP://", true)
    ) "https:/" + substring(6)      // substring(6)은 "HTTP://"의 두 번째 슬래시부터 시작하는 문자열임.
    else "https://${this}"
}

fun String.toRoughLocation() = split(" ").filter { it != "" }
    .let {
        it[0] + " " + it[1]
    }

fun Date.toYyyyMmDd() = toString().let {
    it.takeLast(4) + monToMm[it.substring(4, 7)] + it.substring(8, 10)
}

fun ImageView.glide(urlString: String) {
    Glide.with(context)
        .load(urlString.toHttps())
        .into(this)
}

fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}