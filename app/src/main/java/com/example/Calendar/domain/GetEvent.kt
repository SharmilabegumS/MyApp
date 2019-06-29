package com.example.Calendar.domain

import java.util.ArrayList
import java.util.Calendar
import java.util.Date
import android.content.Context
import com.example.Calendar.dataBase.DataBaseManager
import com.example.Calendar.entity.Event
import com.example.Calendar.utility.DateStringConvertor

class GetEvent(var context: Context) {
    var eventStatus = false

    fun getEvent(date: Date, dsc: DateStringConvertor, calendar: Calendar, userId: String): ArrayList<Event> {
        val dbm = DataBaseManager(context)
        calendar.time = date
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.HOUR, 0)
        val date1 = calendar.time
        // println(date1.toString())
        //println(dsc.getDateToStringConversion(date1.toString()))
        val begin = dsc.getDateInMillis1(date1)
        //println(begin)
        val eventList = dbm.getEvents(begin, userId)
        if (eventList == null || eventList!!.isEmpty() == true) {
            eventStatus = true

        } else {
            eventStatus = false

        }
        return eventList
    }

}
