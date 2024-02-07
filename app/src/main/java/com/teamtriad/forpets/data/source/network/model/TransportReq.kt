package com.teamtriad.forpets.data.source.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TransportReq(
    val uid: String,
    val title: String,
    @Json(name = "start-date") val startDate: String,
    @Json(name = "end-date") val endDate: String,
    val animal: String,
    val from: String,
    val to: String,
    val name: String,
    val age: String,
    val weight: String,
    val kind: String,
    @Json(name = "character-caution") val characterCaution: String,
    val message: String,
    @Json(ignore = true)
    var reqIndex: Int = -1,
)