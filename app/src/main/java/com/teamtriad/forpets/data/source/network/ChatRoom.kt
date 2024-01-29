package com.teamtriad.forpets.data.source.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChatRoom(
    val chats: Map<String, ChatRoom>,
    val members: Map<String, Map<String, Boolean>>,
    val messages: Map<String, Map<String, Message>>
)

@JsonClass(generateAdapter = true)
data class Message(
    val sender: String,
    val content: String,
    val timestamp: String,
    val isMyMessage: Boolean
)