package com.example.Calendar.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.Calendar.MonthFragment
import java.util.*
import androidx.viewpager.widget.PagerAdapter
import android.os.Bundle
import java.text.SimpleDateFormat


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