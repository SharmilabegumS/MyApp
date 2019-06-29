package com.example.Calendar

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener

var position1 = -1

class EventNotification : AppCompatActivity() {
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_notification)
        var toolbar1 = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar1)
        setSupportActionBar(toolbar1)

        val actionBar = supportActionBar
        var sw = findViewById<Switch>(R.id.switch1)
        var listView = findViewById<ListView>(R.id.listView)
//actionBar!!.setBackgroundDrawable(ColorDrawable(Color.RED));
        val intent = intent
        var status = intent.extras
        listView.isEnabled = status.getBoolean("Listview status")
        sw.isChecked = status.getBoolean("Switch status")
        var position = status.getInt("Position")
        if (sw.isChecked == true) {
            position1 = -1
            listView.setAlpha(0.5F);
        }
        actionBar!!.title = "Reminders"
        toolbar1.setTitleTextColor(Color.WHITE);
        //actionBar.setHomeAsUpIndicator(R.drawable.ic_close)
        actionBar.elevation = 4.0F
        actionBar.setDisplayHomeAsUpEnabled(true)

        sw?.setOnCheckedChangeListener({ _, isChecked ->
            if (isChecked) {

                listView.isEnabled = false
                position1 = -1
                listView.setAlpha(0.5F);

            } else {

                listView.isEnabled = true
                listView.setAlpha(1F);
            }
        })

        var values = ArrayList<String>()
        values.add("Before the event")
        values.add("5 min before")
        values.add("10 min before")
        values.add("15 min before")
        values.add("30 min before")
        values.add("1 hour before")
        values.add("1 day before")

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, values)
        adapter.notifyDataSetChanged()
        listView.adapter = adapter
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setItemChecked(position, true);
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

            }
        })


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {

            R.id.done -> {
                finish()
            }
            else ->
                return super.onOptionsItemSelected(item)
        }
        return false
    }
}
