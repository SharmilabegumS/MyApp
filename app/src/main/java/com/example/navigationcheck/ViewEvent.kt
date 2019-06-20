package com.example.navigationcheck

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.*
import com.example.navigationcheck.entity.Event
import kotlinx.android.synthetic.main.activity_view_event.*
import android.content.Intent
import android.view.MenuItem
import com.example.navigationcheck.dataBase.DataBaseManager
import com.example.navigationcheck.entity.Contacts
import com.example.navigationcheck.adapter.CustomAdapter2


lateinit var imageView: ImageView
lateinit var titleEvent: TextView
lateinit var duration: TextView
lateinit var userTitle: TextView
lateinit var userMail: TextView
lateinit var descriptionEvent: TextView
lateinit var guestList: ExpandableListView
lateinit var eventID: Event
lateinit var titleList: ArrayList<String>
lateinit var dbm: DataBaseManager
var contactList = ArrayList<Contacts>()

class ViewEvent(var userId: String = "sharmilabegum97@gmail.com") : AppCompatActivity() {
    internal var adapter: ExpandableListAdapter? = null

    val data: HashMap<String, List<Contacts>>
        get() {
            val listData = HashMap<String, List<Contacts>>()
            contactList.removeAll(contactList)
            for (i in 0..eventID!!.guests.size - 1) {
                var contact = dbm.getSingleContact(eventID.guests.get(i).trim())
                contactList.add(contact!!)
            }
            listData["Guests"] = contactList
            return listData
        }

    constructor (eventId: Event, userId: String) : this(userId) {
        eventID = eventId
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        dbm = DataBaseManager(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_event_modified)
        setSupportActionBar(toolbar1)
        val actionBar = supportActionBar
        actionBar!!.title = "Event"
        actionBar.elevation = 4.0F
        actionBar.setDisplayHomeAsUpEnabled(true)
        titleEvent = findViewById(R.id.event_title)
        duration = findViewById(R.id.event_duration)
        userMail = findViewById(R.id.user_mail)
        descriptionEvent = findViewById(R.id.content_description)
        guestList = findViewById(R.id.guests_list)
        fillFields(eventID)
        var titleList = ArrayList<String>()
        titleList.add("Guests")


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.view_event_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {

            R.id.action_edit -> {

                val intent = Intent(this@ViewEvent, EditEvent::class.java);
                startActivity(intent);
                val resultIntent = Intent()
                setResult(1, resultIntent)
                finish()


                return true
            }
            R.id.action_delete -> {
                dbm.deleteEvent(eventID.eventId)
                val resultIntent = Intent()
                setResult(1, resultIntent)
                monthPagerAdapter.notifyDataSetChanged()
                dayPagerAdapter.notifyDataSetChanged()
                weekPagerAdapter.notifyDataSetChanged()
                finish()


                return true
            }
            else ->
                return super.onOptionsItemSelected(item)
        }

    }


    fun fillFields(eventID: Event) {
        titleEvent.setText(eventID!!.title)
        userMail.setText(userId)
        var sd = dsc.getDateToStringConversion(dsc.getDateFromMillis(eventID!!.startDate))
        var ed = dsc.getDateToStringConversion(dsc.getDateFromMillis(eventID!!.endDate))
        if (sd.substring(0, 10).equals(ed.substring(0, 10))) {
            duration.setText(sd.substring(0, 10) + " " + sd.substring(11, 19) + " to " + ed.substring(11, 19))
        } else {
            duration.setText(
                "${dsc.getDateToStringConversion(dsc.getDateFromMillis(eventID!!.startDate))} to ${dsc.getDateToStringConversion(
                    dsc.getDateFromMillis(eventID!!.endDate)
                )}"
            )
        }
        descriptionEvent.setText(eventID!!.description)
        if (guestList != null) {
            val listData = data
            titleList = ArrayList(listData.keys)
            adapter = CustomAdapter2(this, contactList, titleList, listData)
            println("Contact list: " + contactList)
            println("list data: " + listData)
            guestList!!.setAdapter(adapter)

            /*guestList!!.setOnGroupExpandListener { groupPosition -> Toast.makeText(applicationContext, (titleList as ArrayList<String>)[groupPosition] + " List Expanded.", Toast.LENGTH_SHORT).show() }

           guestList!!.setOnGroupCollapseListener { groupPosition -> Toast.makeText(applicationContext, (titleList as ArrayList<String>)[groupPosition] + " List Collapsed.", Toast.LENGTH_SHORT).show() }

          guestList!!.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
                Toast.makeText(applicationContext, "Clicked: " + (titleList as ArrayList<String>)[groupPosition] + " -> " + listData[(titleList as ArrayList<String>)[groupPosition]]!!.get(childPosition), Toast.LENGTH_SHORT).show()
                false
            }*/
        }

    }


}

