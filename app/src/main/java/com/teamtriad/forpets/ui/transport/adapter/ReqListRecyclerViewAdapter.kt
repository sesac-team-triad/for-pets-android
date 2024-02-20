package com.teamtriad.forpets.ui.transport.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teamtriad.forpets.data.source.network.model.TransportReq
import com.teamtriad.forpets.databinding.RvItemReqListBinding
import com.teamtriad.forpets.ui.transport.TransportListsFragmentDirections
import com.teamtriad.forpets.util.setSafeOnClickListener

class ReqListRecyclerViewAdapter :
    ListAdapter<TransportReq, ReqListRecyclerViewAdapter.ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        RvItemReqListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: RvItemReqListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: TransportReq) {
            itemView.setSafeOnClickListener {
                it.findNavController()
                    .navigate(
                        TransportListsFragmentDirections.actionTransportListsFragmentToTransportReqDetailFragment(
                            data.reqIndex
                        )
                    )
            }

            with(binding) {
                tvName.text = data.name
                tvDepartureDate.text = data.startDate.replace('/', '-') + " ~ " +
                        data.endDate.replace('/', '-')
                tvJourney.text = data.from + " -> " + data.to
            }
        }
    }

    companion object {

        val diffCallback = object : DiffUtil.ItemCallback<TransportReq>() {

            override fun areItemsTheSame(oldItem: TransportReq, newItem: TransportReq): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: TransportReq, newItem: TransportReq): Boolean {
                return oldItem == newItem
            }
        }
    }
}