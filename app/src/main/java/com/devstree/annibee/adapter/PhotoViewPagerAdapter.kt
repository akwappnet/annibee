package com.devstree.annibee.adapter

import androidx.fragment.app.*
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.util.*

class PhotoViewPagerAdapter(fm: FragmentActivity) : FragmentStateAdapter(fm) {

    private val fragments: MutableList<Fragment> = ArrayList()

    fun add(fragment: Fragment) {
        fragments.add(fragment)
        //        fragmentTitle.add(title);
    }

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}