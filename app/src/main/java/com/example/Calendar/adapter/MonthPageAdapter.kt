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
    var date:Date?=null
    val format1 = SimpleDateFormat("MM/yyyy")

    override fun getCount(): Int {
        return monthList.size
    }


    override fun getItem(position: Int): Fragment {


        val bundle = Bundle()
        bundle.putString("monthYear", monthList.get(position))
        val sdf = SimpleDateFormat("MM/yyyy")
        var cal=Calendar.getInstance()
        cal.set(Calendar.MONTH,Integer.parseInt(monthList.get(position).substring(0,2)) )
        cal.set(Calendar.YEAR,Integer.parseInt(monthList.get(position).substring(2,6)))
        cal.set(Calendar.DAY_OF_MONTH,1)
        date=cal.time
       /* if(format1.format(Date()).equals("${monthList.get(position).substring(0,2)}/${monthList.get(position).substring(2,6)}")){
            MainActivity().bdh.isClickable=true
            MainActivity().bdh.performClick()
        }*/

        val fragment = MonthFragment()
        fragment.setArguments(bundle)
        return fragment
    }
    override fun getItemPosition(`object`: Any): Int {


        return PagerAdapter.POSITION_NONE

    }
    fun getDateMonthAdapter():Date?{
    return  date
    }
}