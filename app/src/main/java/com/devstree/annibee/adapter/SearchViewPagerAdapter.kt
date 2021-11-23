package com.devstree.annibee.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.devstree.annibee.fragment.*

class SearchViewPagerAdapter(fragment: FragmentActivity, private var totalTabs: Int) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return totalTabs
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AllAnniversaryFragment()
            1 -> UpcomingAnniversaryFragment()
            2 -> PastAnniversaryFragment()
            else -> PastAnniversaryFragment()
        }
    }
}