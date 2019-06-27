package com.example.navigationcheck

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.*
import com.example.navigationcheck.entity.Event
import kotlinx.android.synthetic.main.activity_view_event.*
import android.content.Intent
import android.graphics.Color
import android.view.MenuItem
import android.view.View
import com.example.navigationcheck.dataBase.DataBaseManager
import com.example.navigationcheck.entity.Contacts
import com.example.navigationcheck.adapter.CustomAdapter2
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat


lateinit var imageView: ImageView
lateinit var titleEvent: TextView
lateinit var duration: TextView
lateinit var userTitle: TextView
lateinit var userMail: TextView
lateinit var descriptionEvent: TextView
lateinit var guestList: ExpandableListView
lateinit var eventID: Event
lateinit var titleList: ArrayList<String>
lateinit var id:String

var contactList = ArrayList<Contacts>()

class ViewEvent() : AppCompatActivity() {

    internal var adapter: ExpandableListAdapter? = null
    var userId:String?=null

    val data: HashMap<String, List<Contacts>>
        get() {
            val listData = HashMap<String, List<Contacts>>()
            contactList.removeAll(contactList)
            for (i in 0..eventID!!.guests.size - 1) {
                var contact = dbm.getSingleContact(eventID.guests.get(i).trim())
                if(contact!=null){
                    contactList.add(contact!!)
                }
            }
            listData["Guests (${contactList.size})"] = contactList
            return listData
        }



    override fun onCreate(savedInstanceState: Bundle?) {
        dbm = DataBaseManager(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_event_modified)
        val intent = intent
        id=intent.getStringExtra("ID")
        println("ID is :"+id)
        eventID=dbm.getEvent(id)!!
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
                startActivityForResult(intent,1000);
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000) {
            var dbm=DataBaseManager(this)
            if (resultCode == Activity.RESULT_OK) {
                var bundle = data!!.getBundleExtra("bundle")

                var result = bundle.getString("result")
                var eventId=bundle.getString("eventId")
                var event=dbm.getEvent(eventId)
                if(result.equals("true")){
                    val mySnackbar = Snackbar.make(findViewById(R.id.framelayout),
                        "Event added successfully", Snackbar.LENGTH_LONG)
                    mySnackbar.setAction("View", View.OnClickListener {
                        val i= Intent(this, ViewEvent::class.java)
                        i.putExtra("ID", event!!.eventId)
                        startActivity(i)
                    })
                    val navDefaultTextColor = Color.parseColor("#006cff")
                    mySnackbar.setActionTextColor(navDefaultTextColor)
                    // mySnackbar.getView().setBackgroundColor(Color.WHITE)
                    mySnackbar.show()
                }
                //monthPagerAdapter = MonthPageAdapter(supportFragmentManager, this, monthList111)

                // onNavigationItemSelected(nav_view.getMenu().getItem(0));

            }
        }
    }

    fun fillFields(eventID: Event) {
        titleEvent.setText(eventID!!.title)
        userMail.setText("sharmilabegum97@gmail.com")
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
            if (data.isEmpty() == false){
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
            else{
             guestList.visibility=View.INVISIBLE
            }
        }

    }

    override fun onNewIntent(intent: Intent?) {


        if (intent != null) {
           id= intent.extras!!.getString("ID")
        }
}


}

