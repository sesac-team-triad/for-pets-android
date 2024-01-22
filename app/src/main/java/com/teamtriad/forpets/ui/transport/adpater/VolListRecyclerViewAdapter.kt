package com.teamtriad.forpets.ui.transport.adpater

import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teamtriad.forpets.databinding.RvItemVolListBinding
import com.teamtriad.forpets.model.tmp.Volunteer

class VolListRecyclerViewAdapter :
    ListAdapter<Volunteer, VolListRecyclerViewAdapter.VolListViewHolder>(DiffCallback) {

    private lateinit var binding: RvItemVolListBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VolListViewHolder {
        binding = RvItemVolListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VolListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VolListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class VolListViewHolder(private val binding: RvItemVolListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            with(binding) {
                cvVolList.setOnClickListener {
                    TransitionManager.beginDelayedTransition(cvVolList, AutoTransition())

                    if (clHiddenLayout.visibility == View.VISIBLE) {
                        clHiddenLayout.visibility = View.GONE
                        groupShortenedLocationName.visibility = View.VISIBLE
                    } else {
                        clHiddenLayout.visibility = View.VISIBLE
                        groupShortenedLocationName.visibility = View.GONE
                    }
                }
            }
        }

        fun bind(volunteer: Volunteer) {
            with(binding) {
                tvName.text = volunteer.name
                tvCountyFrom.text = volunteer.shortenedLocationFrom
                tvCountyTo.text = volunteer.shortenedLocationTo
                tvDate.text = volunteer.date
                tvDetailLocationFrom.text = volunteer.detailFrom
                tvDetailLocationTo.text = volunteer.detailTo
                tvMessage.text = volunteer.message
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Volunteer>() {
            override fun areItemsTheSame(oldItem: Volunteer, newItem: Volunteer): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Volunteer, newItem: Volunteer): Boolean {
                return oldItem.message == newItem.message
            }

        }
    }
}