package com.teamtriad.forpets.ui.chat.adapter

import ChatList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemLongClickListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teamtriad.forpets.databinding.RvItemChatListBinding

class ChatListRecyclerViewAdapter(
    private val itemClickListener: OnItemClickListener,
    private val itemLongClickListener: OnItemLongClickListener
) : ListAdapter<ChatList, ChatListRecyclerViewAdapter.ViewHolder>(ChatListDiffCallback()) {

    interface OnItemClickListener {
        fun onItemClick(chatList: ChatList)
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(chatList: ChatList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RvItemChatListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, itemClickListener, itemLongClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chatList = getItem(position)
        holder.bind(chatList)
    }

    class ViewHolder(
        private val binding: RvItemChatListBinding,
        private val itemClickListener: OnItemClickListener,
        private val itemLongClickListener: OnItemLongClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(chatList: ChatList) {
            binding.tvFriendName.text = chatList.friendName
            binding.tvLastMessage.text = chatList.lastMessage
            binding.tvLastMessageTime.text = chatList.lastMessageTime

            if (chatList.unreadMessageCount == 0) {
                binding.tvUnreadMessageCount.visibility = View.GONE
            } else {
                binding.tvUnreadMessageCount.text = chatList.unreadMessageCount.toString()
            }


            binding.root.setOnClickListener {
                itemClickListener.onItemClick(chatList)
            }

            binding.root.setOnLongClickListener {
                itemLongClickListener.onItemLongClick(chatList)
                true
            }
        }
    }

    private class ChatListDiffCallback : DiffUtil.ItemCallback<ChatList>() {
        override fun areItemsTheSame(oldItem: ChatList, newItem: ChatList): Boolean {
            return oldItem.roomKey == newItem.roomKey && oldItem.lastMessageTime == newItem.lastMessageTime
        }

        override fun areContentsTheSame(oldItem: ChatList, newItem: ChatList): Boolean {
            return oldItem.lastMessage == newItem.lastMessage &&
                    oldItem.lastMessageTime == newItem.lastMessageTime &&
                    oldItem.unreadMessageCount == newItem.unreadMessageCount 
        }
    }

}
