package com.example.book_v2.data.adapters

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPager2Adapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private var pagesFragmentList = ArrayList<Fragment>()


    override fun getItemCount(): Int {
        return pagesFragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return pagesFragmentList[position]
    }


    fun addFragment(newFragment: Fragment) {
        Log.e("TAG", "addFragment: fragment added${newFragment::class.java}  ")
        pagesFragmentList.add(newFragment)
        notifyItemInserted(pagesFragmentList.size - 1)
    }


    fun addFragment1(fragment: Fragment) {
        pagesFragmentList.add(fragment)
    }

    fun removeFragment(fragment: Fragment) {
        pagesFragmentList.remove(fragment)
        notifyDataSetChanged()
    }

    fun getFragment(position: Int): Fragment = pagesFragmentList[position]

    override fun getItemId(position: Int): Long {
        // Return a unique ID for the fragment at the given position
        return super.getItemId(position).hashCode().toLong()
    }

    override fun containsItem(itemId: Long): Boolean {
        for (i in 0 until itemCount) {
            if (getItemId(i) == itemId) {
                Log.e("TAG", "containsItem: true")
                return true
            }
        }
        Log.e("TAG", "containsItem: false")

        return false
    }


}