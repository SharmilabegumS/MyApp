package com.example.Calendar

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.*
import com.example.Calendar.entity.Event
import android.content.Intent
import android.view.MenuItem
import android.view.View
import com.example.Calendar.dataBase.DataBaseManager
import com.example.Calendar.entity.Contacts
import com.example.Calendar.adapter.CustomAdapter2
import kotlinx.android.synthetic.main.activity_calculate_date.toolbar1


var contactList = ArrayList<Contacts>()
lateinit var eventID: Event
class ViewEvent : AppCompatActivity() {

    private lateinit var titleEvent: TextView
    private lateinit var duration: TextView
    private lateinit var userMail: TextView
    private lateinit var descriptionEvent: TextView
    private lateinit var guestList: ExpandableListView

    private lateinit var titleList: ArrayList<String>
    private lateinit var id: String
    private lateinit var notification1: TextView
    private var dbm = DataBaseManager(this)
    private var reminderTypeAndKey = HashMap<Int, String>()
    internal var adapter: ExpandableListAdapter? = null

    val data: HashMap<String, List<Contacts>>
        get() {
            val listData = HashMap<String, List<Contacts>>()
            contactList.removeAll(contactList)
            for (i in 0..eventID.guests.size - 1) {
                var contact = dbm.getSingleContact(eventID.guests.get(i).trim())
                if (contact != null) {
                    contactList.add(contact)
                }
            }
            listData["Guests (${contactList.size})"] = contactList
            return listData
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        dbm = DataBaseManager(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_event)
        val intent = intent
        id = intent.getStringExtra("ID")
        eventID = dbm.getEvent(id)!!
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
        notification1 = findViewById(R.id.notification)
        reminderTypeAndKey.put(-1, "Don't remind")
        reminderTypeAndKey.put(0, "Before the event")
        reminderTypeAndKey.put(1, "5 min before")
        reminderTypeAndKey.put(2, "10 min before")
        reminderTypeAndKey.put(3, "15 min before")
        reminderTypeAndKey.put(4, "30 min before")
        reminderTypeAndKey.put(5, "1 hour before")
        reminderTypeAndKey.put(6, "1 day before")
        fillFields(eventID)
        var titleList = ArrayList<String>()
        titleList.add("Guests")


    }

    override fun onSupportNavigateUp(): Boolean {
        val resultIntent = Intent()
        setResult(1, resultIntent)
        finish()
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
                val intent = Intent(this@ViewEvent, EditEvent::class.java)
                startActivityForResult(intent, 1000)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                val bundle = data!!.getBundleExtra("bundle")
                val eventId = bundle.getString("eventId")
                val resultIntent = Intent()
                val bundle1 = Bundle()
                bundle1.putString("result", "true")
                bundle1.putString("eventId", eventId)
                resultIntent.putExtra("bundle", bundle1)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }
    }

    fun fillFields(eventID: Event) {
        titleEvent.text = eventID.title
        userMail.text = userName
        val sd = dsc.getDateToStringConversion(dsc.getDateFromMillis(eventID.startDate))
        val ed = dsc.getDateToStringConversion(dsc.getDateFromMillis(eventID.endDate))
        if (sd.substring(0, 10).equals(ed.substring(0, 10))) {
            duration.text = sd.substring(0, 10) + " " + sd.substring(11, 19) + " to " + ed.substring(11, 19)
        } else {
            duration.text =
                "${dsc.getDateToStringConversion(dsc.getDateFromMillis(eventID.startDate))} to ${dsc.getDateToStringConversion(
                    dsc.getDateFromMillis(eventID.endDate)
                )}"
        }
        notification1.text = reminderTypeAndKey.get(eventID.eventReminderType)
        descriptionEvent.text = eventID.description
        if (data.isEmpty() == false) {
            val listData = data
            titleList = ArrayList(listData.keys)
            adapter = CustomAdapter2(this, contactList, titleList, listData)
            guestList.setAdapter(adapter)
        } else {
            guestList.visibility = View.INVISIBLE
        }

    }

    override fun onNewIntent(intent: Intent?) {
        if (intent != null) {
            id = intent.extras!!.getString("ID")!!
        }
    }


}

