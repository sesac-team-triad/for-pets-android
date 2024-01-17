package com.teamtriad.forpets.ui.transport.adpater

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.teamtriad.forpets.ui.transport.ViewPagerReqListFragment
import com.teamtriad.forpets.ui.transport.ViewPagerVolListFragment

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> ViewPagerReqListFragment()
            else -> ViewPagerVolListFragment()
        }
    }
}