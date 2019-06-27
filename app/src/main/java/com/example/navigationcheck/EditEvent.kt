package com.example.navigationcheck

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration

import android.graphics.BitmapFactory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.util.DisplayMetrics
import android.view.*
import kotlinx.android.synthetic.main.activity_add_event.*
import java.text.SimpleDateFormat
import java.util.*
import android.widget.*
import android.widget.TextView
import com.example.navigationcheck.dataBase.DataBaseManager
import com.example.navigationcheck.entity.Event
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

import android.widget.ArrayAdapter

import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.example.navigationcheck.entity.Contacts
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.ParseException
import kotlin.collections.ArrayList

@SuppressLint("NewApi")
class EditEvent : AppCompatActivity(), View.OnClickListener {
    lateinit var event: Event
    lateinit var user_name: TextView
    lateinit var title_input: EditText
    lateinit var description: EditText
    var calendar = Calendar.getInstance()
    var bottomSheetDialog: BottomSheetDialog? = null
    lateinit var e11Layout: TextInputLayout
    lateinit var e12Layout: TextInputLayout
    lateinit var d11Layout: TextInputLayout
    lateinit var d12Layout: TextInputLayout
    lateinit var e11: TextInputEditText
    lateinit var e12: TextInputEditText
    lateinit var d11: TextInputEditText
    lateinit var d12: TextInputEditText
    lateinit var guest_names: ChipGroup
    lateinit var guestLayout: LinearLayout
    var guestList = ArrayList<String>()
    lateinit var reminderType: TextView
    var reminderTypeAndKey = HashMap<Int, String>()

