package com.teamtriad.forpets.ui.transport.adpater

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.teamtriad.forpets.ui.transport.ViewPagerReqListFragment
import com.teamtriad.forpets.ui.transport.ViewPagerVolListFragment

class TransportListsViewPagerAdapter(val data: List<String>, fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = data.size

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> ViewPagerReqListFragment()
            else -> ViewPagerVolListFragment()
        }
    }
}