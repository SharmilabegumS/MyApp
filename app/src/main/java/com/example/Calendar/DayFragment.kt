package com.example.Calendar


import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.*
import java.text.SimpleDateFormat
import java.util.*
import android.content.Intent
import android.widget.TextView
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.example.Calendar.dataBase.DataBaseManager
import com.example.Calendar.entity.Event
import kotlin.collections.ArrayList
import android.widget.RelativeLayout
import com.example.Calendar.domain.GetEvent
import kotlin.collections.HashMap


var dayCalendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
var calendarDayView: Calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
val format = SimpleDateFormat("dd/MM/yyyy")


var prevTextViewId = 0

var previousList = ArrayList<Event>()


@SuppressLint("NewApi")
class DayFragment : androidx.fragment.app.Fragment() {
    var dayDate: Date = Date()
    var previousClickedView: TextView? = null
    lateinit var currentTimeLine: View
    lateinit var textViewLinearLayout: LinearLayout
    lateinit var day1: RelativeLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true);
        (getActivity() as MainActivity).setActionBarTitle("Day")
        val activity = activity as MainActivity
        val view = inflater.inflate(R.layout.fragment_day, container, false)
        val bundle = arguments
        val value = bundle!!.getString("monthYear")
        calendar.set(Calendar.MONTH, Integer.parseInt(value!!.substring(2, 4)))
        calendar.set(Calendar.YEAR, Integer.parseInt(value.substring(4, 8)))
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(value.substring(0, 2)))
        context1 = context
        dayDate = calendar.time
        var container1 = view.findViewById<GridLayout>(R.id.container)
        dayCalendar.setTime(dayDate);
        val dayId: TextView = view.findViewById(R.id.date_holder)
        val dayStringTV: TextView = view.findViewById(R.id.day_string)
        val monthTv: TextView = view.findViewById(R.id.month_name)
        val yearTv: TextView = view.findViewById(R.id.year1)
        currentTimeLine = view.findViewById<View>(R.id.current_time)

        val paramsAllDay: RelativeLayout.LayoutParams = currentTimeLine.layoutParams as RelativeLayout.LayoutParams
        paramsAllDay.width = screenWidthCommon
        currentTimeLine.setLayoutParams(paramsAllDay);
        val timeSlotAndEventsHashMap = HashMap<String, ArrayList<Event>>()
        day1 = view.findViewById(R.id.day_fragment)
        val bottom_date_holder = activity.bottomDateHolder
        textViewLinearLayout = view.findViewById<LinearLayout>(R.id.slot11am)

        val start = SimpleDateFormat("dd").format(dayDate)

        //activity.setDateCommon(dayDate)

        val dayString = SimpleDateFormat("EEE").format(dayDate)
        val monthName = SimpleDateFormat("MMM").format(dayDate)
        val yearOfDate = SimpleDateFormat("yyyy").format(dayDate)

        dayId.text = Integer.parseInt(start).toString()
        dayStringTV.text = dayString
        monthTv.text = monthName
        yearTv.text = yearOfDate
        val currentDate = SimpleDateFormat("dd/MM/yyyy").format(Date())
        bottom_date_holder.setText(Integer.parseInt(currentDate.substring(0, 2)).toString())
        if (SimpleDateFormat("dd/MM/yyyy").format(dayDate).equals(currentDate)) {

            dayId.setBackgroundResource(R.drawable.botton_date_holder)
            dayId.setTextColor(Color.WHITE)
            bottom_date_holder.visibility = View.INVISIBLE
        } else {
            dayId.setBackgroundResource(R.drawable.other_date_holder)
            dayId.setTextColor(Color.BLACK)
            bottom_date_holder.visibility = View.VISIBLE
        }
        val cal: Calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
        val time = dsc.getTimeAlone(cal.time)
        val hourAlone = time.substring(0, 2) + time.substring(5, 8)
        val gridLayout = view.findViewById<GridLayout>(R.id.day_grid_layout)

        clearForm(gridLayout)
        calendarDayView.setTime(dayDate)
        val dateIterated = calendarDayView.time
        var iteration1 = 0
        val idList = ArrayList<Int>()
        val counting = gridLayout?.childCount
        val ge = GetEvent(context!!)
        val dbm = DataBaseManager(context!!)
        while (iteration1 < counting!!) {
            val view = gridLayout.getChildAt(iteration1)

            if (view is LinearLayout) {
                val timeSlot = gridLayout.getChildAt(iteration1 - 1)
                if (timeSlot is TextView) {
                    val hour = timeSlot.text.substring(0, 2)
                    val amOrPm = timeSlot.text.substring(3, 5)
                    val startTime = getDateForTimeSlot(hour + ":00 " + amOrPm, dateIterated)
                    val endTime = startTime + 3600000
                    val list = dbm.getEventsOfTimeSlots(startTime, endTime, userName)
                    timeSlotAndEventsHashMap.put(hour + ":00 " + amOrPm, list)
                    if (list.isEmpty() == false) {
                        idList.add(view.getChildAt(0).id)
                    }

                }
            }
            iteration1++
        }


        var index = 0

        val count = gridLayout.childCount
        while (index < count) {
            val view = gridLayout.getChildAt(index)

            if (view is LinearLayout) {
                if (view.childCount == 1) {
                    val textView = view.getChildAt(0)
                    if (textView is TextView) {
                        textView.setOnClickListener {
                            val currentClickedView = textView
                            if (previousClickedView != null) {
                                previousClickedView!!.setBackgroundColor(Color.TRANSPARENT)
                                previousClickedView!!.setText(" ")
                                previousClickedView!!.setTextColor(Color.BLACK)
                                previousClickedView!!.setBackgroundResource(R.drawable.textview_border)

                            }

                            textView.setBackgroundResource(R.color.event_add)
                            textView.setText("+ New Event")
                            textView.setPadding(10, 10, 10, 10)
                            textView.setTextColor(Color.WHITE)


                            val intent = Intent(context, AddEvent::class.java);
                            intent.putExtra("startDate", eventStartTime[0])
                            intent.putExtra("startTime", eventStartTime[1])
                            intent.putExtra("endDate", eventEndTime[0])
                            intent.putExtra("endTime", eventEndTime[1])
                            startActivity(intent);

                            previousClickedView = currentClickedView
                        }
                    }
                }
            }
            index++


        }
        var j = 0
        val count1 = gridLayout.childCount
        var time1: String = ""

        var previousLinearLayout: LinearLayout? = null
        while (j < count1) {
            val view = gridLayout.getChildAt(j)


            if (view is TextView) {
                time1 = view.text.toString().toUpperCase()
                time1 = time1.substring(0, 2) + ":00" + time1.substring(2, 5)
            }


            if (view is LinearLayout) {
                if (j >= 3) {
                    if (gridLayout.getChildAt(j - 2) is LinearLayout) {
                        previousLinearLayout = gridLayout.getChildAt(j - 2) as LinearLayout
                    }
                }

                if (timeSlotAndEventsHashMap.containsKey(time1.toLowerCase())) {
                    val eventList = timeSlotAndEventsHashMap.get(time1.toLowerCase())

                    if (idList.contains(view.getChildAt(0).id)) {
                        view.removeView(view.getChildAt(0))
                    }
                    createTextView1(eventList!!, view, previousLinearLayout)

                }


            }
            j++
        }


        if (format.format(dayDate).equals(format.format(Date()))) {
            currentTimeLine.visibility = View.VISIBLE
            var i = 0

            val count = gridLayout?.childCount
            while (i < count!!) {
                val view = gridLayout?.getChildAt(i)

                if (view is TextView) {
                    if (i % 2 == 0) {

                        if ((view.text).equals(hourAlone.toLowerCase())) {
                            val paramsAllDay: ViewGroup.LayoutParams = view.getLayoutParams()
                            val positionstarts = (i / 2) * paramsAllDay.height
                            val oneMinute = paramsAllDay.height / 59
                            var top = 0
                            if (time.substring(5, 8).equals("AM") || time.substring(0, 2).equals("12")) {
                                top = positionstarts + oneMinute * Integer.parseInt(time.substring(3, 5))

                            } else {
                                top = positionstarts + oneMinute * (Integer.parseInt(time.substring(3, 5)) + 12)

                            }



                      //      currentTimeLine.animate().translationY(top.toFloat())


                            currentTimeLine.setTranslationY(top.toFloat())
                        }

                    }


                }
                i++


            }
        } else {
            currentTimeLine.visibility = View.INVISIBLE
        }



        bottom_date_holder.setOnClickListener {
            val activity = activity as MainActivity
            activity.setDateCommon(Date())
            activity.getDayList()
            activity.getCurrentDayView()
        }
        return view
    }

    private fun getDateForTimeSlot(s: String, date: Date): Long {
        val cal: Calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
        cal.setTime(date)
        if (s.substring(6, 8).toLowerCase().equals("am")) {
            cal.set(Calendar.AM_PM, Calendar.AM)
            if (s.substring(0, 2).equals("12")) {
                cal.set(Calendar.HOUR_OF_DAY, 0)
            } else {
                cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(s.substring(0, 2)))
            }

        } else if (s.substring(6, 8).toLowerCase().equals("pm")) {
            cal.set(Calendar.AM_PM, Calendar.PM)
            if (s.substring(0, 2).equals("12")) {
                cal.set(Calendar.HOUR_OF_DAY, 12)
            } else {
                cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(s.substring(0, 2)) + 12)
            }

        }
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)

        val date1 = dsc.getDateInMillis1(cal.time)
        return date1

    }

    private fun createTextView1(
        eventList: ArrayList<Event>,
        linearLayout: LinearLayout,
        previousLinearLayout: LinearLayout?
    ) {

        val previousListIds = ArrayList<String>()
        val currentListIds = ArrayList<String>()
        for (l in 0..previousList.size - 1) {
            previousListIds.add(previousList.get(l).eventId)
        }
        for (l in 0..eventList.size - 1) {
            currentListIds.add(eventList.get(l).eventId)
        }
        previousListIds.retainAll(currentListIds);

        if (previousListIds.size != 0) {

            if (eventList.size > previousList.size) {
                previousLinearLayout!!.weightSum = eventList.size.toFloat()
            } else {
                previousLinearLayout!!.weightSum = previousList.size.toFloat()
            }
        }
        linearLayout.weightSum = eventList.size.toFloat()
        for (i in 0..eventList.size - 1) {
            val textView1 = TextView(context1)
            var layoutParams: LinearLayout.LayoutParams?
            var height = 0
            val date = dsc.getDateFromMillis(eventList.get(i).startDate)
            val date1 = dsc.getDateFromMillis(eventList.get(i).endDate)
            val calendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
            calendar.time = date
            val calendar1: Calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
            calendar1.time = date1
            height = (screenHeightCommon / 15)
            layoutParams = LinearLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, height, 1.0f

            )
            textView1.setPadding(2, 2, 2, 2)
            textView1.layoutParams = layoutParams
            textView1.text = eventList.get(i).title
            textView1.visibility = View.VISIBLE
            textView1.setTextColor(Color.BLUE)
            textView1.setTextColor(ContextCompat.getColor(context!!, R.color.colorPrimary));
            textView1.setBackgroundResource(R.drawable.week_event_background) // hex color 0xAARRGGBB
            linearLayout.addView(textView1)
            val curTextViewId = prevTextViewId + 1
            textView1.setId(curTextViewId)
            val firstEvent = linearLayout.findViewById<TextView>(curTextViewId)
            firstEvent.setOnClickListener {
                val intent: Intent =
                    Intent(context, ViewEvent::class.java)
                intent.putExtra("ID", eventList.get(i).eventId)
                startActivityForResult(intent, 1)
            }
            prevTextViewId = curTextViewId
        }
        previousList = eventList

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

        if (view is TextView) {
            setDimensions(view, (screenWidthCommon / 6), (screenHeightCommon / 15))
        } else if (view is LinearLayout) {
            setDimensions(view, 5 * (screenWidthCommon / 6) - 10, (screenHeightCommon / 15))

        }
        i++

    }
}

private fun setDimensions(view: View, width: Int, height: Int) {
    val params = view.layoutParams
    params.width = width
    params.height = height
    view.layoutParams = params
}
