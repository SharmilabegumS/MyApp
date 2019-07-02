package com.example.Calendar.adapter


import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import java.util.*
import androidx.viewpager.widget.PagerAdapter
import android.os.Bundle
import com.example.Calendar.WeekFragment


class WeekPagerAdapter internal constructor(fm: FragmentManager, context: Context, monthList: LinkedList<String>) :
    FragmentStatePagerAdapter(fm) {
    var monthList = monthList
    var fm = fm

    var calendar: Calendar = Calendar.getInstance()

    override fun getCount(): Int {
        return monthList.size
    }

    override fun getItem(position: Int): Fragment { // Clear existing observers.
        val bundle = Bundle()
        bundle.putString("monthYear", monthList.get(position))
        val fragment = WeekFragment()
        fragment.setArguments(bundle)
        return fragment
    }

    override fun getItemPosition(`object`: Any): Int {


        return PagerAdapter.POSITION_NONE

    }



}