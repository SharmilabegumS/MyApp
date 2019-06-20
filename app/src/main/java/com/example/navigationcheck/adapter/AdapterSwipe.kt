package com.example.navigationcheck.adapter

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.example.navigationcheck.MonthFragment
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


/*class AdapterSwipe(fm: FragmentManager,fragList:Array<MonthFragment?>) : FragmentStatePagerAdapter(fm) {
    private var fragList= arrayOfNulls<MonthFragment>(3)

    init{
      this.fragList=fragList
  }
    /*public val monthList= HashMap<Int,String>()
var calendar1:Calendar= Calendar.getInstance()
    override fun getCount(): Int {
        return monthList.size
    }

    init {
        var j=0
        for(i in -1..1){
          if(j<5) {
              calendar1.setTime(Date())
              calendar1.add(Calendar.MONTH, i)
              calendar1.set(Calendar.DAY_OF_MONTH, 1)
              var month1: Int = calendar1.get(Calendar.MONTH)
              var year1: Int = calendar1.get(Calendar.YEAR)
              var monthYear = String.format("%02d", month1) + String.format("%04d", year1)
              monthList.put(j, monthYear)
              j++
          }
        }
        println(monthList)
    }

    override fun getItem(position: Int): Fragment {
        var calendar:Calendar= Calendar.getInstance()
        var monthYear=monthList.get(position)
         var month=Integer.parseInt(monthYear!!.substring(0,2))
        var year=Integer.parseInt(monthYear.substring(2,6))
        calendar.set(Calendar.DAY_OF_MONTH,1)
        calendar.set(Calendar.MONTH,month)
        calendar.set(Calendar.YEAR,year)
        println("The fragment: $position ${calendar.time} ")

        return MonthFragment.newInstance(calendar.getTime())
    }


    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    // Delete a page at a `position`
    fun remove(position: Int,position1: Int,monthYear:String) {
        // Remove the corresponding item in the data set
        monthList.put(position1,monthYear)
        monthList.remove(position)

        // Notify the adapter that the data set is changed
        notifyDataSetChanged()
    }
    fun add(position: Int,monthYear:String) {
        // Remove the corresponding item in the data set
        monthList.put(position,monthYear)
        // Notify the adapter that the data set is changed
        notifyDataSetChanged()
    }
   /* override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as MonthFragment

       /* var calendar:Calendar= Calendar.getInstance()
        calendar.time= fragment.getDate()
        calendar.set(Calendar.DAY_OF_MONTH,1)
        var month1:Int=calendar.get(Calendar.MONTH)
        var year1:Int=calendar.get(Calendar.YEAR)
        var monthYear= String.format("%02d", month1)+ String.format("%04d", year1)
        monthList.put(position, monthYear)*/
        return fragment
    }*/
    */
     override fun getItemPosition(`object`: Any): Int {
        // TODO Auto-generated method stub
        return PagerAdapter.POSITION_NONE
    }

    override fun getItem(position: Int): MonthFragment? {

        return fragList!![position]
    }

    override fun getCount(): Int {
        return 3
    }
   fun setCalendar(currentMonth: Calendar) {

       val prevMonth = Calendar.getInstance()
       prevMonth.time = currentMonth.time
       prevMonth.add(Calendar.MONTH, -1)

       val nextMonth = Calendar.getInstance()
       nextMonth.time = currentMonth.time
       nextMonth.add(Calendar.MONTH, 1)
       //update all 3 fragments
       fragList?.get(0)!!.updateUI(prevMonth)
       fragList?.get(1)!!.updateUI(currentMonth)
       fragList?.get(2)!!.updateUI(nextMonth)
   }
}
        */
class AdapterSwipe(fm: FragmentManager, fragList: java.util.LinkedList<String?>) : FragmentStatePagerAdapter(fm) {
    public var fragList = LinkedList<String?>()

    init {
        this.fragList = fragList
        println("Month list in adapter: " + this.fragList)
    }

