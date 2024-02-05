package com.teamtriad.forpets.data.source.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CompletedDate(
    @Json(name = "completed-date") val completedDate: String,
)