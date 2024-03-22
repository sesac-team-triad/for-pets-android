package com.teamtriad.forpets.data.source.network.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    val email: String,
    val password: String,
    val nickname: String,
)