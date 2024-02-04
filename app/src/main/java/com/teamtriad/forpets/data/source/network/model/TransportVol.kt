package com.teamtriad.forpets.data.source.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TransportVol(
    val uid: String,
    val title: String,
    @Json(name = "start-date") val startDate: String,
    @Json(name = "end-date") val endDate: String,
    val animal: String,
    val from: String,
    @Json(name = "from-detail") val fromDetail: String,
    val to: String,
    @Json(name = "to-detail") val toDetail: String,
    val message: String,
)