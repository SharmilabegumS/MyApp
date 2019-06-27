package com.example.navigationcheck


import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.util.*
import kotlinx.android.synthetic.main.content_month.*
import java.lang.Exception
import android.R.id
import android.text.method.TextKeyListener.clear
import android.util.Log
import android.widget.*
import kotlinx.android.synthetic.main.fragment_month.*
import java.text.SimpleDateFormat
import java.time.Month
import android.widget.FrameLayout.LayoutParams


var date: Date =Date()
var calendar :Calendar=Calendar.getInstance()
var  month:Int =calendar.get(Calendar.MONTH)
var  year:Int =calendar.get(Calendar.YEAR)


/**
 * A simple [Fragment] subclass.
 *
 */
class MonthFragment : Fragment() {
    companion object {

        fun newInstance(datePassed:Date):MonthFragment{
            val args: Bundle = Bundle()
            args.putSerializable(date.toString(),datePassed)
            calendar.setTime(datePassed)
            date= calendar.time
            month=calendar.get(Calendar.MONTH)
            year=calendar.get(Calendar.YEAR)
            val fragment = MonthFragment()
            return fragment
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var startDate = getFirstDateOfMonth(date,calendar)
        var endDate = getLastDateOfMonth(date,calendar)
        var lastDayofMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        var firstDayofMonth = calendar.getMinimalDaysInFirstWeek()
        var noOfDays = lastDayofMonth - firstDayofMonth + 1
        var monthName = SimpleDateFormat("MMMM").format(startDate)
        calendar.setTime(startDate)
        var spaces:Int= calendar.get(Calendar.DAY_OF_WEEK)-1
        (getActivity() as MainActivity).setActionBarTitle("Month")
        val view=inflater.inflate(R.layout.fragment_month, container, false)
        var container1=view.findViewById<GridLayout>(R.id.container)
        optimizeSize(view.findViewById(R.id.container))
        // Inflate the layout for this fragment
       var month:TextView=view.findViewById(R.id.month_name)
        var layoutparams: LayoutParams = month.getLayoutParams() as (FrameLayout.LayoutParams)
        layoutparams.height = (screenHeightCommon/11.38).toInt()
       month.setLayoutParams(layoutparams);
        month.setText("$monthName  $year")
        clearForm(view.findViewById(R.id.container),spaces,noOfDays,lastDayofMonth)
        container1.setOnTouchListener(object:OnSwipeTouchListener(){
           override fun onSwipeLeft() {
               println("Hello Left")
               calendar.add(Calendar.MONTH, 1)
               println(calendar.time)
               newInstance(calendar.time)
               val manager = activity!!.supportFragmentManager
               val ft = manager.beginTransaction()
               val newFragment = this@MonthFragment
               this@MonthFragment.onDestroy()
               ft.remove(this@MonthFragment)
               ft.replace(container!!.getId(), newFragment)
               //container is the ViewGroup of current fragment
               ft.addToBackStack(null)
               ft.commit()

           }
               override fun onSwipeRight() {
                   println("Hello right")
                   calendar.add(Calendar.MONTH, -1)
                   println(calendar.time)
                   newInstance(calendar.time)
                   val manager = activity!!.supportFragmentManager
                   val ft = manager.beginTransaction()
                   val newFragment = this@MonthFragment
                   this@MonthFragment.onDestroy()
                   ft.remove(this@MonthFragment)
                   ft.replace(container!!.getId(), newFragment)
                   //container is the ViewGroup of current fragment
                   ft.addToBackStack(null)
                   ft.commit()

               }

            override fun onSwipeBottom() {
                super.onSwipeBottom()
            }

            override fun onSwipeTop() {
                super.onSwipeTop()
            }

        })
        return view
    }
    private fun getFirstDateOfMonth(date: Date, cal: Calendar): Date {
        cal.time = date
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH))
        return cal.time
    }


    private fun getLastDateOfMonth(date: Date, cal: Calendar): Date {
        cal.time = date
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
        return cal.time
    }

    private fun clearForm(group: ViewGroup?,spaces:Int,noOfDays:Int,lastDayofMonth:Int) {
        var i = 1

        var date=1
        val count = group?.childCount
        while (i < count!!) {
            var view = group?.getChildAt(i)

            if (view is TextView) {
                if(i<7+spaces || date>noOfDays){

                }
                else {
                    view.setText(date.toString())
                    date++
                }
            }

            if (view is ViewGroup && view.childCount > 0)
                clearForm(view,spaces,noOfDays,lastDayofMonth)
            i++
        }
        var j=lastDayofMonth-date+1
        var k=1
        while(date-1<noOfDays&&k<=j){
            val view = group?.getChildAt(k+6)
            k++
            if (view is TextView) {
                    view.setText(date.toString())
                    date++

            }
            if (view is ViewGroup && view.childCount > 0)
                clearForm(view,spaces,noOfDays,lastDayofMonth)

        }


    }
    fun optimizeSize(group: ViewGroup?){
        var item=0
        println("Hello")
        val count = group?.childCount
        while(item<count!!){
            println("HI")
            var view = group?.getChildAt(item)
            setDimensions(view,(screenWidthCommon-10)/7,(screenHeightCommon/10.34).toInt())
            item++
        }
    }

    private fun setDimensions(view: View, width: Int, height: Int) {
        val params = view.layoutParams
        params.width = width
        params.height = height
        view.layoutParams = params
    }



}

