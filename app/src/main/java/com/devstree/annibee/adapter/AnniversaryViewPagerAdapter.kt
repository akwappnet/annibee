package com.devstree.annibee.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.util.*

class AnniversaryViewPagerAdapter(fm: FragmentActivity, val fragments: MutableList<Fragment>) : FragmentStateAdapter(fm) {

//    private val fragments: MutableList<Fragment> = ArrayList()

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

    /*override fun getItemId(position: Int): Long {
        return fragments[position].id.toLong()
    }

    override fun containsItem(itemId: Long): Boolean {
        return fragments.any { it.id.toLong() == itemId }
    }*/

}

/*
class PagerDiffUtil(private val oldList: AnniversaryEvent, private val newList: AnniversaryEvent) : DiffUtil.Callback() {

    enum class PayloadKey {
        VALUE
    }

    override fun getOldListSize(): Int = oldList.anniversaryList?.size!!

    override fun getNewListSize() = newList.anniversaryList?.size!!

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList.anniversaryList?.get(oldItemPosition)?.id == newList.anniversaryList?.get(newItemPosition)?.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList.anniversaryList?.get(oldItemPosition) == newList.anniversaryList?.get(newItemPosition)
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return listOf(PayloadKey.VALUE)
    }
}*/
