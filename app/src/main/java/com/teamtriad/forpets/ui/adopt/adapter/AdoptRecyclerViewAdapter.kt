package com.teamtriad.forpets.ui.adopt.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.teamtriad.forpets.data.source.network.model.AbandonmentInfo
import com.teamtriad.forpets.databinding.RvItemAdoptBinding
import com.teamtriad.forpets.ui.adopt.AdoptFragmentDirections
import com.teamtriad.forpets.util.glide
import com.teamtriad.forpets.viewmodel.AdoptViewModel
import kotlinx.coroutines.launch

class AdoptRecyclerViewAdapter(private val viewModel: AdoptViewModel) :
    RecyclerView.Adapter<AdoptRecyclerViewAdapter.ViewHolder>() {

    private val dataSet = mutableListOf<AbandonmentInfo>()
    private val noticeNoSet = mutableSetOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        RvItemAdoptBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataSet[position])

        if (position == dataSet.size - 9) requestAbandonmentInfos()
    }

    override fun getItemCount() = dataSet.size

    fun requestAbandonmentInfos() = viewModel.viewModelScope.launch {
        val prevItemCount: Int = itemCount

        if (updateDataSet(viewModel.getAbandonmentInfos())) {
            viewModel.setAbandonmentInfoList(dataSet)

            notifyItemRangeInserted(prevItemCount, itemCount - prevItemCount)
        }
    }

    fun updateDataSet(item: List<AbandonmentInfo>?): Boolean {
        if (item.isNullOrEmpty()) return false

        val abandonmentInfos = item.toMutableList()

        abandonmentInfos.adjustItemInversion()
        abandonmentInfos.adjustItemDuplicate()

        dataSet.addAll(abandonmentInfos)
        noticeNoSet.addAll(
            abandonmentInfos.map {
                it.noticeNo
            }
        )

        return 1 <= abandonmentInfos.size
    }

    private fun MutableList<AbandonmentInfo>.adjustItemInversion() {
        if (dataSet.isEmpty()) return

        while (isNotEmpty()) {
            if (dataSet[dataSet.lastIndex].happenDate >= this[0].happenDate) break

            removeFirst()
        }
    }

    private fun MutableList<AbandonmentInfo>.adjustItemDuplicate() {
        removeAll {
            noticeNoSet.contains(it.noticeNo)
        }
    }

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
            fun String.toRough() = split(" ").filter { it != "" }
                .let {
                    it[0] + " " + it[1]
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