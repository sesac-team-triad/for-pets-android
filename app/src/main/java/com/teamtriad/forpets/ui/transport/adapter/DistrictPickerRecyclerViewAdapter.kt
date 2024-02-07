package com.teamtriad.forpets.ui.transport.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teamtriad.forpets.databinding.RvItemVolDistrictBinding
import com.teamtriad.forpets.ui.transport.bottomSheetDialog.OnClickListener

class DistrictPickerRecyclerViewAdapter(private val listener: OnClickListener) :
    ListAdapter<String, DistrictPickerRecyclerViewAdapter.DistrictViewHolder>(DiffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DistrictViewHolder(
        RvItemVolDistrictBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )


    override fun onBindViewHolder(holder: DistrictViewHolder, position: Int) {
        with(holder) {
            bind(getItem(position))
            binding.ibDelete.setOnClickListener {
                listener.deleteItem(getItem(position))
            }
        }
    }

    class DistrictViewHolder(val binding: RvItemVolDistrictBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(district: String) {
            binding.tvDistrictName.text = district
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }

}