package com.teamtriad.forpets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.teamtriad.forpets.adapter.AdoptAdapter
import com.teamtriad.forpets.databinding.FragmentAdoptBinding
import com.teamtriad.forpets.model.AbandonmentInfo
import com.teamtriad.forpets.util.toYyyyMmDd
import kotlinx.coroutines.launch
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
            binding.root.adapter = AdoptAdapter(requestAbandonmentInfos())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private suspend fun requestAbandonmentInfos(): List<AbandonmentInfo> {
        val adoptService: AdoptService = AdoptService.getService()

        val response = adoptService.getAbandonmentPublic(
            "20240101",
            Date().toYyyyMmDd(),
            MAX_NUM_OF_ROWS.toString()
        )

        return response.body()
            ?.response
            ?.body
            ?.items
            ?.item ?: listOf()
    }

    companion object {

        const val MAX_NUM_OF_ROWS: Int = 1000
    }
}