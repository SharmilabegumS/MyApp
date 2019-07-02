package com.example.Calendar


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import java.util.*
import android.widget.*
import java.text.SimpleDateFormat
import android.widget.TextView
import com.example.Calendar.domain.GetEvent
import android.widget.ArrayAdapter
import com.example.Calendar.entity.Event
import android.widget.AdapterView
import com.example.Calendar.dataBase.DataBaseManager
import kotlin.collections.ArrayList


var date: Date = Date()
var calendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
var month: Int = calendar.get(Calendar.MONTH)
var year: Int = calendar.get(Calendar.YEAR)


var eventlist = ArrayList<Event>()
var itemsAdapter: ArrayAdapter<String>? = null

var context1: Context? = null
var i = 1


var eventStartTime = ArrayList<String>()
var eventEndTime = ArrayList<String>()


var status: Boolean = true
private lateinit var currentDateElement: TextView

class MonthFragment : androidx.fragment.app.Fragment() {
    var datePassed: Date? = Date()
    private lateinit var view1: View
    private lateinit var monthStartDate: TextView
    private var containerToBeProcessed: ViewGroup? = null
    private lateinit var gridLayout: GridLayout

    private lateinit var eventListView: ListView
    private var cal1: Calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
    private val format = SimpleDateFormat("dd/MM/yyyy")
    private val format1 = SimpleDateFormat("MM/yyyy")
    private var iteration = 1

