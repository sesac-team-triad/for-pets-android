package com.teamtriad.forpets.ui.transport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.teamtriad.forpets.R
import com.teamtriad.forpets.databinding.FragmentTransportListsBinding
import com.teamtriad.forpets.ui.transport.adapter.TransportListsViewPagerAdapter

class TransportListsFragment : Fragment() {

    private var _binding: FragmentTransportListsBinding? = null
    private val binding get() = _binding!!

    private lateinit var vp2Adapter: TransportListsViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransportListsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabTitleList = listOf(
            getString(R.string.all_req_list),
            getString(R.string.all_vol_list),
        )

        with(binding) {
            vp2Adapter = TransportListsViewPagerAdapter(tabTitleList, this@TransportListsFragment)

            vp2TransportLists.adapter = vp2Adapter

            TabLayoutMediator(tlTransportLists, vp2TransportLists) { tab, position ->
                tab.text = tabTitleList[position]
            }.attach()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}