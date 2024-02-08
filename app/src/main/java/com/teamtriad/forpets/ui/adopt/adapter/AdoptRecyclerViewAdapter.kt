package com.teamtriad.forpets.ui.adopt.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.squareup.moshi.JsonDataException
import com.teamtriad.forpets.ForPetsApplication.Companion.adoptService
import com.teamtriad.forpets.data.source.network.model.AbandonmentInfo
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

    private var pageNo: Int = 0         // 마지막으로 요청한 페이지 번호(pageNo) 매개변수의 값을 관리
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

        if (position == dataSet.size - 9) lifecycleScope.launch {
            requestAbandonmentInfos()
        }
    }

    override fun getItemCount() = dataSet.size

    suspend fun requestAbandonmentInfos(): Int {
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
                ).run {
                    if (this >= 1) {
                        notifyItemRangeInserted(dataSet.size - this, this)
                    }
                }
            }
        } catch (e: HttpException) {
            Log.e("AdoptRVAdapter", "retrofit2.HttpException occurred.")

            pageNo--
        } catch (e: SocketTimeoutException) {
            Log.e("AdoptRVAdapter", "java.net.SocketTimeoutException occurred.")

            pageNo--
        } catch (e: JsonDataException) {
            Log.e(
                "AdoptRVAdapter",
                "One of the required fields may be missing from the response message."
            )
        }

        return dataSet.size
    }

    private fun updateDataSet(item: List<AbandonmentInfo>?): Int {
        if (item.isNullOrEmpty()) return 0

        val abandonmentInfos = item.toMutableList()

        abandonmentInfos.adjustItemInversion()
        abandonmentInfos.adjustItemDuplicate()

        dataSet.addAll(abandonmentInfos)
        noticeNoSet.addAll(
            abandonmentInfos.map {
                it.noticeNo
            }
        )

        return abandonmentInfos.size
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