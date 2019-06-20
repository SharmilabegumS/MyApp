package com.example.navigationcheck


import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.*
import android.widget.*
import java.text.SimpleDateFormat
import java.util.*
import android.content.Intent
import android.widget.TextView
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.example.navigationcheck.dataBase.DataBaseManager
import com.example.navigationcheck.entity.Event
import kotlin.collections.ArrayList
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.content_month.*
import kotlin.collections.HashMap


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
var dayDate: Date = Date()
var dayCalendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
lateinit var currentTime: String
var calendarDayView: Calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
val format = SimpleDateFormat("dd/MM/yyyy")
val format1 = SimpleDateFormat("hh:mm a")
lateinit var day1: RelativeLayout
lateinit var currentTimeLine: View
var prevTextViewId = 0
var previousClickedView: TextView? = null
var previousList = ArrayList<Event>()
lateinit var textViewLinearLayout: LinearLayout

/**
 * A simple [Fragment] subclass.
 *
 */
@SuppressLint("NewApi")
class DayFragment : androidx.fragment.app.Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (getActivity() as MainActivity).setActionBarTitle("Day")
        val view = inflater.inflate(R.layout.fragment_day, container, false)
        var bundle = arguments
        var value = bundle!!.getString("monthYear")
        calendar.set(Calendar.MONTH, Integer.parseInt(value.substring(2, 4)))
        calendar.set(Calendar.YEAR, Integer.parseInt(value.substring(4, 8)))
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(value.substring(0, 2)))
        context1 = context
        dayDate = calendar.time


        //var datePassed=getArguments()?.getLong("date")
        //println("date passed: "+datePassed)
        //calendar.setTime(dsc.getDateFromMillis(datePassed))
        //calendar.setTime(datePassed)
        datePassed = calendar.time
        var container1 = view.findViewById<GridLayout>(R.id.container)
        dayCalendar.setTime(dayDate);
        var dayId: TextView = view.findViewById(R.id.date_holder)
        var dayStringTV: TextView = view.findViewById(R.id.day_string)
        var monthTv: TextView = view.findViewById(R.id.month_name)
        var yearTv: TextView = view.findViewById(R.id.year1)
        currentTimeLine = view.findViewById<View>(R.id.current_time)

        var paramsAllDay: RelativeLayout.LayoutParams = currentTimeLine.layoutParams as RelativeLayout.LayoutParams
        //paramsAllDay.setMargins()
        paramsAllDay.width = screenWidthCommon
        currentTimeLine.setLayoutParams(paramsAllDay);
        var timeSlotAndEventsHashMap = HashMap<String, ArrayList<Event>>()


        var day_layout: LinearLayout = view.findViewById(R.id.day_layout)
        day1 = view.findViewById(R.id.day_fragment)
        // var width=(screenWidthCommon - 30) / 7.5
        //var height=(screenWidthCommon - 30) / 7.5-10
        //val layoutParams:RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(width.toInt(), height.toInt())


        //layoutParams.setMargins((screenWidthCommon / 6), start - 20, 4, 0)


        //bottomDateHolder.setPadding(16,16,16,16)
        // layoutParams.setMargins( 50, screenHeightCommon-200,0,0)
        // bottomDateHolder.layoutParams = layoutParams // hex color 0xAARRGGBB
        // in pixels (left, top, right, bottom)
        // textViewLinearLayout.removeView(textViewLinearLayout.getChildAt(0))
        //textViewLinearLayout.addView(textView1)
        // bottomDateHolder.animate().translationY((screenHeightCommon-20).toFloat());
        var bottom_date_holder: TextView = view.findViewById(R.id.bottom_date_holder)
        textViewLinearLayout = view.findViewById<LinearLayout>(R.id.slot11am)

        var paramsAllDay1: RelativeLayout.LayoutParams = bottom_date_holder.layoutParams as RelativeLayout.LayoutParams
        paramsAllDay1.setMargins(screenWidthCommon - 350, ((screenHeightCommon / 1.4) + 10).toInt(), 0, 0)
        bottom_date_holder.setLayoutParams(paramsAllDay1);

        var start = SimpleDateFormat("dd").format(dayDate)
        val activity = activity as MainActivity
        activity!!.setDateCommon(dayDate)

        var dayString = SimpleDateFormat("EEE").format(dayDate)
        var monthName = SimpleDateFormat("MMM").format(dayDate)
        var yearOfDate = SimpleDateFormat("yyyy").format(dayDate)

        dayId.setText(Integer.parseInt(start).toString())
        dayStringTV.setText("$dayString")
        monthTv.setText(monthName)
        yearTv.setText(yearOfDate)
        var currentDate = SimpleDateFormat("dd/MM/yyyy").format(Date())
        bottom_date_holder.setText(currentDate.substring(0, 2))
        if (SimpleDateFormat("dd/MM/yyyy").format(dayDate).equals(currentDate)) {

            dayId.setBackgroundResource(R.drawable.botton_date_holder)
            dayId.setTextColor(Color.WHITE)
            bottom_date_holder.visibility = View.INVISIBLE
        } else {
            dayId.setBackgroundResource(R.drawable.other_date_holder)
            dayId.setTextColor(Color.BLACK)
            bottom_date_holder.visibility = View.VISIBLE
        }
        var cal: Calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
        var time = dsc.getTimeAlone(cal.time)
        var hourAlone = time.substring(0, 2) + time.substring(5, 8)
        var gridLayout = view.findViewById<GridLayout>(R.id.day_grid_layout)

        clearForm(gridLayout)
        calendarDayView.setTime(dayDate)
        var dateIterated = calendarDayView.time
        var iteration1 = 0
        //dateCommon= dayDate
        //activity!!.date= dateIterated
        var idList = ArrayList<Int>()
        var counting = gridLayout?.childCount
        var dbm = DataBaseManager(context!!)
        while (iteration1 < counting!!) {
            var view = gridLayout?.getChildAt(iteration1)

            if (view is LinearLayout) {
                var timeSlot = gridLayout?.getChildAt(iteration1 - 1)
                if (timeSlot is TextView) {
                    var hour = timeSlot.text.substring(0, 2)
                    var amOrPm = timeSlot.text.substring(3, 5)
                    var startTime = getDateForTimeSlot(hour + ":00 " + amOrPm, dateIterated)

                    var endTime = startTime + 3600000
                    //println("start time: "+dsc.getDateFromMillis(startTime))
                    //println("end time: "+dsc.getDateFromMillis(endTime))
                    var list = dbm.getEventsOfTimeSlots(startTime, endTime, "sharmilabegum97@gmail.com")
                    // println(list)
                    // var list1 = dbm.getAllEventsOfTimeSlots(startTime,  endTime, "sharmilabegum97@gmail.com")

                    timeSlotAndEventsHashMap.put(hour + ":00 " + amOrPm, list)
                    if (list.isEmpty() == false) {
                        idList.add(view.getChildAt(0).id)
                    }

                }


            }
            iteration1++
        }

        var countList = ArrayList<Int>()
        /* for( i in 0..timeSlotAndEventsHashMap.size-1){
             timeSlotAndEventsHashMap.forEach({ k, v -> countList.add(v.size) })

         }*/


        /*   val entrySet = timeSlotAndEventsHashMap.entries

       // Obtaining an iterator for the entry set
       val it = entrySet.iterator()

       // Iterate through HashMap entries(Key-Value pairs)

       while (it.hasNext())
   {
   val me = it.next() as Map.Entry<*, *>
       var arrayList=me.value as ArrayList<Event>

      countList.add(arrayList.size)

   }
           var maxVal = Collections.max(countList)
   */
        /* var list1 = dbm.getAllEventsOfTimeSlots(1560772800000,  1560776400000, "sharmilabegum97@gmail.com")


         //println("dateI iterated: "+ dateIterated)
         //var list1 = dbm.getAllEventsOfTimeSlots(1560513600000,  1560517200000, "sharmilabegum97@gmail.com")
         //println(getDateForTimeSlot("11:00 AM", dateIterated))
         //println(getDateForTimeSlot("01:00 PM", dateIterated))
        //// println(list1)
        for( k in 0..list1.size-1){
             println("Events list: "+list1.get(k).eventId)
         }*/

        var i = 0

        val count = gridLayout?.childCount
        while (i < count!!) {
            var view = gridLayout?.getChildAt(i)

            if (view is LinearLayout) {
                //if (i % 2 != 0)  {
                if (view.childCount == 1) {
                    var textView = view.getChildAt(0)
                    if (textView is TextView) {
                        textView.setOnClickListener {
                            // changeInOtherviews(view,gridLayout)
                            var currentClickedView = textView
                            if (previousClickedView != null) {
                                previousClickedView!!.setBackgroundColor(Color.TRANSPARENT)
                                previousClickedView!!.setText(" ")
                                previousClickedView!!.setTextColor(Color.BLACK)
                                previousClickedView!!.setBackgroundResource(R.drawable.textview_border)

                            }

                            textView.setBackgroundResource(R.color.event_add)
                            textView.setText("+ New Event")
                          textView.setPadding(10,10,10,10)
                            textView.setTextColor(Color.WHITE)


                            val intent = Intent(context, AddEvent::class.java);
                            intent.putExtra("startDate", eventStartTime[0])
                            intent.putExtra("startTime", eventStartTime[1])
                            intent.putExtra("endDate", eventEndTime[0])
                            intent.putExtra("endTime", eventEndTime[1])
                            startActivity(intent);

                            previousClickedView = currentClickedView
                        }
                        //  }
                    }
                }
            }
            i++


        }
        var j = 0
        val count1 = gridLayout?.childCount
        var time1: String = ""
        var tv: TextView? = null
        var previousLinearLayout: LinearLayout? = null
        while (j < count1!!) {
            var view = gridLayout?.getChildAt(j)


            if (view is TextView) {
                time1 = view.text.toString().toUpperCase()
                time1 = time1.substring(0, 2) + ":00" + time1.substring(2, 5)
                tv = view
            }


            if (view is LinearLayout) {
                if (j >= 3) {
                    if (gridLayout?.getChildAt(j - 2) is LinearLayout) {
                        previousLinearLayout = gridLayout?.getChildAt(j - 2) as LinearLayout
                    }
                }

                if (timeSlotAndEventsHashMap.containsKey(time1.toLowerCase())) {
                    var eventList = timeSlotAndEventsHashMap.get(time1.toLowerCase())

                    if (idList.contains(view.getChildAt(0).id)) {
                        view.removeView(view.getChildAt(0))
                    }
                    createTextView1(eventList!!, view, previousLinearLayout, tv, gridLayout, i)

                }


            }
            j++
        }

        /* var ge = GetEvent(context1!!)
         var eventList = ge.getEvent(dateIterated, dsc, calendar, "sharmilabegum97@gmail.com")
         if (eventList.isEmpty() == false) {
             val names = arrayOfNulls<String>(eventList.size)

             for (i in 0..eventList.size - 1) {
                 names[i] = eventList[i].title
                 var iteration=1
                 if (format.format(dsc.getDateFromMillis(eventList[i].startDate)).equals(format.format(dateIterated)) && format.format(
                         dsc.getDateFromMillis(eventList[i].endDate)
                     ).equals(format.format(dateIterated))
                 ) {
                     var start = getPositions(format1.format(dsc.getDateFromMillis(eventList[i].startDate)), gridLayout)
                     var end = getPositions(format1.format(dsc.getDateFromMillis(eventList[i].endDate)), gridLayout)
                     var j = 0
                     val count = gridLayout?.childCount
                     var time1:String=""
                     while (j < count!!) {
                         var view = gridLayout?.getChildAt(j)

                         if(view is TextView){
                             time1=view.text.toString().toUpperCase()
                             time1=time1.substring(0,2)+":00"+time1.substring(2,5)
                         }


                         if ( view is LinearLayout) {

                                 if (time1.equals(format1.format(dsc.getDateFromMillis(eventList[i].startDate)))) {

                                     if(idList.contains(view.getChildAt(0).id)){
                                         view.removeView(view.getChildAt(0))
                                     }
                                    // createTextView(i, start, end, eventList, names,view)

                                 }


                         }
                         j++
                     }





                 }
             }


         }*/


        if (format.format(dayDate).equals(format.format(Date()))) {
            currentTimeLine.visibility = View.VISIBLE
            var i = 0

            val count = gridLayout?.childCount
            while (i < count!!) {
                var view = gridLayout?.getChildAt(i)

                if (view is TextView) {
                    if (i % 2 == 0) {

                        if ((view.text).equals(hourAlone.toLowerCase())) {
                            var paramsAllDay: ViewGroup.LayoutParams = view.getLayoutParams()
                            var positionstarts = (i / 2) * paramsAllDay.height
                            var oneMinute = paramsAllDay.height / 59
                            var top = 0
                            if (time.substring(5, 8).equals("AM") || time.substring(0, 2).equals("12")) {
                                top = positionstarts + oneMinute * Integer.parseInt(time.substring(3, 5))

                            } else {
                                top = positionstarts + oneMinute * (Integer.parseInt(time.substring(3, 5)) + 12)

                            }



                            currentTimeLine.animate().translationY(top.toFloat());
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


            activity!!.setDateCommon(Date())
            activity!!.getCurrentDayView()
        }
        return view
    }

    private fun getDateForTimeSlot(s: String, date: Date): Long {
        var cal: Calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
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

        var date1 = dsc.getDateInMillis1(cal.time)
        return date1

    }

    /* private fun createTextView(
         i: Int,
         start: Int,
         end: Int,
         eventList: ArrayList<Event>,
         names: Array<String?>,
         linearLayout: LinearLayout
     ) {
         val textView1 = TextView(context1)
         /* var layoutParams = LinearLayout.LayoutParams(
             5 * (screenWidthCommon / 6) - 10, end - start

         )*/
       var layoutParams=LinearLayout.LayoutParams(
           RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT,1.0f

         )
         //layoutParams.setMargins((screenWidthCommon / 6), start - 20, 4, 0)
         //layoutParams.setMargins( 0,start-20,4,0)
 textView1.setPadding(2,2,2,2)
      textView1.layoutParams = layoutParams
         textView1.text = names[i]
         textView1.visibility = View.VISIBLE
         textView1.setTextColor(Color.BLUE)
         textView1.setTextColor(ContextCompat.getColor(context!!, R.color.colorPrimary));
         textView1.setBackgroundResource(R.drawable.week_event_background) // hex color 0xAARRGGBB
         // in pixels (left, top, right, bottom)
        //day1.addView(textView1)
        // textViewLinearLayout.removeView(textViewLinearLayout.getChildAt(0))
       //textViewLinearLayout.addView(textView1)
        linearLayout.addView(textView1)

         val curTextViewId = prevTextViewId + 1
         textView1.setId(curTextViewId)

         /*val tv = TextView(context1)
         tv.layoutParams = lparams
         tv.text = "test"
         tv.setTextColor(Color.BLACK)
         tv.setId(R.id.layout1)*/
         /*if (day1 != null) {
             day1.addView(textView)
         }*/

         var firstEvent =linearLayout.findViewById<TextView>(curTextViewId)

        //var firstEvent = day1.findViewById<TextView>(curTextViewId)

         //firstEvent.animate().translationY(start.toFloat());
         firstEvent.setOnClickListener {
             val i: Intent =
                 Intent(context, ViewEvent(eventList.get(i), "sharmilabegum97@gmail.com")::class.java)
             startActivityForResult(i, 1);
         }
         prevTextViewId = curTextViewId

     }*/
    private fun createTextView1(
        eventList: ArrayList<Event>,
        linearLayout: LinearLayout,
        previousLinearLayout: LinearLayout?,
        tv: TextView?,
        gridlayout: GridLayout,
        i: Int
    ) {

        var previousListIds = ArrayList<String>()
        var currentListIds = ArrayList<String>()
        for (l in 0..previousList.size - 1) {
            previousListIds.add(previousList.get(l).eventId)
        }
        for (l in 0..eventList.size - 1) {
            currentListIds.add(eventList.get(l).eventId)
        }
        if (previousListIds.size != 0) {
            println("Previous list: " + previousListIds)
        }
        if (currentListIds.size != 0) {
            println("Current list: " + currentListIds)
        }
        previousListIds.retainAll(currentListIds);
        println("Common events: " + previousListIds)
        if (previousListIds.size != 0) {
            println("previousLinearLayout: " + previousLinearLayout)
            if(eventList.size> previousList.size){
            previousLinearLayout!!.weightSum = eventList.size.toFloat()}
            else {
                previousLinearLayout!!.weightSum = previousList.size.toFloat()
            }
        }
        linearLayout.weightSum = eventList.size.toFloat()
        println("linearLayout: " + linearLayout)
        for (i in 0..eventList.size - 1) {
            val textView1 = TextView(context1)
            /* var layoutParams = LinearLayout.LayoutParams(
                5 * (screenWidthCommon / 6) - 10, end - start

            )*/
            var layoutParams: LinearLayout.LayoutParams? = null
            var height = 0
            var date = dsc.getDateFromMillis(eventList.get(i).startDate)
            var date1 = dsc.getDateFromMillis(eventList.get(i).endDate)
            var calendar: Calendar = Calendar.getInstance()
            calendar.time = date

            var startHour = calendar.get(Calendar.HOUR)
            var calendar1: Calendar = Calendar.getInstance()
            calendar1.time = date1
            var endHour = calendar1.get(Calendar.HOUR)
            var times = endHour - startHour

            if (calendar.get(Calendar.MINUTE) == 0) {

                height = (screenHeightCommon / 15)
                layoutParams = LinearLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, height, 1.0f

                )
            } else {
                height = ((screenHeightCommon / 13) / 60) * calendar.get(Calendar.MINUTE)
                layoutParams = LinearLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, height, 1.0f

                )
                if (tv!!.text.substring(0, 2).trim().equals(startHour.toString())) {

                    println("Provide top margin")
                    layoutParams.setMargins(0, height, 0, 0)

                }
                /*else if(tv!!.text.substring(0,2).equals(endHour.toString())){
                    println("Provide bottom margin")
                    layoutParams.setMargins( 0,0,0,height)
                }*/
            }




            println("Minute : " + calendar.get(Calendar.MINUTE))

            println("Text view time: " + tv!!.text.substring(0, 2))
            println(startHour)
            println(endHour)

            //layoutParams.setMargins((screenWidthCommon / 6), start - 20, 4, 0)
            //layoutParams.setMargins( 0,(linearLayout.width/59-(linearLayout.width/59)*  calendar.get(Calendar.MINUTE)),0,0)
            textView1.setPadding(2, 2, 2, 2)
            textView1.layoutParams = layoutParams
            textView1.text = eventList.get(i).title


            textView1.visibility = View.VISIBLE
            textView1.setTextColor(Color.BLUE)
            textView1.setTextColor(ContextCompat.getColor(context!!, R.color.colorPrimary));
            textView1.setBackgroundResource(R.drawable.week_event_background) // hex color 0xAARRGGBB
            // in pixels (left, top, right, bottom)
            //day1.addView(textView1)
            // textViewLinearLayout.removeView(textViewLinearLayout.getChildAt(0))
            //textViewLinearLayout.addView(textView1)
            linearLayout.addView(textView1)

            val curTextViewId = prevTextViewId + 1
            textView1.setId(curTextViewId)

            /*val tv = TextView(context1)
            tv.layoutParams = lparams
            tv.text = "test"
            tv.setTextColor(Color.BLACK)
            tv.setId(R.id.layout1)*/
            /*if (day1 != null) {
                day1.addView(textView)
            }*/

            var firstEvent = linearLayout.findViewById<TextView>(curTextViewId)
            //var firstEvent = day1.findViewById<TextView>(curTextViewId)

            //firstEvent.animate().translationY(start.toFloat());
            firstEvent.setOnClickListener {
                val i: Intent =
                    Intent(context, ViewEvent(eventList.get(i), "sharmilabegum97@gmail.com")::class.java)
                startActivityForResult(i, 1);
            }
            prevTextViewId = curTextViewId
            /* if(times>1) {
                 var j=i
                 for( k in 0..times-1) {
                     j=j+2
                     createTextView11(
                         eventList.get(i), gridlayout, j
                     )
                 }
             }*/

        }
        previousList = eventList

    }

    /*private fun createTextView11(
        event: Event,
        gridlayout: GridLayout,
        i: Int
    ) {
        var layout = gridlayout.getChildAt(i)
        var tv=gridlayout.getChildAt(i-1)

        if (layout is LinearLayout && tv is TextView){
        val textView1 = TextView(context1)
        /* var layoutParams = LinearLayout.LayoutParams(
                5 * (screenWidthCommon / 6) - 10, end - start

            )*/
        var layoutParams: LinearLayout.LayoutParams? = null
        var height = 0
        var date = dsc.getDateFromMillis(event.startDate)
        var date1 = dsc.getDateFromMillis(event.endDate)
        var calendar: Calendar = Calendar.getInstance()
        calendar.time = date

        var startHour = calendar.get(Calendar.HOUR)
        var calendar1: Calendar = Calendar.getInstance()
        calendar1.time = date1
        var endHour = calendar1.get(Calendar.HOUR)

        if (calendar.get(Calendar.MINUTE) == 0) {

            height = (screenHeightCommon / 15)
            layoutParams = LinearLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, height, 1.0f

            )
        } else {
            height = ((screenHeightCommon / 13) / 60) * calendar.get(Calendar.MINUTE)
            layoutParams = LinearLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, height, 1.0f

            )
            if (tv.text.substring(0, 2).trim().equals(startHour.toString())) {

                println("Provide top margin")
                layoutParams.setMargins(0, height, 0, 0)

            }
            /*else if(tv!!.text.substring(0,2).equals(endHour.toString())){
                    println("Provide bottom margin")
                    layoutParams.setMargins( 0,0,0,height)
                }*/
        }




        println("Minute : " + calendar.get(Calendar.MINUTE))

        println("Text view time: " + tv.text.substring(0, 2))
        println(startHour)
        println(endHour)

        //layoutParams.setMargins((screenWidthCommon / 6), start - 20, 4, 0)
        //layoutParams.setMargins( 0,(linearLayout.width/59-(linearLayout.width/59)*  calendar.get(Calendar.MINUTE)),0,0)
        textView1.setPadding(2, 2, 2, 2)
        textView1.layoutParams = layoutParams
        textView1.text = event.title


        textView1.visibility = View.VISIBLE
        textView1.setTextColor(Color.BLUE)
        textView1.setTextColor(ContextCompat.getColor(context!!, R.color.colorPrimary));
        textView1.setBackgroundResource(R.drawable.week_event_background) // hex color 0xAARRGGBB
        // in pixels (left, top, right, bottom)
        //day1.addView(textView1)
        // textViewLinearLayout.removeView(textViewLinearLayout.getChildAt(0))
        //textViewLinearLayout.addView(textView1)
       layout.addView(textView1)

        val curTextViewId = prevTextViewId + 1
        textView1.setId(curTextViewId)

        /*val tv = TextView(context1)
            tv.layoutParams = lparams
            tv.text = "test"
            tv.setTextColor(Color.BLACK)
            tv.setId(R.id.layout1)*/
        /*if (day1 != null) {
                day1.addView(textView)
            }*/

        var firstEvent =layout.findViewById<TextView>(curTextViewId)
        //var firstEvent = day1.findViewById<TextView>(curTextViewId)

        //firstEvent.animate().translationY(start.toFloat());
        firstEvent.setOnClickListener {
            val i: Intent =
                Intent(context, ViewEvent(event, "sharmilabegum97@gmail.com")::class.java)
            startActivityForResult(i, 1);
        }
        prevTextViewId = curTextViewId

    }

    }*/


}

