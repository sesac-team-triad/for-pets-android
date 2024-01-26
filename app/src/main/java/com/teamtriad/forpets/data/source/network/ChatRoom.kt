package com.teamtriad.forpets.data.source.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChatRoom(
    val messageId: String,
    val senderName: String,
    val messageContent: String,
    val sentTime: String,
    val isMyMessage: Boolean
)