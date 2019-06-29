package com.example.Calendar


import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.IntegerRes
import com.example.Calendar.dataBase.DataBaseManager
import com.example.Calendar.domain.GetEvent
import com.example.Calendar.entity.Event
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
var dateWeek: Date = Date()
var calendarWeek: Calendar = Calendar.getInstance()

/**
 * A simple [Fragment] subclass.
 *
 */

class WeekFragment : androidx.fragment.app.Fragment() {

    /* companion object {

         fun newInstance(datePassed:Date):WeekFragment{
             val args: Bundle = Bundle()
             args.putSerializable(dateWeek.toString(),datePassed)
             calendarWeek.setTime(datePassed)
             dateWeek= calendarWeek.time
             val fragment = WeekFragment()
             return fragment
         }
     }*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (getActivity() as MainActivity).setActionBarTitle("Week")
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_week, container, false)
        var timeSlotAndEventsHashMap = HashMap<Int, ArrayList<Event>>()
        var bundle = arguments
        var value = bundle!!.getString("monthYear")
        calendar.set(Calendar.MONTH, Integer.parseInt(value.substring(2, 4)))
        calendar.set(Calendar.YEAR, Integer.parseInt(value.substring(4, 8)))
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(value.substring(0, 2)))
        context1 = context
        dateWeek = calendar.time

        calendarWeek.setTime(dateWeek);
        val activity = activity as MainActivity
        activity!!.setDateCommon(calendarWeek.time)

        var weekNumber = calendarWeek.get(Calendar.WEEK_OF_MONTH);
        calendarWeek.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        var startDate = calendarWeek.getTime();
        var bottom_date_holder: TextView = activity!!.bdh
        calendarWeek.add(Calendar.DATE, 6);
        var endDate = calendarWeek.getTime();
        var yearTv = view.findViewById<TextView>(R.id.year1)
        var monthTv = view.findViewById<TextView>(R.id.month)
        var startMonth = SimpleDateFormat("MMM").format(startDate)
        var endMonth = SimpleDateFormat("MMM").format(endDate)
        var startYear = SimpleDateFormat("yyyy").format(startDate)
        var endYear = SimpleDateFormat("yyyy").format(endDate)
        if (startMonth.equals(endMonth)) {
            monthTv.setText(startMonth)
        } else {
            monthTv.setText("$startMonth - $endMonth")
        }
        if (startYear.equals(endYear)) {
            yearTv.setText(startYear)
        } else {
            yearTv.setText("$startYear - $endYear")
        }
        var start = SimpleDateFormat("dd/MM/yyyy").format(startDate)
        var end = SimpleDateFormat("dd/MM/yyyy").format(endDate)
        var currentDate = SimpleDateFormat("dd/MM/yyyy").format(Date())
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val d1 = sdf.parse(start)
        val d2 = sdf.parse(currentDate)
        val d3 = sdf.parse(end)
        var gridLayout:GridLayout=view.findViewById(R.id.week_grid)
        bottom_date_holder.setText(currentDate.substring(0, 2))
        if (d2.compareTo(d1) >= 0) {
            if (d2.compareTo(d3) <= 0) {
                bottom_date_holder.visibility = View.INVISIBLE
            } else {
                bottom_date_holder.visibility = View.VISIBLE
            }
        }
        //clearForm(view.findViewById(R.id.container), date, startDate, endDate)
        clearForm(view.findViewById(R.id.week_grid))
        var container1: GridLayout = view.findViewById(R.id.week_grid)
        var i = 9
        val count = container1?.childCount
        var calendar111 = Calendar.getInstance()
        calendar111.time = d1

        while (i > 8 && i < 16) {
            var view = container1?.getChildAt(i)
            if (view is TextView) {
                if (format.format(calendar111.time).equals(format.format(Date()))) {
                    view.setBackgroundResource(R.drawable.botton_date_holder)
                    view.setTextColor(Color.WHITE)
                }
                var dateInt = calendar111.get(Calendar.DAY_OF_MONTH)
                view.setText(dateInt.toString())
                calendar111.add(Calendar.DAY_OF_MONTH, 1)
            }

            i++
        }
var datesList=ArrayList<Date>()
    var dateListCalendar=Calendar.getInstance()
        dateListCalendar.time=startDate
        for(i in 1..7){
            datesList.add(dateListCalendar.time)
            dateListCalendar.add(Calendar.DAY_OF_MONTH,1)
        }

        var week_layout: LinearLayout = view.findViewById(R.id.week_layout)
        var week: LinearLayout = view.findViewById(R.id.week_scroll)
        val childCount = container1.childCount
        /* for (i in 0 until childCount) {
             val element = container1.getChildAt(i) as TextView
             var paramsElement: ViewGroup.LayoutParams = element.getLayoutParams()
             paramsElement.width = (screenWidthCommon - 20) / 8
             paramsElement.height = (screenHeightCommon / 17)
             element.setLayoutParams(paramsElement);
         }*/
        /* week.setOnTouchListener(object:OnSwipeTouchListener() {
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
           })*/

