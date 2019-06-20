package com.example.navigationcheck.adapter

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.navigationcheck.MonthFragment
import java.util.*
import kotlin.collections.ArrayList
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import android.util.SparseArray
import com.amitshekhar.utils.Utils
import android.os.Bundle
import com.example.navigationcheck.DayFragment
import com.example.navigationcheck.FragmentObserver


class DayPagerAdapter internal constructor(fm: FragmentManager, context: Context, monthList: LinkedList<String>) :
    FragmentStatePagerAdapter(fm) {

    var monthList = monthList
    var calendar: Calendar = Calendar.getInstance()

    override fun getCount(): Int {
        return monthList.size
    }


    override fun getItem(position: Int): Fragment {

        val bundle = Bundle()
        bundle.putString("monthYear", monthList.get(position))

        val fragment = DayFragment()
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

    /*
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
           val fragment = super.instantiateItem(container, position) as Fragment
           registeredFragments.put(position, fragment)
           return fragment
       }*/

}