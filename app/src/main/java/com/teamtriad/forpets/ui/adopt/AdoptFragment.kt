package com.teamtriad.forpets.ui.adopt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewModelScope
import com.teamtriad.forpets.databinding.FragmentAdoptBinding
import com.teamtriad.forpets.ui.adopt.adapter.AdoptRecyclerViewAdapter
import com.teamtriad.forpets.viewmodel.AdoptViewModel
import kotlinx.coroutines.launch

class AdoptFragment : Fragment() {

    private val adoptViewModel: AdoptViewModel by activityViewModels()

    private var _binding: FragmentAdoptBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerViewAdapter: AdoptRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdoptBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewAdapter = AdoptRecyclerViewAdapter(adoptViewModel)
        binding.rvAdopt.adapter = recyclerViewAdapter

        with(binding.rvAdopt.adapter) {
            if (adoptViewModel.abandonmentInfoList.isNotEmpty()) {
                (this as AdoptRecyclerViewAdapter).updateDataSet(adoptViewModel.abandonmentInfoList)
            } else adoptViewModel.viewModelScope.launch {
                (this@with as AdoptRecyclerViewAdapter).requestAbandonmentInfos()
                    .join()

                if (adoptViewModel.abandonmentInfoList.isEmpty()) {
                    binding.tvFallback.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}