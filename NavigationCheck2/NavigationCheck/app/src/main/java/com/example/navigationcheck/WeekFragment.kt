package com.example.navigationcheck


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.GridView
import android.widget.ImageButton
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
var dateWeek: Date =Date()
var calendarWeek :Calendar=Calendar.getInstance()
/**
 * A simple [Fragment] subclass.
 *
 */

class WeekFragment : Fragment() {
    companion object {

        fun newInstance(datePassed:Date):WeekFragment{
            val args: Bundle = Bundle()
            args.putSerializable(dateWeek.toString(),datePassed)
            calendarWeek.setTime(datePassed)
            dateWeek= calendarWeek.time
            val fragment = WeekFragment()
            return fragment
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (getActivity() as MainActivity).setActionBarTitle("Week")
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_week, container, false)

        calendarWeek.setTime(dateWeek);
        var weekNumber = calendarWeek.get(Calendar.WEEK_OF_MONTH);
        calendarWeek.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        var startDate = calendarWeek.getTime();


        calendarWeek.add(Calendar.DATE, 6);
        var endDate  = calendarWeek.getTime();
        var weekId:TextView=view.findViewById(R.id.week_name)
        var start = SimpleDateFormat("dd/MM/yyyy").format(startDate)
        var end = SimpleDateFormat("dd/MM/yyyy").format(endDate)
        weekId.setText("$start - $end")
        clearForm(view.findViewById(R.id.container),date,startDate,endDate)
        var container1:GridLayout=view.findViewById(R.id.container)
        container1.setOnTouchListener(object:OnSwipeTouchListener() {
            override fun onSwipeLeft() {

                calendarWeek.setTime(startDate)
                calendarWeek.add(Calendar.WEEK_OF_MONTH, 1)
                WeekFragment.newInstance(calendarWeek.time)
                val manager = activity!!.supportFragmentManager
                val ft = manager.beginTransaction()
                val newFragment = this@WeekFragment
                this@WeekFragment.onDestroy()
                ft.remove(this@WeekFragment)
                ft.replace(container!!.getId(), newFragment)
                //container is the ViewGroup of current fragment
                ft.addToBackStack(null)
                ft.commit()

            }

            override fun onSwipeRight() {
                calendarWeek.setTime(startDate)
                calendarWeek.add(Calendar.WEEK_OF_MONTH, -1)
                WeekFragment.newInstance(calendarWeek.time)
                val manager = activity!!.supportFragmentManager
                val ft = manager.beginTransaction()
                val newFragment = this@WeekFragment
                this@WeekFragment.onDestroy()
                ft.remove(this@WeekFragment)
                ft.replace(container!!.getId(), newFragment)
                //container is the ViewGroup of current fragment
                ft.addToBackStack(null)
                ft.commit()

            }
        })
        return view
    }


}
private fun clearForm(group: ViewGroup?,date1: Date,startDate:Date,endDate:Date) {
    var i = 9

   calendarWeek.setTime(startDate)
    while (i < 16) {
        var dayOfMonth = calendarWeek.get(Calendar.DAY_OF_MONTH);
        val view = group?.getChildAt(i)
        if (view is TextView) {
            view.setGravity(Gravity.CENTER_HORIZONTAL);
                view.setText(dayOfMonth.toString())
            calendarWeek.add(Calendar.DAY_OF_MONTH,1)
        }
        if (view is ViewGroup && view.childCount > 0)
            clearForm(view,date1,startDate,endDate)
        i++
    }
}


