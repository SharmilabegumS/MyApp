package com.example.navigationcheck.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.navigationcheck.MonthFragment
import java.util.*
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import android.os.Bundle
import com.example.navigationcheck.FragmentObserver



class MonthPageAdapter internal constructor(fm: FragmentManager, context: Context, monthList: LinkedList<String>) :
    FragmentStatePagerAdapter(fm) {
    var monthList = monthList
    var calendar: Calendar = Calendar.getInstance()

    override fun getCount(): Int {
        return monthList.size
    }


    override fun getItem(position: Int): Fragment {


        val bundle = Bundle()
        bundle.putString("monthYear", monthList.get(position))
        val fragment = MonthFragment()
        fragment.setArguments(bundle)
        return fragment
    }
    override fun getItemPosition(`object`: Any): Int {


        return PagerAdapter.POSITION_NONE

    }
}