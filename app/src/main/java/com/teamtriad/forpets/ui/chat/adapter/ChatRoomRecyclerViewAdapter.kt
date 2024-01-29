package com.teamtriad.forpets.ui.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teamtriad.forpets.databinding.RvItemChatRoomBinding
import com.teamtriad.forpets.ui.chat.ChatRoomFragment

class ChatRoomRecyclerViewAdapter :
    ListAdapter<ChatRoomFragment.ChatMessage, ChatRoomRecyclerViewAdapter.ViewHolder>(
        MessageDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RvItemChatRoomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = getItem(position)
        holder.bind(message)
    }

    class ViewHolder(private val binding: RvItemChatRoomBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: ChatRoomFragment.ChatMessage) {
            binding.tvFriendName.text = message.content
            binding.tvFriendMessage.text = message.senderName
        }
    }
}

class MessageDiffCallback : DiffUtil.ItemCallback<ChatRoomFragment.ChatMessage>() {
    override fun areItemsTheSame(
        oldItem: ChatRoomFragment.ChatMessage,
        newItem: ChatRoomFragment.ChatMessage
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: ChatRoomFragment.ChatMessage,
        newItem: ChatRoomFragment.ChatMessage
    ): Boolean {
        return oldItem == newItem
    }
}
