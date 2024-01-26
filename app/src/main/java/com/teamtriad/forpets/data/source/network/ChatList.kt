package com.teamtriad.forpets.data.source.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChatList(
    val roomId: String,
    val friendName: String,
    val lastMessage: String,
    val lastMessageTime: String,
    val lastMessageCount: Int
)