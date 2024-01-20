package com.teamtriad.forpets

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserData(
    val email: String,
    val password: String,
    val nickname: String
)