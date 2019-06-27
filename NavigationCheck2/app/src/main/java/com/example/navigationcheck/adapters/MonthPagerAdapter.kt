package com.example.navigationcheck.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.navigationcheck.MonthFragment
import com.example.navigationcheck.R
import java.util.*
var calendarObj = Calendar.getInstance()
class MonthPagerAdapter(private val myContext: Context?, fragmentManager: FragmentManager?, var totalTabs: Int) :
    FragmentPagerAdapter(fragmentManager) {


    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> {
                calendarObj.setTime(Date())
                calendarObj.add(Calendar.MONTH, -1)
                var date11111 = calendarObj.getTime()
                var prevMonth = MonthFragment.newInstance(date11111)
                return prevMonth
            }

            1 -> {
                calendarObj.setTime(Date())
                var date111 = calendarObj.getTime()
                var currentMonth = MonthFragment.newInstance(date111)
                return currentMonth
            }
            2 -> {
                calendarObj.setTime(Date())
                calendarObj.add(Calendar.MONTH, 1)
                var date1111 = calendarObj.getTime()
                var nextMonth = MonthFragment.newInstance(date1111)
                return nextMonth
            }
        }
        return null
    }

    override fun getCount(): Int {
        return 3
    }

}