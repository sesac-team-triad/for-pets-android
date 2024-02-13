package com.teamtriad.forpets.ui.transport.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teamtriad.forpets.data.source.network.model.Moving
import com.teamtriad.forpets.databinding.RvItemMovingListBinding
import com.teamtriad.forpets.viewmodel.ProfileViewModel
import com.teamtriad.forpets.viewmodel.TransportViewModel
import kotlinx.coroutines.launch

class MovingListRecyclerViewAdapter(
    private val lifecycleScope: LifecycleCoroutineScope,
    private val transportViewModel: TransportViewModel,
    private val profileViewModel: ProfileViewModel
) : ListAdapter<Moving, MovingListRecyclerViewAdapter.ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        RvItemMovingListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        lifecycleScope.launch {
            val data = getItem(position)
            val appointment = transportViewModel.getAppointmentByKey(data.appointmentKey)!!

            holder.bind(
                appointment.name,
                profileViewModel.getUserNicknameByUid(appointment.reqUid),
                profileViewModel.getUserNicknameByUid(data.volUid),
                data.from,
                data.to
            )
        }
    }

    class ViewHolder(private val binding: RvItemMovingListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            name: String,
            reqNickname: String?,
            volNickname: String?,
            from: String,
            to: String
        ) {
            with(binding) {
                tvName.text = name
                tvAssociate.text = "${reqNickname}, ${volNickname}"
                tvJourney.text = "${from} -> ${to}"
            }
        }
    }

    companion object {

        val diffCallback = object : DiffUtil.ItemCallback<Moving>() {

            override fun areItemsTheSame(oldItem: Moving, newItem: Moving): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Moving, newItem: Moving): Boolean {
                return oldItem == newItem
            }
        }
    }
}