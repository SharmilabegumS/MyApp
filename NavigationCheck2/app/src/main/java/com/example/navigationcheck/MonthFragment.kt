package com.example.navigationcheck


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.util.*

import android.widget.*
import java.text.SimpleDateFormat

import android.widget.FrameLayout.LayoutParams
import android.graphics.drawable.Drawable
import android.view.MotionEvent
import android.widget.TextView
import com.example.navigationcheck.Domain.GetEvent
import android.widget.ArrayAdapter
import com.example.navigationcheck.Entity.Event
import android.view.animation.TranslateAnimation
import android.text.method.TextKeyListener.clear
import kotlinx.android.synthetic.main.activity_add_event.*
import android.widget.AdapterView
import android.widget.AdapterView.OnItemLongClickListener
import androidx.viewpager.widget.ViewPager
import com.example.navigationcheck.adapters.MonthPagerAdapter
import kotlinx.android.synthetic.main.fragment_month.*
import kotlin.collections.ArrayList
import kotlinx.android.synthetic.main.fragment_month.pager as pager1


var date: Date =Date()
var calendar :Calendar=Calendar.getInstance()
var  month:Int =calendar.get(Calendar.MONTH)
var  year:Int =calendar.get(Calendar.YEAR)
var dateInteger= calendar.get(Calendar.DAY_OF_MONTH)
lateinit var gridLayout: GridLayout
lateinit var currentDateElement:TextView
lateinit var eventListView:ListView
lateinit var events_title:TextView
var eventlist= ArrayList<Event>()
var itemsAdapter:ArrayAdapter<String>?=null
 var containerToBeProcessed:ViewGroup?=null
var attempt=0
lateinit var pager:ViewPager

/**
 * A simple [Fragment] subclass.
 *
 */
class MonthFragment : androidx.fragment.app.Fragment() {

    var cal1:Calendar= Calendar.getInstance()
    val format = SimpleDateFormat("dd/MM/yyyy")
    var location = IntArray(2)
    var location1 = IntArray(2)
    var iteration=1
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

