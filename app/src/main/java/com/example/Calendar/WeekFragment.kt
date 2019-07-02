package com.example.Calendar


import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.annotation.IntegerRes
import com.example.Calendar.dataBase.DataBaseManager
import com.example.Calendar.domain.GetEvent
import com.example.Calendar.entity.Event
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WeekFragment : androidx.fragment.app.Fragment() {
    var dateWeek: Date = Date()
    var calendarWeek: Calendar = Calendar.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (getActivity() as MainActivity).setActionBarTitle("Week")
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_week, container, false)
        val timeSlotAndEventsHashMap = HashMap<Int, ArrayList<Event>>()
        val bundle = arguments
        val value = bundle!!.getString("monthYear")
        calendar.set(Calendar.MONTH, Integer.parseInt(value!!.substring(2, 4)))
        calendar.set(Calendar.YEAR, Integer.parseInt(value.substring(4, 8)))
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(value.substring(0, 2)))
        dateWeek = calendar.time

        calendarWeek.setTime(dateWeek);
        val activity = activity as MainActivity
        activity.setDateCommon(calendarWeek.time)
        calendarWeek.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        val startDate = calendarWeek.getTime();
        val bottom_date_holder: TextView = activity.bottomDateHolder


        calendarWeek.add(Calendar.DATE, 6);
        val endDate = calendarWeek.getTime();
        val yearTv = view.findViewById<TextView>(R.id.year1)
        val monthTv = view.findViewById<TextView>(R.id.month)
        val startMonth = SimpleDateFormat("MMM").format(startDate)
        val endMonth = SimpleDateFormat("MMM").format(endDate)
        val startYear = SimpleDateFormat("yyyy").format(startDate)
        val endYear = SimpleDateFormat("yyyy").format(endDate)
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
        val start = SimpleDateFormat("dd/MM/yyyy").format(startDate)
        val end = SimpleDateFormat("dd/MM/yyyy").format(endDate)
        val currentDate = SimpleDateFormat("dd/MM/yyyy").format(Date())
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val d1 = sdf.parse(start)
        val d2 = sdf.parse(currentDate)
        val d3 = sdf.parse(end)
        val gridLayout: GridLayout = view.findViewById(R.id.week_grid)
        bottom_date_holder.setText(Integer.parseInt(currentDate.substring(0, 2)).toString())
        if (d2.compareTo(d1) >= 0) {
            if (d2.compareTo(d3) <= 0) {
                bottom_date_holder.visibility = View.INVISIBLE
            } else {
                bottom_date_holder.visibility = View.VISIBLE
            }
        }
        clearForm(view.findViewById(R.id.week_grid))
        val container1: GridLayout = view.findViewById(R.id.week_grid)
        var i = 9
        val calendar111 = Calendar.getInstance()
        calendar111.time = d1

        while (i > 8 && i < 16) {
            val view = container1?.getChildAt(i)
            if (view is TextView) {
                if (format.format(calendar111.time).equals(format.format(Date()))) {
                    view.setBackgroundResource(R.drawable.botton_date_holder)
                    view.setTextColor(Color.WHITE)
                }
                val dateInt = calendar111.get(Calendar.DAY_OF_MONTH)
                view.setText(dateInt.toString())
                calendar111.add(Calendar.DAY_OF_MONTH, 1)
            }

            i++
        }
        val datesList = ArrayList<Date>()
        val dateListCalendar = Calendar.getInstance()
        dateListCalendar.time = startDate
        for (i in 1..7) {
            datesList.add(dateListCalendar.time)
            dateListCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        println("Hash map: " + timeSlotAndEventsHashMap)

        bottom_date_holder.setOnClickListener {
            val activity = activity as MainActivity
            activity.setDateCommon(Date())
            activity.getWeekList()
            activity.getCurrentWeekView()
        }
        return view
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.jump_to_date).setVisible(false)
        menu.findItem(R.id.calculate_date).setVisible(false)
        super.onCreateOptionsMenu(menu, inflater)
    }

}

private fun clearForm(group: ViewGroup?) {
    var i = 0
    val count = group?.childCount
    while (i < count!!) {
        val view = group.getChildAt(i)
        setDimensions(view, (screenWidthCommon / 8), (screenWidthCommon / 8).toInt())
        i++

    }
}

private fun setDimensions(view: View, width: Int, height: Int) {
    val params = view.layoutParams
    params.width = width
    params.height = height
    view.layoutParams = params
}