    @SuppressLint("NewApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_month, container, false)
        view1 = view
        status = true
        setHasOptionsMenu(true)
        val bundle = arguments
        val value = bundle!!.getString("monthYear")
        calendar.set(Calendar.MONTH, Integer.parseInt(value!!.substring(0, 2)))
        calendar.set(Calendar.YEAR, Integer.parseInt(value.substring(2, 6)))
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        datePassed = calendar.time
        date = calendar.time
        val activity = activity as MainActivity
        month = calendar.get(Calendar.MONTH)
        year = calendar.get(Calendar.YEAR)
        containerToBeProcessed = container
        var startDate = getFirstDateOfMonth(date, calendar)
        var endDate = getLastDateOfMonth(date, calendar)
        var calendarStartDate = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
        calendarStartDate.time = startDate
        calendarStartDate.set(Calendar.HOUR_OF_DAY, 0)

        calendarStartDate.set(Calendar.MINUTE, 0)
        calendarStartDate.set(Calendar.SECOND, 1)
        startDate = calendarStartDate.time
        var calendarEndDate = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
        calendarEndDate.time = endDate
        calendarEndDate.set(Calendar.HOUR_OF_DAY, 23)
        calendarEndDate.set(Calendar.MINUTE, 59)
        calendarEndDate.set(Calendar.SECOND, 59)
        endDate = calendarEndDate.time
        val dbm = DataBaseManager(context!!)

        val bottom_date_holder: TextView = activity.bottomDateHolder
        val lastDayofMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val firstDayofMonth = calendar.getMinimalDaysInFirstWeek()
        val noOfDays = lastDayofMonth - firstDayofMonth + 1
        val monthName = SimpleDateFormat("MMM").format(startDate)
        val currentMonthName = SimpleDateFormat("MMM").format(Date())
        val currentYear = SimpleDateFormat("yyyy").format(Date())
        if (!currentMonthName.equals(monthName) && !currentYear.equals(year)) {
            bottom_date_holder.visibility = View.VISIBLE
        }
        calendar.time = startDate
        val spaces: Int = calendar.get(Calendar.DAY_OF_WEEK) - 1
        (getActivity() as MainActivity).setActionBarTitle("Month")

        optimizeSize(view.findViewById(R.id.gridlayout))

        lateinit var events_title: TextView
        eventListView = view.findViewById(R.id.event_list)
        var calBottomDateHolder=Calendar.getInstance()
        calBottomDateHolder.time= Date()
        var dateInteger = calBottomDateHolder.get(Calendar.DAY_OF_MONTH)
        bottom_date_holder.text = dateInteger.toString()
        bottom_date_holder.setBackgroundResource(R.drawable.circle_shape)
        setDimensions(
            bottom_date_holder,
            ((screenWidthCommon - 30) / 7.5).toInt(),
            (screenWidthCommon - 30) / 7.5.toInt() - 10
        )
        val month: TextView = view.findViewById(R.id.month_name)
        val year1: TextView = view.findViewById(R.id.year1)
        month.text = monthName
        year1.setText("$year")
//        activity!!.setDateCommon(startDate)

        clearForm(view.findViewById(R.id.gridlayout), spaces, noOfDays, lastDayofMonth)

        val monthEvents = dbm.getAllEventsOfMonth(
            dsc.getDateInMillis1(startDate),
            dsc.getDateInMillis1(endDate),
            userName
        )
        val ge = GetEvent(context!!)
        val currentDateEvents = ge.getEvent(Date(), dsc, calendar, userName)

        val container1 = view.findViewById(R.id.gridlayout) as GridLayout
        events_title = view.findViewById(R.id.events_title)

        val cal: Calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
        cal.setTime(Date())
        cal1.setTime(startDate)
        gridLayout = container1
        val childCount = container1.childCount
        for (i in 7 until childCount) {
            val element = container1.getChildAt(i) as TextView
            var date: Int
            var dateIterated = Date()
            val eventList = ArrayList<Event>()
            if (element.text.toString().equals("") == false) {
                date = Integer.valueOf(element.text.toString())
                cal1.set(Calendar.DAY_OF_MONTH, date)
                dateIterated = cal1.time


                val dateStartInMillis = dsc.getDateInMillis1(dateIterated)
                cal.setTime(dateIterated)
                cal.set(Calendar.HOUR_OF_DAY, 23)
                cal.set(Calendar.MINUTE, 59)
                cal.set(Calendar.SECOND, 59)

                val dateEndInMillis = dsc.getDateInMillis1(cal.time)
                for (i in 0..monthEvents.size - 1) {
                    val fromDate = monthEvents.get(i).startDate
                    val toDate = monthEvents.get(i).endDate
                    if ((fromDate!! >= dateStartInMillis && fromDate <= dateEndInMillis) or (toDate!! >= dateStartInMillis && toDate <= dateEndInMillis)) {
                        eventList.add(monthEvents.get(i))
                    }

                }
                if (element.text == "1") {
                    element.isClickable = true
                    monthStartDate = element
                }
                if (format.format(dateIterated).equals(format.format(Date()))) {

                    element.setBackgroundResource(R.drawable.circle_shape)
                    element.setTextColor(Color.WHITE)
                    currentDateElement = element
                    bottom_date_holder.visibility = View.INVISIBLE
                    val calendarObj: Calendar = Calendar.getInstance()
                    val calobj: Calendar = Calendar.getInstance()
                    calobj.setTime(startDate)
                    //activity!!.setDateCommon(Date())
                   // calendarObj.set(Calendar.db.DAY_OF_MONTH, calobj.get(Calendar.db.DAY_OF_MONTH))
                    calendarObj.set(Calendar.MONTH, calobj.get(Calendar.MONTH))
                    calendarObj.set(Calendar.YEAR, calobj.get(Calendar.YEAR))
                    calendarObj.set(Calendar.DAY_OF_MONTH, Integer.valueOf(element.text.toString()))
                    val dateIterated = calendarObj.time
                    calendarObj.add(Calendar.HOUR, 1)
                    val timeEnd = calendarObj.time
                    val date = dsc.getDateToStringConversion(dateIterated)
                    val date1 = dsc.getDateToStringConversion(timeEnd)
                    eventStartTime = getTimeInFormat(date, calendarObj)
                    eventEndTime = getTimeInFormat(date1, calendarObj)
                    status = false

                }


                if (format.format(dateIterated).equals(format.format(Date()))) {
                    if (eventList.isEmpty() == false) {
                        element.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.bullet_white)
                    }
                } else {
                    if (eventList.isEmpty() == false) {
                        element.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.bullet_blue)
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
                    activity.setDateCommon(Date())
                    if (currentDateEvents.isEmpty() == false) {
                        currentDateElement.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.bullet_white)
                    }


                } else {
                    val calendarrr = Calendar.getInstance()
                    calendarrr.time = startDate

                    calendarrr.set(Calendar.DAY_OF_MONTH, Integer.parseInt(element.text.toString()))
                    var dateChoosen=calendarrr.time
                    activity.setDateCommon(calendarrr.time)

                    bottom_date_holder.visibility = View.VISIBLE
                    it.setBackgroundResource(R.drawable.colour_transparent)
                    if (format1.format(dateIterated).equals(format1.format(Date()))) {

                        currentDateElement.setTextColor(Color.BLUE)
                        if (currentDateEvents.isEmpty() == false) {
                            currentDateElement.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.bullet_blue)
                        }

                    }
                }
                changeInOtherViews(it, container1, eventList)
                val calendarObj: Calendar = Calendar.getInstance()
                val calobj: Calendar = Calendar.getInstance()
                calobj.time = startDate
                calendarObj.set(Calendar.DAY_OF_MONTH, calobj.get(Calendar.DAY_OF_MONTH))
                calendarObj.set(Calendar.MONTH, calobj.get(Calendar.MONTH))
                calendarObj.set(Calendar.YEAR, calobj.get(Calendar.YEAR))

                calendarObj.set(Calendar.DAY_OF_MONTH, Integer.valueOf(element.text.toString()))

                val dateIterated = calendarObj.time
                calendarObj.add(Calendar.HOUR_OF_DAY, 1)
                val timeEnd = calendarObj.time
                val date = dsc.getDateToStringConversion(dateIterated)
                val date1 = dsc.getDateToStringConversion(timeEnd)
                eventStartTime = getTimeInFormat(date, calendarObj)
                eventEndTime = getTimeInFormat(date1, calendarObj)

