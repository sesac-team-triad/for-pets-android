package com.teamtriad.forpets.ui.adopt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.teamtriad.forpets.databinding.FragmentAdoptBinding
import com.teamtriad.forpets.ui.adopt.adapter.AdoptRecyclerViewAdapter

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

        binding.root.adapter = AdoptRecyclerViewAdapter(lifecycleScope).apply {
            requestAbandonmentInfos()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}