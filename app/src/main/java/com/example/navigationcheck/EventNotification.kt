package com.example.navigationcheck

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_event_notification.*
import kotlinx.android.synthetic.main.add_contact.*
import android.widget.Toast
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener

var position1 = -1

class EventNotification : AppCompatActivity() {
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_notification)
        var toolbar1 = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar1)
        setSupportActionBar(toolbar1)

        val actionBar = supportActionBar

//actionBar!!.setBackgroundDrawable(ColorDrawable(Color.RED));

        listView.isEnabled = false
        position1 = -1
        actionBar!!.title = "Reminders"
        toolbar1.setTitleTextColor(Color.WHITE);
        //actionBar.setHomeAsUpIndicator(R.drawable.ic_close)
        actionBar.elevation = 4.0F
        actionBar.setDisplayHomeAsUpEnabled(true)
        var sw = findViewById<Switch>(R.id.switch1)
        sw?.setOnCheckedChangeListener({ _, isChecked ->
            if (isChecked) {

                listView.isEnabled = false
                position1 = -1
                finish()

            } else {

                listView.isEnabled = true
            }
        })
        var listView = findViewById<ListView>(R.id.listView)
        var values = ArrayList<String>()
        values.add("Before the event")
        values.add("5 min before")
        values.add("10 min before")
        values.add("15 min before")
        values.add("30 min before")
        values.add("1 hour before")
        values.add("1 day before")

        val adapter = ArrayAdapter<String>(this, R.layout.reminder_list, R.id.profile_name, values)
        adapter.notifyDataSetChanged()
        listView.adapter = adapter

        listView.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                var resultIntent = Intent()

                val bundle = Bundle()

                bundle.putInt("Position", position)

                resultIntent.putExtras(bundle)

                position1 = position

                setResult(Activity.RESULT_OK, resultIntent)
                println("Position: " + position)
                finish()

            }
        })


    }
}
