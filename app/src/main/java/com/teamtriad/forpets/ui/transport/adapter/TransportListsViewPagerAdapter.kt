package com.teamtriad.forpets.ui.transport.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.teamtriad.forpets.ui.transport.ViewPagerReqListFragment
import com.teamtriad.forpets.ui.transport.ViewPagerVolListFragment

class TransportListsViewPagerAdapter(
    private val dataSet: List<String>,
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = dataSet.size

    override fun createFragment(position: Int) = when(position) {
        0 -> ViewPagerReqListFragment()
        else -> ViewPagerVolListFragment()
    }
}