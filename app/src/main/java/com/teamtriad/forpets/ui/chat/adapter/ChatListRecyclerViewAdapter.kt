package com.teamtriad.forpets.ui.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teamtriad.forpets.data.source.network.ChatList
import com.teamtriad.forpets.databinding.RvItemChatListBinding

class ChatListRecyclerViewAdapter(private val itemClickListener: OnItemClickListener) :
    ListAdapter<ChatList, ChatListRecyclerViewAdapter.ViewHolder>(ChatListDiffCallback()) {

    interface OnItemClickListener {
        fun onItemClick(chatList: ChatList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RvItemChatListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: RvItemChatListBinding,
        private val itemClickListener: OnItemClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(chatList: ChatList) {
            binding.tvFriendName.text = chatList.friendName
            binding.tvLastMessage.text = chatList.lastMessage
            binding.tvLastMessageTime.text = chatList.lastMessageTime
            binding.tvUnreadMessageCount.text = chatList.unreadMessageCount.toString()
            binding.root.setOnClickListener {
                itemClickListener.onItemClick(chatList)
            }
        }
    }

    private class ChatListDiffCallback : DiffUtil.ItemCallback<ChatList>() {
        override fun areItemsTheSame(oldItem: ChatList, newItem: ChatList): Boolean {
            return oldItem.roomId == newItem.roomId
        }

        override fun areContentsTheSame(oldItem: ChatList, newItem: ChatList): Boolean {
            return oldItem.lastMessage == newItem.lastMessage &&
                    oldItem.lastMessageTime == newItem.lastMessageTime &&
                    oldItem.unreadMessageCount == newItem.unreadMessageCount
        }
    }
}
