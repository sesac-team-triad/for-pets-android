package com.teamtriad.forpets.data.source.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Moving(
    @Json(name = "appointment-key") val appointmentKey: String,
    val from: String,
    val to: String,
    @Json(name = "vol-uid") val volUid: String,
)