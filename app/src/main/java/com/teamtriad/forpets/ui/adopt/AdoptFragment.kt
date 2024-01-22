package com.teamtriad.forpets.ui.adopt

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.squareup.moshi.JsonDataException
import com.teamtriad.forpets.AdoptService
import com.teamtriad.forpets.ui.adopt.adapter.AdoptAdapter
import com.teamtriad.forpets.databinding.FragmentAdoptBinding
import com.teamtriad.forpets.model.AbandonmentInfo
import com.teamtriad.forpets.util.toYyyyMmDd
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.util.Date

class AdoptFragment : Fragment() {

    private var _binding: FragmentAdoptBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdoptBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            binding.root.adapter = AdoptAdapter(
                try { requestAbandonmentInfos(MAX_NUM_OF_ROWS) } catch (e: JsonDataException) {
                    try { requestAbandonmentInfos(199) } catch (e: JsonDataException) {
                        try { requestAbandonmentInfos(39) } catch (e: JsonDataException) {
                            try {
                                requestAbandonmentInfos(7)
                            } catch (e: JsonDataException) {
                                listOf()
                            }
                        }
                    }
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private suspend fun requestAbandonmentInfos(numOfRows: Int): List<AbandonmentInfo> {
        val adoptService: AdoptService = AdoptService.getService()

        return try {
            val response = adoptService.getAbandonmentPublic(
                "20240101",
                Date().toYyyyMmDd(),
                numOfRows.toString()
            )

            if (response.isSuccessful) response.body()
                ?.response
                ?.body
                ?.items
                ?.item ?: listOf()
            else listOf()
        } catch (e: HttpException) {
            Log.e("AdoptFragment", "retrofit2.HttpException occurred.")

            listOf()
        } catch (e: JsonDataException) {
            Log.e(
                "AdoptFragment",
                "One of the required fields may be missing from the response message."
            )

            throw JsonDataException()
        }
    }

    companion object {

        const val MAX_NUM_OF_ROWS: Int = 999
    }
}