    @SuppressLint("NewApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
containerToBeProcessed=container
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
        optimizeSize(view.findViewById(R.id.gridlayout))

        // Inflate the layout for this fragment
        eventListView=view.findViewById(R.id.event_list)
        var bottom_date_holder:TextView=view.findViewById(R.id.bottom_date_holder)
        pager= view.findViewById(R.id.pager)

        /* if(attempt==0) {

             var calendarObj = Calendar.getInstance()
             var date111 = cal.time
             var currentMonth = MonthFragment.newInstance(date111)
             cal.add(Calendar.MONTH, 1)
             var date1111 = cal.time
             var nextMonth = MonthFragment.newInstance(date1111)
             cal.time = Date()
             cal.add(Calendar.MONTH, -1)
             var date11111 = cal.time
             var prevMonth = MonthFragment.newInstance(date11111)
             var pager = view.findViewById<ViewPager>(R.id.pager)
             var monthPagerAdapter = MonthPagerAdapter(context, childFragmentManager, 3)
             monthPagerAdapter.loadFragmentsToAdapter(prevMonth, currentMonth, nextMonth)

             pager.adapter = monthPagerAdapter
             pager.setCurrentItem(1)
             attempt=1
         }*/
        bottom_date_holder.text=dateInteger.toString()
        bottom_date_holder.setBackgroundResource(R.drawable.current_date_click)
        bottom_date_holder.performClick();
        bottom_date_holder.setPressed(true);
        bottom_date_holder.invalidate();
        bottom_date_holder.setPressed(false);
        bottom_date_holder.invalidate();
        setDimensions(bottom_date_holder,((screenWidthCommon-30)/7.5).toInt(),(screenWidthCommon-30)/7.5.toInt())
        var month_layout:GridLayout=view.findViewById(R.id.gridlayout)
       var month:TextView=view.findViewById(R.id.month_name)
        var layoutparams: LayoutParams = month.getLayoutParams() as (FrameLayout.LayoutParams)
        layoutparams.width=(screenWidthCommon)
        layoutparams.height = (screenHeightCommon/11.38).toInt()
       month.setLayoutParams(layoutparams);
        month.setText("$monthName  $year")
        clearForm(view.findViewById(R.id.gridlayout),spaces,noOfDays,lastDayofMonth)
        val container1 = view.findViewById(R.id.gridlayout) as GridLayout
        events_title=view.findViewById(R.id.events_title)

        var cal:Calendar= Calendar.getInstance()
        cal.setTime(Date())
        cal1.setTime(startDate)
        gridLayout=container1
        val childCount = container1.childCount


        for (i in 7 until childCount) {
            val element = container1.getChildAt(i) as TextView
            if(element.text.toString().equals("")==false) {
                var date: Int = Integer.valueOf(element.text.toString())
                cal1.set(Calendar.DAY_OF_MONTH, date)
                var dateIterated = cal1.time
                var ge=GetEvent(context!!)
                var eventList=ge.getEvent(dateIterated, dsc, calendar,"sharmilabegum97@gmail.com")
                if(eventList.isEmpty()==false){
                    element.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.bullet1)
                }


                if (format.format(dateIterated).equals(format.format(Date()))) {

                    element.setBackgroundResource(R.drawable.current_date_click)
                    currentDateElement=element
                    bottom_date_holder.visibility=View.INVISIBLE
                    location[0]= currentDateElement.getX().toInt()
                    location[1]= currentDateElement.getY().toInt()
                }

            }
            else{
                element.isEnabled=false
            }
            element.setOnClickListener{

                    if (it == currentDateElement) {
                        currentDateElement.setBackgroundResource(R.drawable.current_date_click)
                        bottom_date_holder.visibility = View.INVISIBLE
                        iteration = 1

                    }
                    // currentDateElement.animate().translationX(location[0]-location1[0].toFloat());
                    else { // currentDateElement.animate().translationY(400.toFloat());
                        /* val transAnimation = TranslateAnimation(location1[0].toFloat(),location[0].toFloat(), location1[1].toFloat(),location[1].toFloat())
currentDateElement.startAnimation(transAnimation)
    transAnimation.duration=10000*/
                        it.setBackgroundResource(R.drawable.current_date)
                        /*var rootLayout: GameRootLayout = view.findViewById(R.id.gameRootLayout) as (GameRootLayout)

if(iteration==1) {
    rootLayout.addFlyOver(currentDateElement, location1[0], location1[1])
    bottom_date_holder.text = currentDateElement.text
    bottom_date_holder.setBackgroundResource(R.drawable.current_date_click)
    bottom_date_holder.visibility = View.VISIBLE
    iteration=0
}*/


                    }
                    changeInOtherViews(it)
                    if (itemsAdapter != null) {
                        eventlist.removeAll(eventlist)
                        itemsAdapter!!.notifyDataSetChanged()

                    }
                    events(it)
                false

            }
            element.setOnLongClickListener {
                var calendarObj=Calendar.getInstance()
                calendarObj.setTime(startDate)
                calendarObj.set(Calendar.DAY_OF_MONTH, Integer.valueOf(element.text.toString()))
                var dateIterated = calendarObj.time
                calendarObj.add(Calendar.HOUR,1)
                var timeEnd=calendarObj.time
                var date=dsc.getDateToStringConversion(dateIterated)
                var date1=dsc.getDateToStringConversion(timeEnd)
                var time=getTimeInFormat(date,calendarObj)
                var endTime=getTimeInFormat(date1,calendarObj)
                val intent = Intent(context, add_event::class.java);
                intent.putExtra("startDate",time[0])
                intent.putExtra("startTime",time[1])
                intent.putExtra("endDate",endTime[0])
                intent.putExtra("endTime",endTime[1])
                startActivity(intent);
                true
            }
        }
        bottom_date_holder.setOnClickListener{
            location1[0]=it.getX().toInt()
            location1[1] = it.getY().toInt()
        }
        eventListView.setOnItemClickListener(object :AdapterView.OnItemClickListener {


            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                for(i in 0..eventlist.size-1){
                    if(eventlist.get(i).title.equals(parent!!.getItemAtPosition(position))){
                        val i: Intent = Intent(context, ViewEvent(eventlist.get(i)!!,"sharmilabegum97@gmail.com")::class.java)
                        startActivityForResult(i, 1);
                    }
                }
                  Toast.makeText(context,parent!!.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
            }
        })
        /*month_layout.setOnTouchListener(object:OnSwipeTouchListener(){
           override fun onSwipeLeft() {
               calendar.add(Calendar.MONTH, 1)
               var cal1:Calendar= Calendar.getInstance()
               cal1.setTime(Date())
               var cal:Calendar= Calendar.getInstance()
               cal.setTime(calendar.time)
               if(cal.get(Calendar.MONTH)!=cal1.get(Calendar.MONTH)){
                   var rootLayout: GameRootLayout = view.findViewById(R.id.gameRootLayout) as (GameRootLayout)

                   rootLayout.addFlyOver(currentDateElement, location1[0], location1[1])
                   bottom_date_holder.setText(currentDateElement.text)
                   bottom_date_holder.setBackgroundResource(R.drawable.current_date_click)
                   bottom_date_holder.visibility=View.VISIBLE
               }
               else{
                   iteration=1
                   bottom_date_holder.visibility=View.INVISIBLE
               }
               list.add(newInstance(calendar.time))
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
                   calendar.add(Calendar.MONTH, -1)
                   var cal1:Calendar= Calendar.getInstance()
                   cal1.setTime(Date())
                   var cal:Calendar= Calendar.getInstance()
                   cal.setTime(calendar.time)
                   if(cal.get(Calendar.MONTH)!=cal1.get(Calendar.MONTH)){
                       var rootLayout: GameRootLayout = view.findViewById(R.id.gameRootLayout) as (GameRootLayout)

                       rootLayout.addFlyOver(currentDateElement, location1[0], location1[1])

                       bottom_date_holder.setText(currentDateElement.text)
                       bottom_date_holder.setBackgroundResource(R.drawable.current_date_click)
                       bottom_date_holder.visibility=View.VISIBLE
                   }
                   else{
                       iteration=1
                       bottom_date_holder.visibility=View.INVISIBLE
                   }
                   list.add(newInstance(calendar.time))
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


        })*/

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
                view.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.bullet)
                    view.setText(date.toString())
                    date++

            }
            if (view is ViewGroup && view.childCount > 0)
                clearForm(view,spaces,noOfDays,lastDayofMonth)

        }


    }
    fun optimizeSize(group: ViewGroup?){
        var item=0

        val count = group?.childCount
        while(item<count!!){

            var view = group?.getChildAt(item)
            setDimensions(view,((screenWidthCommon-30)/7.5).toInt(),(screenWidthCommon-30)/7.5.toInt())
            item++
        }
    }
    fun changeInOtherViews(view:View){
        val childCount = gridLayout.childCount
        for (i in 7 until childCount) {
            val element = gridLayout.getChildAt(i) as TextView
            if (element != view) {
                element.setBackgroundResource(R.drawable.bg_transparent)
            }
                if (element.text.toString().equals("") == false) {
                    var date: Int = Integer.valueOf(element.text.toString())
                    cal1.set(Calendar.DAY_OF_MONTH, date)
                    var dateIterated = cal1.time
                    if (format.format(dateIterated).equals(format.format(Date()))) {
                        element.setTextColor(Color.BLUE)
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
    private fun events(view:View){

        if((view as TextView).text.toString().equals("")==false) {
            var date: Int = Integer.valueOf(view.text.toString())
            cal1.set(Calendar.DAY_OF_MONTH, date)
            var dateIterated = cal1.time
            var ge=GetEvent(context!!)
            var eventList=ge.getEvent(dateIterated, dsc, calendar,"sharmilabegum97@gmail.com")
            if(eventList.isEmpty()==false){
                eventlist=eventList
                val names= arrayOfNulls<String>(eventList.size)
                for(i in 0..eventList.size-1){
                    names[i]=eventList[i].title
                }

                events_title.visibility=View.VISIBLE
                eventListView.visibility=View.VISIBLE
           itemsAdapter= ArrayAdapter(context!!, android.R.layout.simple_list_item_1, names!! as Array<String>)
                eventListView.adapter=itemsAdapter
            }
            else{
                events_title.visibility=View.INVISIBLE
                eventListView.visibility=View.INVISIBLE
                var values = ArrayList<String>()
                values.clear()
                val adapter  = ArrayAdapter<String>(context, R.layout.item_layout, android.R.id.text1, values)
                eventListView.adapter=adapter
            }

        }
    }
    override fun onResume() {
        super.onResume()

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
                    element.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.bullet1)
                }
            }

        }
        events_title.visibility=View.INVISIBLE
        eventListView.visibility=View.INVISIBLE

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {


        if(requestCode==1) {

            if (resultCode == 1) {

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
                            element.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.bullet1)
                        }
                        else{
                            element.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)

                        }

                    }


                }
                events_title.visibility=View.INVISIBLE
                eventListView.visibility=View.INVISIBLE
            }
        }
    }
fun getTimeInFormat(date:String,cal: Calendar):ArrayList<String>{
   var time=ArrayList<String>()
    if(date.substring(14,16).toInt()>=0 && date.substring(14,16).toInt()<30){
        time.add(date.substring(0,10))
        time.add(date.substring(11,14)+"30 "+date.substring(17,19))
    }
    else if(date.substring(14,16).toInt()>=30 && date.substring(14,16).toInt()<=59){
        var dateAlone=date.substring(0,10)
        var amOrPm=date.substring(17,19)
        var timeInt=Integer.valueOf(date.substring(11,13))+1
        if((Integer.valueOf(date.substring(11,13))+1)>12){
            cal1.setTime(dsc.getStringToDateConversion(date))
            cal1.add(Calendar.HOUR,1)
            var dateMorrow=dsc.getDateToStringConversion(cal1.time)
            timeInt=Integer.valueOf(dateMorrow.substring(11,13))
            amOrPm=dateMorrow.substring(17,19)
            dateAlone=dateMorrow.substring(0,10)

        }
        time.add(dateAlone)
        time.add(String.format("%02d", timeInt) +":00 "+amOrPm)
    }

    return time
}

}