private fun getPositions(time: String, gridLayout: GridLayout): Int {
    var hourAlone = time.substring(0, 2) + time.substring(5, 8)
    var i = 0
    val count = gridLayout?.childCount
    while (i < count!!) {
        var view = gridLayout?.getChildAt(i)

        if (view is TextView) {
            if (i % 2 == 0) {
                if ((view.text).equals(hourAlone.toLowerCase())) {
                    var paramsAllDay: ViewGroup.LayoutParams = view.getLayoutParams()
                    var positionstarts = (i / 2) * paramsAllDay.height
                    //var positionEnds = ((i / 2) * paramsAllDay.height + paramsAllDay.height)
                    //val mp = MarginLayoutParams(currentTimeLine.layoutParams)

                    var oneMinute = paramsAllDay.height / 59
                    var top = 0
                    if (time.substring(5, 8).equals("AM") || time.substring(0, 2).equals("12")) {
                        top = positionstarts + oneMinute * Integer.parseInt(time.substring(3, 5)) + 2

                    } else {
                        top = positionstarts + oneMinute * (Integer.parseInt(time.substring(3, 5)) + 12) + 2

                    }
                    return top
                }

            }


        }
        i++


    }

    return 0
}

private fun clearForm(group: ViewGroup?) {
    var i = 0
    val count = group?.childCount
    while (i < count!!) {
        var view = group?.getChildAt(i)

        if (view is TextView) {
            // if (i % 2 == 0) {
            setDimensions(view, (screenWidthCommon / 6), (screenHeightCommon / 15).toInt())
        }
        // }
        else if (view is LinearLayout) {
            setDimensions(view, 5 * (screenWidthCommon / 6) - 10, (screenHeightCommon / 15).toInt())

        }
        i++

    }
}

private fun changeInOtherviews(view1: TextView, gridLayout: GridLayout) {
    var i = 0

    val count = gridLayout?.childCount
    while (i < count!!) {
        var view = gridLayout?.getChildAt(i)

        if (view is TextView) {
            if (i % 2 == 0) {
            } else {
                view.setBackgroundColor(Color.TRANSPARENT)
                view.setText(" ")
                view.setTextColor(Color.BLACK)
            }
        }
    }
}


private fun setDimensions(view: View, width: Int, height: Int) {
    val params = view.layoutParams
    params.width = width
    params.height = height
    view.layoutParams = params
}