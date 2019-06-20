package com.example.navigationcheck


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.util.*
import android.widget.*
import java.text.SimpleDateFormat
import android.widget.FrameLayout.LayoutParams
import android.widget.TextView
import com.example.navigationcheck.domain.GetEvent
import android.widget.ArrayAdapter
import com.example.navigationcheck.entity.Event
import android.widget.AdapterView
import kotlin.collections.ArrayList




var date: Date = Date()
var calendar: Calendar = Calendar.getInstance()
var month: Int = calendar.get(Calendar.MONTH)
var year: Int = calendar.get(Calendar.YEAR)
var dateInteger = calendar.get(Calendar.DAY_OF_MONTH)
lateinit var gridLayout: GridLayout
lateinit var currentDateElement: TextView


var eventlist = ArrayList<Event>()
var itemsAdapter: ArrayAdapter<String>? = null
var containerToBeProcessed: ViewGroup? = null
var context1: Context? = null
var i=1
lateinit var view1:View
var datePassed: Date? = Date()
var eventStartTime=ArrayList<String>()
var eventEndTime=ArrayList<String>()
lateinit var monthStartDate:TextView
var status:Boolean=true
class MonthFragment : androidx.fragment.app.Fragment() {

    var cal1: Calendar = Calendar.getInstance()
    val format = SimpleDateFormat("dd/MM/yyyy")
    val format1 = SimpleDateFormat("MM/yyyy")
    var location = IntArray(2)
    var location1 = IntArray(2)
    var iteration = 1

    companion object {
        @JvmStatic
        fun newInstance(date: Date): MonthFragment? {
            datePassed = date
            return MonthFragment()
        }
    }