        for (i in 0..0){
        //var dateIterated = datesList.get(i)
        var iteration1 = i+16
            var k=0
        var idList = ArrayList<Int>()
        var counting = gridLayout?.childCount
       // var ge = GetEvent(context!!)
       // println("Date iterated: " + dateIterated)
        //var iteratedDateEvents = ge.getEvent(dateIterated, dsc, calendar, userName)
        //println("Events of day: " + iteratedDateEvents)
        var dbm = DataBaseManager(context!!)
        while (iteration1 < counting!!) {
            var startTime:Long=0
            var endTime:Long=0
            var dateIterated = datesList.get(k)
            var view = gridLayout?.getChildAt(iteration1)
            println("Iteration: "+iteration1)
                var timeSlot = gridLayout?.getChildAt(iteration1)
                println("ID:"+timeSlot.id)
                if (timeSlot is TextView) {
                    println("Text is: "+timeSlot.text)
                    var hour = timeSlot.text.substring(0, 2)
                    var amOrPm = timeSlot.text.substring(3, 4)
                     startTime = getDateForTimeSlot(hour + ":00 " + amOrPm, dateIterated)
                    endTime = startTime + 3600000





            }
            if(timeSlot is LinearLayout){
                timeSlot.setOnClickListener{
                timeSlot.getChildAt(0).setBackgroundColor(Color.GRAY)}
               // var list = dbm.getEventsOfTimeSlots(startTime, endTime, userName)
               // timeSlotAndEventsHashMap.put(view.id, list)
                //if (list.isEmpty() == false) {
                  //  idList.add(timeSlot.getChildAt(0).id)
               // }

            }
            iteration1=iteration1+1
            k++
            if(k==7){
                k=0
            }

        }

    }

        println("Hash map: "+timeSlotAndEventsHashMap)

        bottom_date_holder.setOnClickListener {
            val activity = activity as MainActivity
            activity!!.setDateCommon(Date())
            activity!!.getCurrentWeekView()
        }
        return view
    }


}

/*private fun clearForm(group: ViewGroup?, date1: Date, startDate: Date, endDate: Date) {
    var i = 9

    calendarWeek.setTime(startDate)
    while (i < 16) {
        var dayOfMonth = calendarWeek.get(Calendar.DAY_OF_MONTH);
        val view = group?.getChildAt(i)
        if (view is TextView) {
            view.setGravity(Gravity.CENTER_HORIZONTAL);
            view.setText(dayOfMonth.toString())
            calendarWeek.add(Calendar.DAY_OF_MONTH, 1)
        }
        if (view is ViewGroup && view.childCount > 0)
            clearForm(view, date1, startDate, endDate)
        i++
    }
}*/
private fun clearForm(group: ViewGroup?) {
    var i = 0
    val count = group?.childCount
    while (i < count!!) {
        var view = group?.getChildAt(i)



        setDimensions(view, (screenWidthCommon / 8), (screenWidthCommon / 8).toInt())

        i++

    }
}
private fun getDateForTimeSlot(s: String, date: Date): Long {
    var cal: Calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
    cal.setTime(date)
    if (s.substring(5, 7).toLowerCase().equals("am")) {
        cal.set(Calendar.AM_PM, Calendar.AM)
        if (s.substring(0, 2).equals("12")) {
            cal.set(Calendar.HOUR_OF_DAY, 0)
        } else {
            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(s.substring(0, 2)))
        }

    } else if (s.substring(5, 7).toLowerCase().equals("pm")) {
        cal.set(Calendar.AM_PM, Calendar.PM)
        if (s.substring(0, 2).equals("12")) {
            cal.set(Calendar.HOUR_OF_DAY, 12)
        } else {
            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(s.substring(0, 2)) + 12)
        }

    }
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)

    var date1 = dsc.getDateInMillis1(cal.time)
    return date1

}
private fun setDimensions(view: View, width: Int, height: Int) {
    val params = view.layoutParams
    params.width = width
    params.height = height
    view.layoutParams = params
}


