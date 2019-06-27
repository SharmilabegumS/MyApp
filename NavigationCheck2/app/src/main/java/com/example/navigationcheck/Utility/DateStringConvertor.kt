package com.example.navigationcheck.Utility

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*
import kotlin.math.min

class DateStringConvertor(private val cal: Calendar) {
    private var date: Date? = null

    fun getStringToDateConversion(dateInString: String): Date? {
        try {
            val sdf = SimpleDateFormat("yyyy/MM/dd hh:mm a")
            date = sdf.parse(dateInString)
        } catch (e: ParseException) {
            return null
        }

        return date
    }

    fun getStringToDateConversion1(dateInString: String): Date? {
        try {
            val sdf = SimpleDateFormat("yyyy/MM/dd")
            date = sdf.parse(dateInString)
        } catch (e: ParseException) {
            return null
        }

        return date
    }
    fun getDateToStringConversion(date: Date): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm a")
        val dateAsString = formatter.format(date)

        return dateAsString
    }



    fun getDateToStringConversion1(date: String): String {
        val date1: String
        val day = date.substring(0, 10)
        val year = date.substring(30, 34)
        val time = date.substring(11, 19)
        val fullTime = get24to12hrsConversion(time)
        date1 = "$day $year $fullTime"
        return date1
    }

    private fun get24to12hrsConversion(time: String): String {
        var fullTime: String? = null
        var hour = Integer.parseInt(time.substring(0, 2))
        if (hour > 12) {
            hour = hour - 12
            val hourPortion = Integer.toString(hour)
            val modifiedTime = hourPortion + time.substring(2, 5)
            fullTime = "$modifiedTime PM"
        } else {
            val modifiedTime = time.substring(0, 5)
            fullTime = "$modifiedTime AM"
        }
        return fullTime
    }

    fun getDateInMillis(dateInString: String): Long? {
        try {

            val formatter1 = SimpleDateFormat("dd/MM/yyyy hh:mm a")
            formatter1.timeZone = TimeZone.getTimeZone("GMT")
            date = formatter1.parse(dateInString)
            cal.time = date
            val time = cal.timeInMillis
            cal.timeInMillis = time
            cal.time
            return time
        } catch (e: ParseException) {
            return null

        }

    }

    fun getDateFromMillis(value: Long?): Date {
        var calendar1:  Calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"),
            Locale.getDefault());
        var cal:Calendar= Calendar.getInstance()
        var currentLocalTime:Date  = calendar1.getTime();
        var date: DateFormat = SimpleDateFormat("Z");
        var localTime:String = date.format(currentLocalTime);
        var finalTime: Long = 0

        val sign = localTime.substring(0, 1)
        val hour = Integer.parseInt(localTime.substring(1, 3))
        val minute = Integer.parseInt(localTime.substring(3, 5))
        val timeVal = (hour * 60 * 60 + minute * 60) * 1000

        if (sign == "+") {
            finalTime = value!! - timeVal
        } else if (sign == "-") {
            finalTime = value!! + timeVal
        }

        calendar1.setTimeInMillis(finalTime)
        return calendar1.time

    }

    fun getDateInMillis1(date1: Date?): Long {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
        calendar.setTime(date1)
        //Returns current time in millis
        val timeMilli = calendar.timeInMillis
        return timeMilli

    }

}
