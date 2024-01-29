package com.teamtriad.forpets.ui.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teamtriad.forpets.databinding.RvItemUserListBinding
import com.teamtriad.forpets.ui.chat.Users

class UserListRecyclerViewAdapter(
    private val itemClickListener: OnItemClickListener
) : ListAdapter<Users, UserListRecyclerViewAdapter.ViewHolder>(UserDiffCallback()) {

    interface OnItemClickListener {
        fun onItemClick(user: Users)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RvItemUserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: RvItemUserListBinding,
        private val itemClickListener: OnItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: Users) {
            binding.tvUser.text = user.nickname
            binding.root.setOnClickListener {
                itemClickListener.onItemClick(user)
            }
        }
    }
}

class UserDiffCallback : DiffUtil.ItemCallback<Users>() {
    override fun areItemsTheSame(oldItem: Users, newItem: Users): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Users, newItem: Users): Boolean {
        return oldItem == newItem
    }
}
