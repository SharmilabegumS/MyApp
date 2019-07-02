package com.example.Calendar

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_calculate_date_result.*
import java.text.SimpleDateFormat
import java.util.*

class CalculateDateResult : AppCompatActivity() {


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculate_date_result)
        setSupportActionBar(toolbar1)
        val actionBar = supportActionBar
        actionBar!!.title = "Result"
        // Set action bar elevation
        actionBar.elevation = 4.0F
        actionBar.setDisplayHomeAsUpEnabled(true)
        toolbar1.setTitleTextColor(Color.WHITE);
        val startDateField = findViewById<TextView>(R.id.tv1)
        val endDateField: TextView = findViewById<TextView>(R.id.date)
        val endDayField = findViewById<TextView>(R.id.day)
        val view_month = findViewById<TextView>(R.id.view_month)
        val add_event = findViewById<TextView>(R.id.add_event)
        val numberOfDaysField = findViewById<TextView>(R.id.days_number)
        val intent = intent
        val startDate = intent.getStringExtra("startDate")
        val endDate = intent.getStringExtra("endDate")
        val endDay = intent.getStringExtra("endDay")
        val numberOfDays1 = intent.getStringExtra("numberOfDays")
        val forwardOrBackward = intent.getStringExtra("forwardOrBackward")
        startDateField.setText(startDate)
        endDateField.setText(endDate)
        endDayField.setText(endDay)
        numberOfDaysField.setText("$numberOfDays1 days $forwardOrBackward")

        add_event.setOnClickListener {
            val calendarObj = Calendar.getInstance()
            calendarObj.time = Date()
            val calobj: Calendar = Calendar.getInstance()

            val sdf = SimpleDateFormat("dd MMMM, yyyy")
            val dateEvent = sdf.parse(endDate)

            calobj.setTime(dateEvent)
            calobj.set(Calendar.HOUR_OF_DAY, calendarObj.get(Calendar.HOUR_OF_DAY))
            calobj.set(Calendar.MINUTE, calendarObj.get(Calendar.MINUTE))
            calobj.set(Calendar.SECOND, calendarObj.get(Calendar.SECOND))

            val dateIterated = calobj.time
            calobj.add(Calendar.HOUR, 1)
            val timeEnd = calobj.time
            val date = dsc.getDateToStringConversion(dateIterated)
            val date1 = dsc.getDateToStringConversion(timeEnd)
            val eventStartTime = getTimeInFormat(date, calobj)
            val eventEndTime = getTimeInFormat(date1, calobj)
            val intent = Intent(this, AddEvent::class.java);
            intent.putExtra("startDate", eventStartTime[0])
            intent.putExtra("startTime", eventStartTime[1])
            intent.putExtra("endDate", eventEndTime[0])
            intent.putExtra("endTime", eventEndTime[1])
            startActivity(intent);
            val resultIntent = Intent()
            val bundle = Bundle()
            bundle.putString("endDate",endDate)
            resultIntent.putExtra("bundle", bundle)
            setResult(111, resultIntent)
            finish()
        }
        view_month.setOnClickListener {
            val resultIntent = Intent()
            val bundle = Bundle()
            bundle.putString("endDate",endDate)
            resultIntent.putExtra("bundle", bundle)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()

        }
    }

    fun getTimeInFormat(date: String, cal: Calendar): ArrayList<String> {
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
}
