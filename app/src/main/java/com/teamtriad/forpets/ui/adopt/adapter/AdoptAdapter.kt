package com.teamtriad.forpets.ui.adopt.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.teamtriad.forpets.data.source.network.AbandonmentInfo
import com.teamtriad.forpets.databinding.RvItemAdoptBinding
import com.teamtriad.forpets.ui.adopt.AdoptFragmentDirections
import com.teamtriad.forpets.util.glide

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

        fun bind(data: AbandonmentInfo) {
            setOnClickListener(data.imageUrl)   // TODO: ViewModel을 사용하여 데이터를 전달하기.
            bindData(data)
        }

        private fun setOnClickListener(imageUrl: String) {
            val action = AdoptFragmentDirections.actionAdoptFragmentToAdoptDetailFragment(imageUrl)

            itemView.setOnClickListener {
                it.findNavController()
                    .navigate(action)
            }
        }

        private fun bindData(data: AbandonmentInfo) {
            fun String.toRough(): String {
                return split(" ").filter { it != "" }
                    .let {
                        it[0] + " " + it[1]
                    }
            }

            with(binding) {
                sivThumbnail.glide(data.imageUrl)
                tvRegion.text = data.careAddr.toRough()
                tvSex.text = data.sex
                tvAge.text = data.age
            }
        }
    }
}