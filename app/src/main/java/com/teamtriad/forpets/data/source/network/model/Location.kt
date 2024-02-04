package com.teamtriad.forpets.data.source.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Location(
    val latitude: String,
    val longitude: String,
    @Json(name = "transport-req-count") val transportReqCount: Int,
    @Json(name = "transport-vol-count") val transportVolCount: Int,
)