                if (itemsAdapter != null) {
                    eventlist.removeAll(eventlist)
                    itemsAdapter!!.notifyDataSetChanged()

                }


                if (!(it as TextView).text.toString().equals("")) {
                    val date: Int = Integer.valueOf(it.text.toString())
                    cal1.set(Calendar.DAY_OF_MONTH, date)
                    val dateIterated = cal1.time
                    val eventsListOfDate = ArrayList<Event>()

                    val dateStartInMillis = dsc.getDateInMillis1(dateIterated)

                    cal.setTime(dateIterated)
                    cal.set(Calendar.HOUR_OF_DAY, 23)
                    cal.set(Calendar.MINUTE, 59)
                    cal.set(Calendar.SECOND, 59)

                    val dateEndInMillis = dsc.getDateInMillis1(cal.time)
                    for (i in 0..monthEvents.size - 1) {
                        val fromDate = monthEvents.get(i).startDate
                        val toDate = monthEvents.get(i).endDate
                        if ((fromDate!! >= dateStartInMillis && fromDate <= dateEndInMillis) or (toDate!! >= dateStartInMillis && toDate <= dateEndInMillis)) {
                            eventsListOfDate.add(monthEvents.get(i))
                        }
                    }
                    if (eventsListOfDate.isEmpty() == false) {
                        eventlist = eventsListOfDate
                        val names = arrayOfNulls<String>(eventsListOfDate.size)
                        for (i in 0..eventsListOfDate.size - 1) {
                            names[i] = eventsListOfDate[i].title
                        }

                        events_title.visibility = View.VISIBLE
                        eventListView.visibility = View.VISIBLE
                        itemsAdapter =
                            ArrayAdapter(context!!, R.layout.event_list_layout, R.id.itemText, names as Array<String>)
                        eventListView.adapter = itemsAdapter
                    } else {
                        events_title.visibility = View.INVISIBLE
                        eventListView.visibility = View.INVISIBLE
                        val values = ArrayList<String>()
                        values.clear()
                        val adapter = ArrayAdapter<String>(context, R.layout.item_layout, android.R.id.text1, values)
                        adapter.notifyDataSetChanged()
                        eventListView.adapter = adapter
                    }

                }
            }
        }
        if (status == true) {
            /* monthStartDate.performClick()
             monthStartDate.setBackgroundResource(R.drawable.colour_transparent)
             monthStartDate.setTextColor(Color.BLACK)
             monthStartDate.setClickable(true)*/

            //bottom_date_holder.visibility = View.VISIBLE


        } else {
            // currentDateElement.performClick()

            //bottom_date_holder.visibility = View.INVISIBLE


        }
        bottom_date_holder.setOnClickListener {
            val activity = activity
            activity.setDateCommon(Date())
            activity.getMonthList()
            activity.getCurrentMonthView()
        }
        eventListView.setOnItemClickListener(object : AdapterView.OnItemClickListener {


            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                for (i in 0..eventlist.size - 1) {
                    if (eventlist.get(i).title.equals(parent!!.getItemAtPosition(position))) {
                        val intent =
                            Intent(context, ViewEvent::class.java)
                        intent.putExtra("ID", eventlist.get(i)!!.eventId)
                        startActivityForResult(intent, 1)

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
            val view = group.getChildAt(i)

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
        val j = lastDayofMonth - date + 1
        var k = 1
        while (date - 1 < noOfDays && k <= j) {
            val view = group.getChildAt(k + 6)
            k++
            if (view is TextView) {
                view.setText(date.toString())
                date++

            }
            if (view is ViewGroup && view.childCount > 0)
                clearForm(view, spaces, noOfDays, lastDayofMonth)

        }


    }

    private fun optimizeSize(group: ViewGroup?) {
        var item = 0

        val count = group?.childCount
        while (item < count!!) {

            val view = group.getChildAt(item)
            setDimensions(view, ((screenWidthCommon - 30) / 7.5).toInt(), (screenWidthCommon - 30) / 7.5.toInt() - 10)
            item++
        }
    }

    private fun changeInOtherViews(view: View, gridLayout: GridLayout, eventList: ArrayList<Event>) {
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


    private fun getTimeInFormat(date: String, cal: Calendar): ArrayList<String> {
        val time = ArrayList<String>()
        if (date.substring(14, 16).toInt() >= 0 && date.substring(14, 16).toInt() < 30) {
            time.add(date.substring(0, 10))
            time.add(date.substring(11, 14) + "30 " + date.substring(17, 19))
        } else if (date.substring(14, 16).toInt() >= 30 && date.substring(14, 16).toInt() <= 59) {
            val dateAlone = date.substring(0, 10)
            val amOrPm = date.substring(17, 19)
            val timeInt = Integer.valueOf(date.substring(11, 13))
            time.add(dateAlone)
            time.add(String.format("%02d", timeInt) + ":00 " + amOrPm)
        }

        return time
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.jump_to_date).setVisible(true)
        menu.findItem(R.id.calculate_date).setVisible(true)
        super.onCreateOptionsMenu(menu, inflater)
    }

}