    @SuppressLint("NewApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_month, container, false)
        view1=view
        status=true
        var bundle = arguments
        var value = bundle!!.getString("monthYear")
        calendar.set(Calendar.MONTH, Integer.parseInt(value.substring(0, 2)))
        calendar.set(Calendar.YEAR, Integer.parseInt(value.substring(2, 6)))
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        datePassed = calendar.time
        date = calendar.time
        val activity = activity as MainActivity
        //activity!!.date=date
        //dateCommon=date
        month = calendar.get(Calendar.MONTH)
        year = calendar.get(Calendar.YEAR)
        containerToBeProcessed = container
        var startDate = getFirstDateOfMonth(date, calendar)
        var endDate = getLastDateOfMonth(date, calendar)
        var lastDayofMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        var firstDayofMonth = calendar.getMinimalDaysInFirstWeek()
        var noOfDays = lastDayofMonth - firstDayofMonth + 1
        var monthName = SimpleDateFormat("MMM").format(startDate)

        calendar.setTime(startDate)
        var spaces: Int = calendar.get(Calendar.DAY_OF_WEEK) - 1
        (getActivity() as MainActivity).setActionBarTitle("Month")

        optimizeSize(view.findViewById(R.id.gridlayout))

        lateinit var events_title: TextView
        var eventListView: ListView = view.findViewById(R.id.event_list)
        var bottom_date_holder: TextView = view.findViewById(R.id.bottom_date_holder)
        bottom_date_holder.text = dateInteger.toString()
        bottom_date_holder.setBackgroundResource(R.drawable.circle_shape)
        setDimensions(
            bottom_date_holder,
            ((screenWidthCommon - 30) / 7.5).toInt(),
            (screenWidthCommon - 30) / 7.5.toInt()-10
        )
        //var month_layout: GridLayout = view.findViewById(R.id.gridlayout)
        var month: TextView = view.findViewById(R.id.month_name)
var year1:TextView=view.findViewById(R.id.year1)
        month.setText("$monthName")
        year1.setText("$year")
        clearForm(view.findViewById(R.id.gridlayout), spaces, noOfDays, lastDayofMonth)
        val container1 = view.findViewById(R.id.gridlayout) as GridLayout
        events_title = view.findViewById(R.id.events_title)

        var cal: Calendar = Calendar.getInstance()
        cal.setTime(Date())
        cal1.setTime(startDate)
        gridLayout = container1
        val childCount = container1.childCount
        for (i in 7 until childCount) {
            val element = container1.getChildAt(i) as TextView
            var date: Int
            var dateIterated = Date()
            var eventList = ArrayList<Event>()
            if (element.text.toString().equals("") == false) {
                date = Integer.valueOf(element.text.toString())
                cal1.set(Calendar.DAY_OF_MONTH, date)
                dateIterated = cal1.time
                var ge = GetEvent(context!!)
                eventList = ge.getEvent(dateIterated, dsc, calendar, "sharmilabegum97@gmail.com")

                if(element.text.equals("1")) {
                  monthStartDate=element
                }
                if (format.format(dateIterated).equals(format.format(Date()))) {

                    element.setBackgroundResource(R.drawable.circle_shape)
                    element.setTextColor(Color.WHITE)
                    currentDateElement = element
                    bottom_date_holder.visibility = View.INVISIBLE
                    location[0] = currentDateElement.getX().toInt()
                    location[1] = currentDateElement.getY().toInt()
                   var calendarObj: Calendar = Calendar.getInstance()
                    var calobj:Calendar= Calendar.getInstance()
                    calobj.setTime(startDate)

                    activity!!.setDateCommon(Date())
                    calendarObj.set(Calendar.DAY_OF_MONTH,calobj.get(Calendar.DAY_OF_MONTH))
                    calendarObj.set(Calendar.MONTH,calobj.get(Calendar.MONTH))
                    calendarObj.set(Calendar.YEAR,calobj.get(Calendar.YEAR))
                    calendarObj.set(Calendar.DAY_OF_MONTH, Integer.valueOf(element.text.toString()))
                    var dateIterated = calendarObj.time
                    calendarObj.add(Calendar.HOUR, 1)
                    var timeEnd = calendarObj.time
                    var date = dsc.getDateToStringConversion(dateIterated)
                    var date1 = dsc.getDateToStringConversion(timeEnd)
                    eventStartTime = getTimeInFormat(date, calendarObj)
                    eventEndTime = getTimeInFormat(date1, calendarObj)
                    status=false

                }

                if (format.format(dateIterated).equals(format.format(Date()))) {
                    if (eventList.isEmpty() == false) {
                        //element.setCompoundDrawablePadding(3)
                        element.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.bullet_white)
                    }
                }
                else{
                    if (eventList.isEmpty() == false) {
                       // element.setCompoundDrawablePadding(3)
                        element.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.bullet1)
                    }
                }


            } else {
                element.isEnabled = false
            }
            element.setOnClickListener {

                if (it == currentDateElement) {
                    currentDateElement.setBackgroundResource(R.drawable.circle_shape)
                    currentDateElement.setTextColor(Color.WHITE)
                    bottom_date_holder.visibility = View.INVISIBLE
                    iteration = 1
                    var ge = GetEvent(context!!)
                activity!!.setDateCommon(Date())
                    eventList = ge.getEvent(Date(), dsc, calendar, "sharmilabegum97@gmail.com")
                    //if (format1.format(dateIterated).equals(format1.format(Date()))) {
                        if (eventList.isEmpty() == false) {
                           // currentDateElement.setCompoundDrawablePadding(3)
                            currentDateElement.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.bullet_white)
                        }
                   // }
                   /* if (eventList!!.isEmpty() == false) {

                        if (format.format(dateIterated).equals(format.format(Date()))) {
                            element.setCompoundDrawablePadding(5)
                            element.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.bullet_white)
                        }
                    }*/




                }
                else {
                    // currentDateElement.animate().translationY(400.toFloat());
                    /* val transAnimation = TranslateAnimation(location1[0].toFloat(),location[0].toFloat(), location1[1].toFloat(),location[1].toFloat())
                       currentDateElement.startAnimation(transAnimation)
                       transAnimation.duration=10000*/
                    var calendarrr=Calendar.getInstance()
                    calendarrr.time=startDate
                    calendarrr.set(Calendar.DAY_OF_MONTH,Integer.parseInt(element.text.toString()))
                    activity!!.setDateCommon(calendarrr.time)
                    bottom_date_holder.visibility = View.VISIBLE
                    it.setBackgroundResource(R.drawable.colour_transparent)
                    var ge = GetEvent(context!!)
                    eventList = ge.getEvent(Date(), dsc, calendar, "sharmilabegum97@gmail.com")
                    if (format1.format(dateIterated).equals(format1.format(Date()))) {

                        //if (eventList.isEmpty() == false) {
                            currentDateElement.setTextColor(Color.BLUE)
                        if (eventList.isEmpty() == false) {
                            //currentDateElement.setCompoundDrawablePadding(3)
                            currentDateElement.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.bullet1)
                        }
                        //}
                    }
                    //else{
                        //currentDateElement.setTextColor(Color.WHITE)
                    //}

                }
                changeInOtherViews(it, container1, eventList)
                var calendarObj: Calendar = Calendar.getInstance()
                var calobj:Calendar= Calendar.getInstance()
                calobj.setTime(startDate)
                calendarObj.set(Calendar.DAY_OF_MONTH,calobj.get(Calendar.DAY_OF_MONTH))
                calendarObj.set(Calendar.MONTH,calobj.get(Calendar.MONTH))
                calendarObj.set(Calendar.YEAR,calobj.get(Calendar.YEAR))

                calendarObj.set(Calendar.DAY_OF_MONTH, Integer.valueOf(element.text.toString()))
                var dateIterated = calendarObj.time
                calendarObj.add(Calendar.HOUR, 1)
                var timeEnd = calendarObj.time
                var date = dsc.getDateToStringConversion(dateIterated)
                var date1 = dsc.getDateToStringConversion(timeEnd)
                eventStartTime = getTimeInFormat(date, calendarObj)
                eventEndTime = getTimeInFormat(date1, calendarObj)

                if (itemsAdapter != null) {
                    eventlist.removeAll(eventlist)
                    itemsAdapter!!.notifyDataSetChanged()

                }


                if ((it as TextView).text.toString().equals("") == false) {
                    var date: Int = Integer.valueOf(it.text.toString())
                    cal1.set(Calendar.DAY_OF_MONTH, date)
                    var dateIterated = cal1.time
                    var ge = GetEvent(context!!)
                    var eventList = ge.getEvent(dateIterated, dsc, calendar, "sharmilabegum97@gmail.com")
                    if (eventList.isEmpty() == false) {

                        eventlist = eventList
                        val names = arrayOfNulls<String>(eventList.size)
                        for (i in 0..eventList.size - 1) {
                            names[i] = eventList[i].title
                        }

                        events_title.visibility = View.VISIBLE
                        eventListView.visibility = View.VISIBLE
                        itemsAdapter =
                            ArrayAdapter(context!!, android.R.layout.simple_list_item_1, names!! as Array<String>)
                        eventListView.adapter = itemsAdapter
                    } else {
                        events_title.visibility = View.INVISIBLE
                        eventListView.visibility = View.INVISIBLE
                        var values = ArrayList<String>()
                        values.clear()
                        val adapter = ArrayAdapter<String>(context, R.layout.item_layout, android.R.id.text1, values)
                        adapter.notifyDataSetChanged()
                        eventListView.adapter = adapter
                    }

                }
                false

            }
        }
        if(status==true){
           monthStartDate.setBackgroundResource(R.drawable.colour_transparent)
            monthStartDate.setTextColor(Color.BLACK)
            monthStartDate.setClickable(true)
           /* var calendarObj: Calendar = Calendar.getInstance()
            calendarObj.setTime(startDate)
            calendarObj.set(Calendar.DAY_OF_MONTH, Integer.valueOf( monthStartDate.text.toString()))
            var dateIterated = calendarObj.time
            calendarObj.add(Calendar.HOUR, 1)
            var timeEnd = calendarObj.time
            var date = dsc.getDateToStringConversion(dateIterated)
            var date1 = dsc.getDateToStringConversion(timeEnd)
            eventStartTime = getTimeInFormat(date, calendarObj)
            eventEndTime = getTimeInFormat(date1, calendarObj)*/

        }
        bottom_date_holder.setOnClickListener {
            location1[0] = it.getX().toInt()
            location1[1] = it.getY().toInt()
            val activity = activity as MainActivity
            //activity!!.date=Date()
            activity!!.setDateCommon(Date())
            activity!!.getCurrentMonthView()
        }
        eventListView.setOnItemClickListener(object : AdapterView.OnItemClickListener {


            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                for (i in 0..eventlist.size - 1) {
                    if (eventlist.get(i).title.equals(parent!!.getItemAtPosition(position))) {
                        val i: Intent =
                            Intent(context, ViewEvent(eventlist.get(i)!!, "sharmilabegum97@gmail.com")::class.java)
                        startActivityForResult(i, 1);
                    }
                }
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

    private fun clearForm(group: ViewGroup?, spaces: Int, noOfDays: Int, lastDayofMonth: Int) {
        var i = 1

        var date = 1
        val count = group?.childCount
        while (i < count!!) {
            var view = group?.getChildAt(i)

            if (view is TextView) {
                if (i < 7 + spaces || date > noOfDays) {

                } else {

                    view.setText(date.toString())
                    date++
                }
            }

            if (view is ViewGroup && view.childCount > 0)
                clearForm(view, spaces, noOfDays, lastDayofMonth)
            i++
        }
        var j = lastDayofMonth - date + 1
        var k = 1
        while (date - 1 < noOfDays && k <= j) {
            val view = group?.getChildAt(k + 6)
            k++
            if (view is TextView) {
                view.setText(date.toString())
                date++

            }
            if (view is ViewGroup && view.childCount > 0)
                clearForm(view, spaces, noOfDays, lastDayofMonth)

        }


    }

    fun optimizeSize(group: ViewGroup?) {
        var item = 0

        val count = group?.childCount
        while (item < count!!) {

            var view = group?.getChildAt(item)
            setDimensions(view, ((screenWidthCommon - 30) / 7.5).toInt(), (screenWidthCommon - 30) / 7.5.toInt()-10)
            item++
        }
    }

    fun changeInOtherViews(view: View, gridLayout: GridLayout, eventList: ArrayList<Event>) {
        val childCount = gridLayout.childCount
        for (i in 7 until childCount) {
            val element = gridLayout.getChildAt(i) as TextView
            if (element != view) {
                element.setBackgroundResource(R.drawable.bg_transparent)
            }

        }
    }

    private fun setDimensions(view: View, width: Int, height: Int) {
        val params = view.layoutParams
        params.width = width
        params.height = height
        view.layoutParams = params
    }


   /* override fun onResume() {
        super.onResume()
       var gridLayout=view1.findViewById<GridLayout>(R.id.gridlayout)
            val childCount = gridLayout.childCount
            for (i in 7 until childCount) {
                val element = gridLayout.getChildAt(i) as TextView
                if (element.text.toString().equals("") == false) {
                    var date: Int = Integer.valueOf(element.text.toString())
                    cal1.set(Calendar.DAY_OF_MONTH, date)
                    var dateIterated = cal1.time
                    var ge = GetEvent(context!!)
                    var eventList = ge.getEvent(dateIterated, dsc, calendar, "sharmilabegum97@gmail.com")
                    if (eventList.isEmpty() == false) {

                        if (format.format(dateIterated).equals(format.format(Date()))) {
                            element.setCompoundDrawablePadding(5)
                            element.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.bullet_white)
                        } else
                            element.setCompoundDrawablePadding(5)
                        element.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.bullet1)

                    }
                }

            }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {

            if (resultCode == 1) {
                    monthPagerAdapter.notifyDataSetChanged()
                val childCount = gridLayout.childCount
                for (i in 7 until childCount) {
                    val element = gridLayout.getChildAt(i) as TextView
                    if (element.text.toString().equals("") == false) {
                        var date: Int = Integer.valueOf(element.text.toString())
                        cal1.set(Calendar.DAY_OF_MONTH, date)
                        var dateIterated = cal1.time
                        var ge = GetEvent(context!!)
                        var eventList = ge.getEvent(dateIterated, dsc, calendar, "sharmilabegum97@gmail.com")
                        if (eventList.isEmpty() == false) {

                            if (format.format(dateIterated).equals(format.format(Date()))) {
                                element.setCompoundDrawablePadding(5)
                                element.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.bullet_white)
                            } else
                                element.setCompoundDrawablePadding(5)
                            element.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.bullet1)

                        } else {
                            element.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)

                        }

                    }


                }
            }
        }
    }*/

    fun getTimeInFormat(date: String, cal: Calendar): ArrayList<String> {
        var time = ArrayList<String>()
        if (date.substring(14, 16).toInt() >= 0 && date.substring(14, 16).toInt() < 30) {
            time.add(date.substring(0, 10))
            time.add(date.substring(11, 14) + "30 " + date.substring(17, 19))
        } else if (date.substring(14, 16).toInt() >= 30 && date.substring(14, 16).toInt() <= 59) {
            var dateAlone = date.substring(0, 10)
            var amOrPm = date.substring(17, 19)
            var timeInt = Integer.valueOf(date.substring(11, 13))
            time.add(dateAlone)
            time.add(String.format("%02d", timeInt) + ":00 " + amOrPm)
        }

        return time
    }



}