package com.teamtriad.forpets.ui.chat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teamtriad.forpets.data.source.network.ChatRoom
import com.teamtriad.forpets.databinding.RvItemChatRoomBinding

class ChatRoomRecyclerViewAdapter(private val currentUserId: String) :
    ListAdapter<ChatRoom, ChatRoomRecyclerViewAdapter.ViewHolder>(ChatRoomDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RvItemChatRoomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chatRoom = getItem(position)
        holder.bind(chatRoom, currentUserId)
    }

    inner class ViewHolder(private val binding: RvItemChatRoomBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(chatRoom: ChatRoom, currentUserId: String) {
            if (chatRoom.senderName == currentUserId) {
                binding.tvMyMessage.text = chatRoom.messageContent
                binding.tvMyMessageTime.text = chatRoom.sentTime

                // 상대방의 메세지 뷰들을 숨김
                binding.tvFriendName.visibility = View.GONE
                binding.tvFriendMessage.visibility = View.GONE
                binding.tvFriendMessageTime.visibility = View.GONE
            } else {
                binding.tvFriendName.text = chatRoom.senderName
                binding.tvFriendMessage.text = chatRoom.messageContent
                binding.tvFriendMessageTime.text = chatRoom.sentTime

                // 나의 메세지 뷰들을 숨김
                binding.tvMyMessage.visibility = View.GONE
                binding.tvMyMessageTime.visibility = View.GONE
            }
        }
    }

    private class ChatRoomDiffCallback : DiffUtil.ItemCallback<ChatRoom>() {
        override fun areItemsTheSame(oldItem: ChatRoom, newItem: ChatRoom): Boolean {
            return oldItem.messageId == newItem.messageId
        }

        override fun areContentsTheSame(oldItem: ChatRoom, newItem: ChatRoom): Boolean {
            return oldItem == newItem
        }
    }
}
