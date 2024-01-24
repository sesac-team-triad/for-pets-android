package com.teamtriad.forpets.ui.adopt.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.squareup.moshi.JsonDataException
import com.teamtriad.forpets.data.source.network.AbandonmentInfo
import com.teamtriad.forpets.data.source.network.AdoptService
import com.teamtriad.forpets.databinding.RvItemAdoptBinding
import com.teamtriad.forpets.ui.adopt.AdoptFragmentDirections
import com.teamtriad.forpets.util.glide
import com.teamtriad.forpets.util.toYyyyMmDd
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.util.Date

class AdoptRecyclerViewAdapter(private val lifecycleScope: LifecycleCoroutineScope) :
    RecyclerView.Adapter<AdoptRecyclerViewAdapter.ViewHolder>() {

    private var pageNo: Int = 0
    private val dataSet = mutableListOf<AbandonmentInfo>()

    private val adoptService: AdoptService by lazy { AdoptService.getService() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        RvItemAdoptBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataSet[position])

        if (position == dataSet.size - 1) requestAbandonmentInfos()
    }

    override fun getItemCount() = dataSet.size

    fun requestAbandonmentInfos() {
        lifecycleScope.launch {
            try {
                val response = adoptService.getAbandonmentPublic(
                    "20240101",
                    Date().toYyyyMmDd(),
                    (++pageNo).toString()
                )

                if (response.isSuccessful) {
                    updateDataSet(
                        response.body()!!
                            .response
                            .body
                            ?.items
                            ?.item
                    ).let {
                        notifyItemRangeInserted(dataSet.size - it, it)
                    }
                }
            } catch (e: HttpException) {
                Log.e("AdoptRVAdapter", "retrofit2.HttpException occurred.")
            } catch (e: SocketTimeoutException) {
                Log.e("AdoptRVAdapter", "java.net.SocketTimeoutException occurred.")
            } catch (e: JsonDataException) {
                Log.e(
                    "AdoptRVAdapter",
                    "One of the required fields may be missing from the response message."
                )

                pageNo++
            }
        }
    }

    private fun updateDataSet(item: List<AbandonmentInfo>?): Int {
        if (item.isNullOrEmpty()) return 0

        val abandonmentInfos = item.toMutableList()

        do {
            if (dataSet.find {
                    it.noticeNo == abandonmentInfos[0].noticeNo
                } == null) break

            abandonmentInfos.removeFirst()
        } while (abandonmentInfos.isNotEmpty())

        return abandonmentInfos.run {
            dataSet.addAll(this)

            size
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