package com.teamtriad.forpets.ui.chat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teamtriad.forpets.databinding.RvItemChatRoomBinding
import com.teamtriad.forpets.ui.chat.ChatRoomFragment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatRoomRecyclerViewAdapter(private val currentUserID: String) :
    ListAdapter<ChatRoomFragment.ChatMessage, ChatRoomRecyclerViewAdapter.ViewHolder>(
        MessageDiffCallback()
    ) {
    fun addMessage(message: ChatRoomFragment.ChatMessage) {
        submitList(currentList + listOf(message))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RvItemChatRoomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = getItem(position)
        holder.bind(message, currentUserID)
    }

    class ViewHolder(private val binding: RvItemChatRoomBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: ChatRoomFragment.ChatMessage, currentUserID: String) {
            if (message.senderUid == currentUserID) {

                binding.tvMyMessage.visibility = View.VISIBLE
                binding.tvMyMessage.text = message.content
                binding.tvMyMessageTime.text = message.time

                binding.ivFriend.visibility = View.GONE
                binding.tvFriendName.visibility = View.GONE
                binding.tvFriendMessage.visibility = View.GONE
                binding.tvFriendMessageTime.visibility = View.GONE
            } else {
                binding.tvFriendName.text = message.senderName
                binding.tvFriendMessage.visibility = View.VISIBLE
                binding.tvFriendMessage.text = message.content
                binding.tvFriendMessageTime.text = message.time

                binding.tvMyMessage.visibility = View.GONE
                binding.tvMyMessageTime.visibility = View.GONE
            }
        }
    }

    class MessageDiffCallback : DiffUtil.ItemCallback<ChatRoomFragment.ChatMessage>() {
        override fun areItemsTheSame(
            oldItem: ChatRoomFragment.ChatMessage,
            newItem: ChatRoomFragment.ChatMessage
        ): Boolean {
            return oldItem.content == newItem.content && oldItem.senderUid == newItem.senderUid
        }

        override fun areContentsTheSame(
            oldItem: ChatRoomFragment.ChatMessage,
            newItem: ChatRoomFragment.ChatMessage
        ): Boolean {
            return oldItem == newItem
        }
    }
}