    /*public val monthList= HashMap<Int,String>()
var calendar1:Calendar= Calendar.getInstance()
    override fun getCount(): Int {
        return monthList.size
    }

    init {
        var j=0
        for(i in -1..1){
          if(j<5) {
              calendar1.setTime(Date())
              calendar1.add(Calendar.MONTH, i)
              calendar1.set(Calendar.DAY_OF_MONTH, 1)
              var month1: Int = calendar1.get(Calendar.MONTH)
              var year1: Int = calendar1.get(Calendar.YEAR)
              var monthYear = String.format("%02d", month1) + String.format("%04d", year1)
              monthList.put(j, monthYear)
              j++
          }
        }
        println(monthList)
    }

    override fun getItem(position: Int): Fragment {
        var calendar:Calendar= Calendar.getInstance()
        var monthYear=monthList.get(position)
         var month=Integer.parseInt(monthYear!!.substring(0,2))
        var year=Integer.parseInt(monthYear.substring(2,6))
        calendar.set(Calendar.DAY_OF_MONTH,1)
        calendar.set(Calendar.MONTH,month)
        calendar.set(Calendar.YEAR,year)
        println("The fragment: $position ${calendar.time} ")

        return MonthFragment.newInstance(calendar.getTime())
    }


    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    // Delete a page at a `position`
    fun remove(position: Int,position1: Int,monthYear:String) {
        // Remove the corresponding item in the data set
        monthList.put(position1,monthYear)
        monthList.remove(position)

        // Notify the adapter that the data set is changed
        notifyDataSetChanged()
    }
    fun add(position: Int,monthYear:String) {
        // Remove the corresponding item in the data set
        monthList.put(position,monthYear)
        // Notify the adapter that the data set is changed
        notifyDataSetChanged()
    }
   /* override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as MonthFragment

       /* var calendar:Calendar= Calendar.getInstance()
        calendar.time= fragment.getDate()
        calendar.set(Calendar.DAY_OF_MONTH,1)
        var month1:Int=calendar.get(Calendar.MONTH)
        var year1:Int=calendar.get(Calendar.YEAR)
        var monthYear= String.format("%02d", month1)+ String.format("%04d", year1)
        monthList.put(position, monthYear)*/
        return fragment
    }*/
    */
    override fun getItemPosition(`object`: Any): Int {
        // TODO Auto-generated method stub
        return PagerAdapter.POSITION_NONE
    }

    override fun getItem(position: Int): Fragment {
        val calendarObj = Calendar.getInstance()
        calendarObj.set(Calendar.MONTH, Integer.parseInt(fragList?.get(position)!!.substring(0, 2)))
        calendarObj.set(Calendar.YEAR, Integer.parseInt(fragList?.get(position)!!.substring(2, 6)))
        calendarObj.set(Calendar.DAY_OF_MONTH, 1)
        //return  MonthFragment.newInstance(calendarObj.time)
        return MonthFragment()


    }

    override fun getCount(): Int {
        return 3
    }

    fun setCalendar(currentMonth: Calendar) {


        val prevMonth = Calendar.getInstance()
        prevMonth.time = currentMonth.time
        prevMonth.add(Calendar.MONTH, -1)
        var monthYear0 =
            String.format("%02d", prevMonth.get(Calendar.MONTH)) + String.format("%04d", prevMonth.get(Calendar.YEAR))
        val nextMonth = Calendar.getInstance()
        var monthYear1 = String.format("%02d", currentMonth.get(Calendar.MONTH)) + String.format(
            "%04d",
            currentMonth.get(Calendar.YEAR)
        )

        nextMonth.time = currentMonth.time
        nextMonth.add(Calendar.MONTH, 1)
        var monthYear2 =
            String.format("%02d", nextMonth.get(Calendar.MONTH)) + String.format("%04d", nextMonth.get(Calendar.YEAR))

        //update all 3 fragments
        fragList[0] = monthYear0
        fragList[1] = monthYear1
        fragList[2] = monthYear2
        notifyDataSetChanged()


        /*  //update all 3 fragments
        val calendarObj = Calendar.getInstance()
       calendarObj.set(Calendar.MONTH,Integer.parseInt(fragList?.get(0)!!.substring(0,2)))
        calendarObj.set(Calendar.YEAR,Integer.parseInt(fragList?.get(0)!!.substring(2,6)))
        calendarObj.set(Calendar.DAY_OF_MONTH,1)
        MonthFragment.newInstance(calendarObj.time)!!.updateUI(prevMonth)

        calendarObj.set(Calendar.MONTH,Integer.parseInt(fragList?.get(1)!!.substring(0,2)))
        calendarObj.set(Calendar.YEAR,Integer.parseInt(fragList?.get(1)!!.substring(2,6)))
        calendarObj.set(Calendar.DAY_OF_MONTH,1)
        MonthFragment.newInstance(calendarObj.time)!!.updateUI(currentMonth)

        calendarObj.set(Calendar.MONTH,Integer.parseInt(fragList?.get(2)!!.substring(0,2)))
        calendarObj.set(Calendar.YEAR,Integer.parseInt(fragList?.get(2)!!.substring(2,6)))
        calendarObj.set(Calendar.DAY_OF_MONTH,1)
        MonthFragment.newInstance(calendarObj.time)!!.updateUI(nextMonth)*/
    }
}
