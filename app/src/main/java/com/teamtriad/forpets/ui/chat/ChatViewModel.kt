package com.teamtriad.forpets.ui.chat

import androidx.lifecycle.ViewModel

class ChatViewModel : ViewModel() {
    private var roomKey: String? = null

    fun getRoomKey(): String? {
        return roomKey
    }

    fun setRoomId(newRoomId: String) {
        roomKey = newRoomId
    }
}