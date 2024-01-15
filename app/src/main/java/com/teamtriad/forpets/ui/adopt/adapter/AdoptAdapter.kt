package com.teamtriad.forpets.ui.adopt.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.teamtriad.forpets.data.source.network.AbandonmentInfo
import com.teamtriad.forpets.databinding.RvItemAdoptBinding

class AdoptAdapter(private val dataSet: List<AbandonmentInfo>) :
    RecyclerView.Adapter<AdoptAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        RvItemAdoptBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataSet[position])
    }

    override fun getItemCount() = dataSet.size

    class ViewHolder(private val binding: RvItemAdoptBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(abandonmentInfo: AbandonmentInfo) {
            fun String.toRough(): String {
                return split(" ").filter { it != "" }
                    .let {
                        it[0] + " " + it[1]
                    }
            }

            with(binding) {
                Glide.with(itemView.context)
                    .load(
                        abandonmentInfo.thumbnailImageUrl.replaceFirst("http:", "https:")
                    )
                    .into(sivThumbnail)
                tvRegion.text = abandonmentInfo.careAddr.toRough()
                tvDate.text = abandonmentInfo.happenDate
                tvPhoneNumber.text = abandonmentInfo.careTel
            }
        }
    }
}