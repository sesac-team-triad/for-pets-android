package com.teamtriad.forpets.ui.transport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.teamtriad.forpets.databinding.FragmentTransportMovingListBinding
import com.teamtriad.forpets.ui.transport.adapter.MovingListRecyclerViewAdapter
import com.teamtriad.forpets.viewmodel.ProfileViewModel
import com.teamtriad.forpets.viewmodel.TransportViewModel
import kotlinx.coroutines.launch

class TransportMovingListFragment : Fragment() {

    private val transportViewModel: TransportViewModel by activityViewModels()
    private val profileViewModel: ProfileViewModel by viewModels()

    private var _binding: FragmentTransportMovingListBinding? = null
    private val binding get() = _binding!!

    private val recyclerViewAdapter by lazy {
        MovingListRecyclerViewAdapter(lifecycleScope, transportViewModel, profileViewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransportMovingListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            transportViewModel.getAllMovingMap()
                .join()

            binding.setRecyclerView()
        }
    }

    private fun FragmentTransportMovingListBinding.setRecyclerView() {
        root.adapter = recyclerViewAdapter.apply {
            transportViewModel.movingMap.observe(viewLifecycleOwner) {
                submitList(
                    it.values.toList()
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}