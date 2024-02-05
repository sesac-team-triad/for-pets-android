package com.teamtriad.forpets.data.source.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class KeyResponse(
    @Json(name = "name") val key: String,
)