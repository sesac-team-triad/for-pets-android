package com.teamtriad.forpets.ui.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teamtriad.forpets.R
import com.teamtriad.forpets.data.source.network.User
import com.teamtriad.forpets.databinding.RvItemChatListBinding

class ChatListRecyclerViewAdapter(private val itemClickListener: OnItemClickListener) :
    ListAdapter<User, ChatListRecyclerViewAdapter.ViewHolder>(UserDiffCallback()) {

    interface OnItemClickListener {
        fun onItemClick(user: User)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RvItemChatListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: RvItemChatListBinding, private val itemClickListener: OnItemClickListener) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            binding.tvNickname.text = user.nickname

            binding.root.setOnClickListener {
                itemClickListener.onItemClick(user)
            }
        }
    }

    private class UserDiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.nickname == newItem.nickname
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.email == newItem.email
        }
    }
}
