package com.teamtriad.forpets.ui.chat

import androidx.lifecycle.ViewModel

class ChatViewModel : ViewModel() {
    private var roomId: String? = null

    fun getRoomId(): String? {
        return roomId
    }

    fun setRoomId(newRoomId: String) {
        roomId = newRoomId
    }
}