package com.example.navigationcheck

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_add_event.*
import java.text.SimpleDateFormat
import java.util.*

class CalculateDateResult : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculate_date_result)
        setSupportActionBar(toolbar1)

        // Now get the support action bar
        val actionBar = supportActionBar
        // Set toolbar title/app title

        actionBar!!.title = "Result"

        // Set action bar/toolbar sub title


        // Set action bar elevation
        actionBar.elevation = 4.0F
        actionBar.setDisplayHomeAsUpEnabled(true)
        toolbar1.setTitleTextColor(Color.WHITE);
        var startDateField = findViewById<TextView>(R.id.tv1)
        var endDateField: TextView = findViewById<TextView>(R.id.date)
        var endDayField = findViewById<TextView>(R.id.day)
        var view_month = findViewById<TextView>(R.id.view_month)
        var add_event = findViewById<TextView>(R.id.add_event)
        var numberOfDaysField = findViewById<TextView>(R.id.days_number)

        val intent = intent

        var startDate = intent.getStringExtra("startDate")
        var endDate = intent.getStringExtra("endDate")
        var endDay = intent.getStringExtra("endDay")
        var numberOfDays1 = intent.getStringExtra("numberOfDays")
        var forwardOrBackward = intent.getStringExtra("forwardOrBackward")
        startDateField.setText(startDate)
        endDateField.setText(endDate)
        endDayField.setText(endDay)
        numberOfDaysField.setText("$numberOfDays1 days $forwardOrBackward")

        add_event.setOnClickListener {
            var calendarObj = Calendar.getInstance()
            calendarObj.time = Date()
            println("second: " + calendarObj.get(Calendar.SECOND))
            println("Hour: " + calendarObj.get(Calendar.HOUR_OF_DAY))
            println("Minute: " + calendarObj.get(Calendar.MINUTE))

            println("Calendar obj time: " + calendarObj.time)
            var calobj: Calendar = Calendar.getInstance()

            val sdf = SimpleDateFormat("dd MMMM, yyyy")
            var dateEvent = sdf.parse(endDate)

            calobj.setTime(dateEvent)
            calobj.set(Calendar.HOUR_OF_DAY, calendarObj.get(Calendar.HOUR_OF_DAY))
            calobj.set(Calendar.MINUTE, calendarObj.get(Calendar.MINUTE))
            calobj.set(Calendar.SECOND, calendarObj.get(Calendar.SECOND))

            var dateIterated = calobj.time
            calobj.add(Calendar.HOUR, 1)
            var timeEnd = calobj.time
            var date = dsc.getDateToStringConversion(dateIterated)
            var date1 = dsc.getDateToStringConversion(timeEnd)
            var eventStartTime = getTimeInFormat(date, calobj)
            var eventEndTime = getTimeInFormat(date1, calobj)
            val intent = Intent(this, AddEvent::class.java);
            intent.putExtra("startDate", eventStartTime[0])
            intent.putExtra("startTime", eventStartTime[1])
            intent.putExtra("endDate", eventEndTime[0])
            intent.putExtra("endTime", eventEndTime[1])
            startActivity(intent);
            finish()
        }
        view_month.setOnClickListener {
            val resultIntent = Intent()
            var bundle = Bundle()
            bundle.putString("endDate",endDate)
            resultIntent.putExtra("bundle", bundle)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()

        }
    }

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
