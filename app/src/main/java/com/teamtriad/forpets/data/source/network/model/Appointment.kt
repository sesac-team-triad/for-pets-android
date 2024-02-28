package com.teamtriad.forpets.data.source.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Appointment(
    val name: String,
    val date: String,
    val time: String,
    val from: String,
    val to: String,
    @Json(name = "req-uid") val reqUid: String,
    @Json(name = "vol-uid") val volUid: String,
    @Json(name = "transport-req-key") val transportReqKey: String,
    val progress: Int,
    @Json(name = "completed-date") val completedDate: String,
)