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
    var date:Date?=null
    var calendar: Calendar = Calendar.getInstance()

    override fun getCount(): Int {
        return monthList.size
    }


    override fun getItem(position: Int): Fragment { // Clear existing observers.


        val bundle = Bundle()
        bundle.putString("monthYear", monthList.get(position))

        var day = Integer.parseInt(monthList.get(position).substring(0, 2))
        var month = Integer.parseInt(monthList.get(position).substring(2, 4))
        var year = Integer.parseInt(monthList.get(position).substring(4, 8))
        var cal=Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH,day)

        cal.set(Calendar.MONTH,month )
        cal.set(Calendar.YEAR,year)
        date=cal.time
        val fragment = WeekFragment()
        fragment.setArguments(bundle)


        /*var monthYear=monthList.get(position)
        var month=Integer.parseInt(monthYear.substring(0,2))
        var year=Integer.parseInt(monthYear.substring(2,6))
        calendar.set(Calendar.DAY_OF_MONTH,1)
        calendar.set(Calendar.MONTH,month)
        calendar.set(Calendar.YEAR,year)
        println("The fragment: $position ${calendar.time} ")

        return MonthFragment.newInstance(calendar.getTime())*/
        // fragment.refresh()
        return fragment
    }
    /*override fun getItemPosition(`object`: Any): Int {
        val index = views.indexOf(`object`)
        return if (index == -1)
            PagerAdapter.POSITION_NONE
        else
            index
    }
*/
    /* public override fun getItemPosition(`object`: Any): Int {
         val index = monthList.indexOf(`object`)

         return if (index == -1)
             PagerAdapter.POSITION_NONE
         else
             index
     }*/

    /*override fun getItemPosition(`object`: Any): Int {
        val dummyItem = (`object` as View).tag as String
        val position = monthList.indexOf(dummyItem)
        return if (position >= 0) {
            // The current data matches the data in this active fragment, so let it be as it is.
            position
        } else {
            // Returning POSITION_NONE means the current data does not matches the data this fragment is showing right now.  Returning POSITION_NONE constant will force the fragment to redraw its view layout all over again and show new data.
            PagerAdapter.POSITION_NONE
        }
    }*/
    override fun getItemPosition(`object`: Any): Int {


        return PagerAdapter.POSITION_NONE

    }
    fun getDateWeekAdapter():Date?{
        return  date
    }

    /*
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
           val fragment = super.instantiateItem(container, position) as Fragment
           registeredFragments.put(position, fragment)
           return fragment
       }*/

}