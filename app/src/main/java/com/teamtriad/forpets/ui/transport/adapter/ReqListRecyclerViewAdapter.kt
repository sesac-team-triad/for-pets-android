package com.teamtriad.forpets.ui.transport.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamtriad.forpets.databinding.RvItemReqListBinding

class ReqListRecyclerViewAdapter(private val dataSet: List<Any>) :
    RecyclerView.Adapter<ReqListRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        RvItemReqListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }

    override fun getItemCount() = dataSet.size

    class ViewHolder(binding: RvItemReqListBinding) :
        RecyclerView.ViewHolder(binding.root) {}
}