    var time: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_event_modified)
        user_name = findViewById(R.id.user_name)
        title_input = findViewById(R.id.title_input)
        description = findViewById(R.id.description)
        val context: Context? = applicationContext
        screenWidth = getScreenWidthInDPs(context!!)
        screenHeight = getScreenHeightInDPs(context)
        setSupportActionBar(toolbar1)

        // Now get the support action bar
        val actionBar = supportActionBar
        // Set toolbar title/app title

        actionBar!!.title = "Event"

        // Set action bar/toolbar sub title
        reminderTypeAndKey.put(-1,"Don't remind")
        reminderTypeAndKey.put(0, "Before the event")
        reminderTypeAndKey.put(1, "5 min before")
        reminderTypeAndKey.put(2, "10 min before")
        reminderTypeAndKey.put(3, "15 min before")
        reminderTypeAndKey.put(4, "30 min before")
        reminderTypeAndKey.put(5, "1 hour before")
        reminderTypeAndKey.put(6, "1 day before")

        // Set action bar elevation
        actionBar.elevation = 4.0F
        actionBar.setDisplayHomeAsUpEnabled(true)
        guestLayout = findViewById(R.id.guestslayout)
        var paramsGuestLayout: RelativeLayout.LayoutParams =
            guestLayout.getLayoutParams() as RelativeLayout.LayoutParams

        paramsGuestLayout.setMargins(0, 2, 0, 0);
        guestLayout.setLayoutParams(paramsGuestLayout);
        var start_date: TextView = findViewById(R.id.start_date)
        var end_date: TextView = findViewById(R.id.end_date)
        var end_time: TextView = findViewById(R.id.end_time)
        var start_time: TextView = findViewById(R.id.start_time)
        guest_names = findViewById<ChipGroup>(R.id.guest_names)
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

            var paramsStartDate: ViewGroup.LayoutParams = start_date.getLayoutParams()
            paramsStartDate.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            paramsStartDate.width = (screenWidth - 120) / 2
            start_date.setLayoutParams(paramsStartDate)


            var paramsEndDate: ViewGroup.LayoutParams = end_date.getLayoutParams()
            paramsEndDate.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            paramsEndDate.width = (screenWidth - 120) / 2
            end_date.setLayoutParams(paramsEndDate);


            var paramsStartTime: ViewGroup.LayoutParams = start_time.getLayoutParams()
            paramsStartTime.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            paramsStartTime.width = (screenWidth - 120) / 2
            start_time.setLayoutParams(paramsStartTime);


            var paramsEndTime: ViewGroup.LayoutParams = end_time.getLayoutParams()
            paramsEndTime.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            paramsEndTime.width = (screenWidth - 120) / 2
            end_time.setLayoutParams(paramsEndTime);

        }
        reminderType = findViewById<TextView>(R.id.choose_reminder_type)
        reminderType.setOnClickListener {
            var intent = Intent(this, EventNotification::class.java)
            startActivityForResult(intent, 999);
        }

        createBottomSheetDialog()
        var e1: TextInputEditText = findViewById(R.id.start_time)
        var e2: TextInputEditText = findViewById(R.id.end_time)
        var d1: TextInputEditText = findViewById(R.id.start_date)
        var d2: TextInputEditText = findViewById(R.id.end_date)
        e11Layout = findViewById(R.id.start_time_field)
        e12Layout = findViewById(R.id.end_time_field)
        d11Layout = findViewById(R.id.start_date_layout)
        d12Layout = findViewById(R.id.end_date_layout)
        e11 = e1
        e12 = e2
        d11 = d1
        d12 = d2
        e1!!.setShowSoftInputOnFocus(false)
        e2!!.setShowSoftInputOnFocus(false)
        d1!!.setShowSoftInputOnFocus(false)
        d2!!.setShowSoftInputOnFocus(false)
        e1!!.setOnClickListener {
            clickTimePicker(e1)
        }
        e2!!.setOnClickListener {
            clickTimePicker(e2)
        }

        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(
                view: DatePicker, year: Int, monthOfYear: Int,
                dayOfMonth: Int
            ) {
                mDay = dayOfMonth
                mMonth = monthOfYear
                mYear = year
                updateDateInView(d1)
                mDay = dayOfMonth
                mMonth = monthOfYear
                mYear = year
                updateDateInView(d1)
                var result = checkDateAndTime(d1, d2, e1, e2)
                if (result == false) {

                    e11Layout.setError(" ")
                    d11Layout.setError(" ")
                    //d1.setTextAppearance(getApplicationContext(), R.style.MyTextInputLayout);
                    //var colorStateList:      ColorStateList = ColorStateList.valueOf(Color.RED)
                    // d1.supportBackgroundTintList=colorStateList
                    // e1.setError("Start time is after event end time!")
                    // e1.supportBackgroundTintList=colorStateList
                } else if (result == true) {
                    d11Layout.error = null
                    e11Layout.error = null
                }

            }
        }
        val dateSetListener1 = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(
                view: DatePicker, year: Int, monthOfYear: Int,
                dayOfMonth: Int
            ) {
                mDay = dayOfMonth
                mMonth = monthOfYear
                mYear = year
                updateDateInView(d2)
                var result = checkDateAndTime(d1, d2, e1, e2)
                if (result == false) {
                    e11Layout.setError(" ")
                    d11Layout.setError(" ")

                    // d1.setError("Start date is after event end date!")
                    // var colorStateList:      ColorStateList = ColorStateList.valueOf(Color.RED)
                    // d1.supportBackgroundTintList=colorStateList
                    //e1.setError("Start time is after event end time!")
                    // e1.supportBackgroundTintList=colorStateList
                } else if (result == true) {
                    d11Layout.error = null
                    e11Layout.error = null
                }

            }
        }

        d1!!.setOnClickListener(
            {

                DatePickerDialog(
                    this@EditEvent, R.style.MyDialogTheme,
                    dateSetListener,
                    mYear, mMonth, mDay
                ).show()


            })
        d2!!.setOnClickListener(
            {

                DatePickerDialog(
                    this@EditEvent, R.style.MyDialogTheme,
                    dateSetListener1,
                    mYear, mMonth, mDay
                ).show()


            })

        val sw = findViewById<Switch>(R.id.switch1)
        sw?.setOnCheckedChangeListener({ _, isChecked ->
            if (isChecked) {
                e1.setText("12:01 AM")
                e2.setText("11:59 PM")
                e1.isEnabled = false
                e2.isEnabled = false

            } else {
                e1.setText("")
                e2.setText("")
                e1.isEnabled = true
                e2.isEnabled = true
            }
        })
        var allday = findViewById<TextView>(R.id.all_day)
        allday.setOnClickListener {
            var status = sw.isChecked()
            sw.isChecked = !status
        }
        guestLayout.setOnClickListener {
            showDialog1()
        }
        guests.setOnClickListener {
            showDialog1()
        }
        guest_names.setOnClickListener {
            showDialog1()
        }
        var title: EditText = findViewById(R.id.title_input)
        title.setText(eventID.title)
        var description: EditText = findViewById(R.id.description)
        description.setText(eventID.description)
        var user: TextView = findViewById(R.id.user_name)
        user.setText("sharmilabegum97@gmail.com")
        var sd = dsc.getDateToStringConversion(dsc.getDateFromMillis(eventID.startDate))
        var ed = dsc.getDateToStringConversion(dsc.getDateFromMillis(eventID.endDate))
        sd.substring(0, 10)
        sd.substring(11, 19)
        ed.substring(0, 10)
        ed.substring(11, 19)
        d1.setText(sd.substring(0, 10))
        d2.setText(ed.substring(0, 10))
        e1.setText(sd.substring(11, 19))
        e2.setText(ed.substring(11, 19))
        if (sd.substring(11, 19).equals("12:01 AM") && ed.substring(11, 19).equals("11:59 PM")) {
            sw.setChecked(true)
        }
        reminderType.setText(reminderTypeAndKey.get(eventID.eventReminderType))
        var guests: TextView = findViewById(R.id.guests)
        guests.setText("")
        println("size: " + eventID.guests.size)
        for (i in 0..eventID.guests.size - 1) {
            var contact = dataBaseManager.getSingleContact(eventID.guests.get(i).trim())
            if (contact != null) {
                onItemSelected(contact!!)
            } else {
                guests.setText("Add Guests")
            }

        }
    }


    override fun onSupportNavigateUp(): Boolean {
        if (title_input.text.toString().equals("") == false || description.text.toString().equals("") == false) {
            AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setTitle("Cancel")
                .setMessage("Discard your changes?")
                .setPositiveButton("Yes") { dialog, which -> finish() }
                .setNegativeButton("No", null)
                .show()
        } else {
            onBackPressed()
        }
        return true

    }

    override fun onBackPressed() {
        if (title_input.text.toString().equals("") == false || description.text.toString().equals("") == false) {
            AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setTitle("Cancel")
                .setMessage("Discard your changes?")
                .setPositiveButton("Yes") { dialog, which -> finish() }
                .setNegativeButton("No", null)
                .show()
        } else {
            super.onBackPressed()
        }
    }

    private fun updateDateInView(editText: TextInputEditText) {
        val myFormat = "dd/MM/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.DAY_OF_MONTH, mDay)
        calendar.set(Calendar.MONTH, mMonth)
        editText!!.setText(sdf.format(calendar.getTime()))
    }

    fun clickTimePicker(editText: TextView) {
        val c = Calendar.getInstance()
        var hour = c.get(Calendar.HOUR)
        var minute = c.get(Calendar.MINUTE)
        lateinit var amOrPm: String

        val tpd = TimePickerDialog(
            this, R.style.MyDialogTheme1, TimePickerDialog.OnTimeSetListener(function = { view, h, m ->
                if (h < 12) {
                    amOrPm = "AM";
                } else {
                    amOrPm = "PM";
                }
                hour = h
                minute = m
                var timeSet = ""
                if (hour > 12) {
                    hour -= 12
                    timeSet = "PM"
                } else if (hour === 0) {
                    hour += 12
                    timeSet = "AM"
                } else if (hour === 12) {
                    timeSet = "PM"
                } else {
                    timeSet = "AM"
                }
                var hours = ""
                if (hour < 10)
                    hours = "0$hour"
                else
                    hours = hour.toString()
                var min = ""
                if (minute < 10)
                    min = "0$minute"
                else
                    min = minute.toString()

                val aTime = StringBuilder().append(hours).append(':')
                    .append(min).append(" ").append(timeSet).toString()
                editText.setText(aTime)
                time = String.format("%02d", h) + " : " + String.format("%02d", m) + " " + amOrPm
                var result = checkDateAndTime(d11, d12, e11, e12)
                if (result == false) {
                    e11Layout.setError(" ")
                    d11Layout.setError(" ")

                    //d11.setError("Start date is after event end date!")
                    //e11.setError("Start time is after event end time!")
                } else if (result == true) {
                    d11Layout.error = null
                    e11Layout.error = null
                }

            }), hour, minute, false
        )
        tpd.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    var dataBaseManager = DataBaseManager(this)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {

            R.id.done -> {
                if (d11Layout.error == null && e11Layout.error == null) {


                    var eventId = eventID.eventId
                    var title = title_input.text.toString()
                    var userId = user_name.text
                    var startDate = dsc.getDateInMillis("${start_date.text} ${start_time.text}")
                    var endDate = dsc.getDateInMillis("${end_date.text} ${end_time.text}")
                    var description = description.text.toString()
                    var reminderType1:Int=0
                    for ((key, value) in reminderTypeAndKey) {
                        if(value.equals(reminderType.text)){
                            reminderType1=key
                        }
                    }

                    if (title.equals("")) {
                        title = "No title"
                    }
                    if (description.equals("")) {
                        description = "No description"
                    }
                    var event = Event(eventId, title, startDate, endDate, guestList, description, reminderType1)

                    dataBaseManager.update(event, "sharmilabegum97@gmail.com")
                    monthPagerAdapter.notifyDataSetChanged()
                    dayPagerAdapter.notifyDataSetChanged()
                    weekPagerAdapter.notifyDataSetChanged()
                    val resultIntent = Intent()
                    var bundle = Bundle()
                    bundle.putString("result", "true")
                    bundle.putString("eventId", eventId)
                    resultIntent.putExtra("bundle", bundle)
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                } else {
                    Toast.makeText(this, "Start date is previous to end date", Toast.LENGTH_SHORT).show()
                }
                return true
            }
            else ->
                return super.onOptionsItemSelected(item)
        }

    }

    private fun createBottomSheetDialog() {

        if (bottomSheetDialog == null) {
            val view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet, null)
            var listView = view.findViewById<ListView>(R.id.listView)
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1, resources.getStringArray(R.array.UserList)
            )
            listView.setAdapter(adapter)
            bottomSheetDialog = BottomSheetDialog(this)
            bottomSheetDialog!!.setContentView(view)
            listView.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->

                val itemValue = listView.getItemAtPosition(position) as String
                user_name.setText(itemValue)
                bottomSheetDialog!!.dismiss()


            })
        }
    }


    private fun setDimensions(view: View, width: Int, height: Int) {
        val params = view.layoutParams
        params.width = width
        params.height = height
        view.layoutParams = params
    }

    fun showDialog(view: View) {
        bottomSheetDialog!!.show()
    }

    fun showDialog1() {
        var b: Bundle = Bundle();
        b.putStringArrayList("nameList", guestList);
        println("GuestList: " + guestList)
        var intent = Intent(this, InviteGuests::class.java)
        intent.putExtras(b);
        startActivityForResult(intent, 1011);

    }


    fun getScreenWidthInDPs(context: Context): Int {
        var dm = DisplayMetrics();
        var windowManager: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager;
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels
    }

    fun getScreenHeightInDPs(context: Context): Int {
        var dm = DisplayMetrics()
        var windowManager: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels
    }

    override fun onClick(v: View?) {
        var chip = v as Chip
        guest_names.removeView(chip)
        guestList.remove(chip.text)
        if (guestList.isEmpty() == true) {
            guests.text = "Add Guests"
            var params = guests.layoutParams
            params.width = 500
            params.height = 150
            guests.layoutParams = params
        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {


        if (requestCode == 1011) {

            if (resultCode == 1) {
                var dbm = DataBaseManager(this)
                var bundle = data!!.getBundleExtra("bundle");
                guestList.removeAll(guestList)
                guest_names.removeAllViews()
                var stockkList: LongArray = bundle!!.getLongArray("arrayList");
                println(stockkList)
                var contactSelected = ArrayList<Contacts?>()
                for (i in 0..stockkList.size - 1) {
                    contactSelected.add(dbm.getSingleContact(stockkList.get(i)))
                }
                if (stockkList.size == 0) {

                    var params = guests.layoutParams
                    params.width = 500
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    guests.layoutParams = params
                    guests.text = "Add Guests"
                    guestLayout.visibility = View.INVISIBLE
                }
                // guest_names.removeAllViews()
                if (stockkList.size != 0) {
                    guests.text = ""
                    var params = guests.layoutParams
                    params.width = 1
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    guests.layoutParams = params
                    guestLayout.visibility = View.VISIBLE
                }
                for (i in 0..stockkList.size - 1) {
                    /*guests.text = ""
                    var params = guests.layoutParams
                    params.width = 1
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    guests.layoutParams = params
                    guestLayout.visibility = View.VISIBLE*/
                    onItemSelected(contactSelected.get(i)!!)
                }

            }
        } else if (requestCode == 999) {

            if (resultCode == Activity.RESULT_OK) {
                val bundle = intent.extras

//Extract the data…
                //val choosenReminderType = bundle!!.getInt("Position")
                var item = reminderTypeAndKey.get(position1)
                reminderType.setText(item.toString())


            }
        }
    }

    fun onItemSelected(contact: Contacts) {


        setTheme(R.style.Theme_MaterialComponents_Light_DarkActionBar)
        var chip = Chip(this@EditEvent)
        chip.setText(contact.name)
        val options = BitmapFactory.Options()


        var bitmap =
            BitmapFactory.decodeByteArray(contact.picId, 0, contact.picId.size, options) //Convert bytearray to bitmap
        var d: RoundedBitmapDrawable =
            RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        d.setCircular(true);
        chip.setChipIcon(d)
        chip.setCloseIconVisible(true)
        chip.setCheckable(false)
        chip.setClickable(false)
        chip.setOnCloseIconClickListener(this@EditEvent)

        if (guestList.isEmpty() == false) {
            if (guestList.contains(contact.name) != true) {
                guest_names.addView(chip)
                guestList.add(contact.name)
            }
        } else {
            guestList.add(contact.name)

            guest_names.addView(chip)
        }
        guest_names.visibility = View.VISIBLE

    }

    private fun checkDateAndTime(
        d1: TextInputEditText,
        d2: TextInputEditText,
        e1: TextInputEditText,
        e2: TextInputEditText
    ): Boolean {

        var status = false
        try {
            println("HI")
            val formatter1 = SimpleDateFormat("dd/MM/yyyy hh:mm a")
            var date = formatter1.parse(d1.text.toString() + " " + e1.text.toString())
            var date1 = formatter1.parse(d2.text.toString() + " " + e2.text.toString())
            println("Date: " + date)
            println("Date1: " + date1)
            if (date1.after(date) || date1.equals(date)) {
                status = true
            } else
                status = false
            return status

        } catch (e: ParseException) {
            return false

        }

    }
}

