package com.example.Calendar

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.view.inputmethod.InputMethodManager
import java.text.SimpleDateFormat
import java.util.*
import android.widget.*
import android.widget.TextView
import com.example.Calendar.dataBase.DataBaseManager
import com.example.Calendar.domain.AddEvent
import com.example.Calendar.entity.Event
import com.example.Calendar.utility.UUIDGenerator
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import android.widget.Toast
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.view.GestureDetectorCompat
import com.example.Calendar.entity.Contacts
import com.example.Calendar.utility.DateStringConvertor
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_add_event.*
import java.text.ParseException
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


var screenWidth = 0
var screenHeight = 0
var cal = Calendar.getInstance()
var uuid = UUIDGenerator
var c = Calendar.getInstance()
var mYear = c.get(Calendar.YEAR)
var mMonth = c.get(Calendar.MONTH)
var mDay = c.get(Calendar.DAY_OF_MONTH)
var dsc = DateStringConvertor(cal)


@SuppressLint("NewApi")
class AddEvent : AppCompatActivity(), View.OnClickListener {
    var keyBoardState: Boolean = false
    private var gestureDetectorCompat: GestureDetectorCompat? = null

    var calendar = Calendar.getInstance()
    lateinit var e11: TextInputEditText
    lateinit var e12: TextInputEditText
    lateinit var d11: TextInputEditText
    lateinit var d12: TextInputEditText
    lateinit var e11Layout: TextInputLayout
    lateinit var e12Layout: TextInputLayout
    lateinit var d11Layout: TextInputLayout
    lateinit var d12Layout: TextInputLayout
    lateinit var user_name: TextView
    lateinit var title_input: EditText
    lateinit var description: EditText
    lateinit var guest_names: ChipGroup
    lateinit var reminderType: TextView

    lateinit var guestLayout: LinearLayout
    lateinit var guestsFieldLayout: RelativeLayout
    var guestList = ArrayList<String>()
    var reminderTypeAndKey = HashMap<Int, String>()

    var time: String = ""
    @SuppressLint("LocalSuppress")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)
        val context: Context? = applicationContext
        screenWidth = getScreenWidthInDPs(context!!)
        screenHeight = getScreenHeightInDPs(context)
        setSupportActionBar(toolbar1)

        val actionBar = supportActionBar

        reminderTypeAndKey.put(-1, "Don't remind")
        reminderTypeAndKey.put(0, "Before the event")
        reminderTypeAndKey.put(1, "5 min before")
        reminderTypeAndKey.put(2, "10 min before")
        reminderTypeAndKey.put(3, "15 min before")
        reminderTypeAndKey.put(4, "30 min before")
        reminderTypeAndKey.put(5, "1 hour before")
        reminderTypeAndKey.put(6, "1 day before")

        actionBar!!.title = "Event"
        setupUI(findViewById(R.id.add_event_layout))
        val layout = findViewById<RelativeLayout>(R.id.add_event_layout)
        layout.getViewTreeObserver().addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val r = Rect()
                layout.getWindowVisibleDisplayFrame(r)
                val screenHeight = layout.getRootView().getHeight()
                val keypadHeight = screenHeight - r.bottom
                if (keypadHeight > screenHeight * 0.15) {
                    keyBoardState = true
                } else {
                    keyBoardState = false
                }
            }
        })

        actionBar.elevation = 4.0F
        actionBar.setDisplayHomeAsUpEnabled(true)

        val navDefaultTextColor = Color.parseColor("#006cff")
        getWindow().setStatusBarColor(navDefaultTextColor)
        toolbar1.setTitleTextColor(Color.WHITE)
        user_name = findViewById(R.id.user_name)
        title_input = findViewById(R.id.title_input)
        description = findViewById(R.id.description)
        guestLayout = findViewById(R.id.guestslayout)
        guestsFieldLayout = findViewById(R.id.guests_field_layout)
        val paramsGuestLayout: RelativeLayout.LayoutParams =
            guestLayout.layoutParams as RelativeLayout.LayoutParams

        paramsGuestLayout.setMargins(0, 2, 0, 0)
        guestLayout.layoutParams = paramsGuestLayout
        reminderType = findViewById<TextView>(R.id.choose_reminder_type)
        reminderType.setOnClickListener {
            val intent = Intent(this, EventNotification::class.java)
            if (reminderType.text.equals("Don't remind")) {
                intent.putExtra("Switch status", true)
                intent.putExtra("Listview status", false)
                intent.putExtra("Position", -1)
            } else {

                var reminderType1: Int = 0
                for ((key, value) in reminderTypeAndKey) {
                    if (value.equals(reminderType.text)) {
                        reminderType1 = key
                    }
                }
                intent.putExtra("Switch status", false)
                intent.putExtra("Listview status", true)
                intent.putExtra("Position", reminderType1)
            }
            startActivityForResult(intent, 999)
        }
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            val start_date: TextView = findViewById(R.id.start_date)
            val paramsStartDate: ViewGroup.LayoutParams = start_date.layoutParams
            paramsStartDate.height = ViewGroup.LayoutParams.WRAP_CONTENT
            paramsStartDate.width = (screenWidth / 2.5).toInt()
            start_date.layoutParams = paramsStartDate

            val end_date: TextView = findViewById(R.id.end_date)
            val paramsEndDate: ViewGroup.LayoutParams = end_date.layoutParams
            paramsEndDate.height = ViewGroup.LayoutParams.WRAP_CONTENT
            paramsEndDate.width = (screenWidth / 2.5).toInt()
            end_date.layoutParams = paramsEndDate

            val start_time: TextView = findViewById(R.id.start_time)
            val paramsStartTime: ViewGroup.LayoutParams = start_time.layoutParams
            paramsStartTime.height = ViewGroup.LayoutParams.WRAP_CONTENT
            paramsStartTime.width = (screenWidth / 2.5).toInt()
            start_time.layoutParams = paramsStartTime


            val end_time: TextView = findViewById(R.id.end_time)
            val paramsEndTime: ViewGroup.LayoutParams = end_time.layoutParams
            paramsEndTime.height = ViewGroup.LayoutParams.WRAP_CONTENT
            paramsEndTime.width = (screenWidth / 2.5).toInt()
            end_time.layoutParams = paramsEndTime
        }
        guest_names = findViewById(R.id.guest_names)
        val e1: TextInputEditText = findViewById(R.id.start_time)
        val e2: TextInputEditText = findViewById(R.id.end_time)
        val d1: TextInputEditText = findViewById(R.id.start_date)
        val d2: TextInputEditText = findViewById(R.id.end_date)
        e11Layout = findViewById(R.id.start_time_field)
        e12Layout = findViewById(R.id.end_time_field)
        d11Layout = findViewById(R.id.start_date_layout)
        d12Layout = findViewById(R.id.end_date_layout)
        e11 = e1
        e12 = e2
        d11 = d1
        d12 = d2
        e1.showSoftInputOnFocus = false
        e2.showSoftInputOnFocus = false
        d1.showSoftInputOnFocus = false
        d2.showSoftInputOnFocus = false
        e1.setOnClickListener {
            clickTimePicker(e1)

        }
        e2.setOnClickListener {

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
                val result = checkDateAndTime(d1, d2, e1, e2)
                if (result == false) {

                    e11Layout.error = " "
                    d11Layout.error = " "
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
                val result = checkDateAndTime(d1, d2, e1, e2)
                if (result == false) {
                    e11Layout.error = " "
                    d11Layout.error = " "

                } else if (result == true) {
                    d11Layout.error = null
                    e11Layout.error = null
                }


            }
        }



        d1.setOnClickListener(
            {

                DatePickerDialog(
                    this@AddEvent, R.style.MyDialogTheme,
                    dateSetListener,
                    mYear, mMonth, mDay
                ).show()

            })
        d2.setOnClickListener(
            {

                DatePickerDialog(
                    this@AddEvent, R.style.MyDialogTheme,
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
        val allday = findViewById<TextView>(R.id.all_day)
        allday.setOnClickListener {
            sw.isChecked = !sw.isChecked
        }
        guestsFieldLayout.setOnClickListener {
            showDialog1()
        }
        val intent = intent
        val startDate = intent.getStringExtra("startDate")
        val startTime = intent.getStringExtra("startTime")
        val endDate = intent.getStringExtra("endDate")
        val endTime = intent.getStringExtra("endTime")

        d1.setText(startDate)
        e1.setText(startTime)
        d2.setText(endDate)
        e2.setText(endTime)
        user_name.setText(userName)
    }

    private fun checkDateAndTime(
        d1: TextInputEditText,
        d2: TextInputEditText,
        e1: TextInputEditText,
        e2: TextInputEditText
    ): Boolean {

        val status: Boolean?
        try {
            val formatter1 = SimpleDateFormat("dd/MM/yyyy hh:mm a")
            val date = formatter1.parse(d1.text.toString() + " " + e1.text.toString())
            val date1 = formatter1.parse(d2.text.toString() + " " + e2.text.toString())
            status = date1.after(date) || date1.equals(date)
            return status

        } catch (e: ParseException) {
            return false

        }

    }

    override fun onSupportNavigateUp(): Boolean {
        if (title_input.text.toString().equals("") == false || description.text.toString().equals("") == false) {
            AlertDialog.Builder(this)
                .setMessage(getString(R.string.WarningMessage))
                .setPositiveButton(getString(R.string.Discard)) { _, _ -> finish() }
                .setNegativeButton(getString(R.string.KeepEditing), null)
                .show()
        } else {
            onBackPressed()
        }
        return true

    }

    override fun onBackPressed() {
        if (title_input.text.toString().equals("") == false || description.text.toString().equals("") == false) {
            AlertDialog.Builder(this)
                .setMessage(getString(R.string.WarningMessage))
                .setPositiveButton(getString(R.string.Discard)) { _, _ -> finish() }
                .setNegativeButton(getString(R.string.KeepEditing), null)
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
        editText.setText(sdf.format(calendar.time))

    }

    fun clickTimePicker(editText: TextView) {
        val c = Calendar.getInstance()
        var hour = c.get(Calendar.HOUR)
        var minute = c.get(Calendar.MINUTE)
        lateinit var amOrPm: String

        val tpd = TimePickerDialog(
            this, R.style.MyDialogTheme1, TimePickerDialog.OnTimeSetListener(function = { view1, h, m ->
                if (h < 12) {
                    amOrPm = "am"
                } else {
                    amOrPm = "pm"
                }
                hour = h
                minute = m
                val timeSet: String
                if (hour > 12) {
                    hour -= 12
                    timeSet = "pm"
                } else if (hour == 0) {
                    hour += 12
                    timeSet = "am"
                } else if (hour == 12) {
                    timeSet = "pm"
                } else {
                    timeSet = "am"
                }
                val hours: String
                if (hour < 10)
                    hours = "0$hour"
                else
                    hours = hour.toString()
                val min: String
                if (minute < 10)
                    min = "0$minute"
                else
                    min = minute.toString()

                val aTime = StringBuilder().append(hours).append(':')
                    .append(min).append(" ").append(timeSet).toString()
                editText.text = aTime
                time = String.format("%02d", h) + " : " + String.format("%02d", m) + " " + amOrPm
                val result = checkDateAndTime(d11, d12, e11, e12)
                if (result == false) {
                    e11Layout.error = " "
                    d11Layout.error = " "
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


                    val eventId = uuid.generateUUID()
                    var title = title_input.text.toString()
                    val userId = user_name.text
                    val startDate = dsc.getDateInMillis("${start_date.text} ${start_time.text}")
                    val endDate = dsc.getDateInMillis("${end_date.text} ${end_time.text}")
                    var description = description.text.toString()

                    var reminderType1: Int = 0
                    for ((key, value) in reminderTypeAndKey) {
                        if (value.equals(reminderType.text)) {
                            reminderType1 = key
                        }
                    }
                    if (title.equals("")) {
                        title = "No title"
                    }
                    if (description.equals("")) {
                        description = "No description"
                    }
                    val event =
                        Event(eventId, title, startDate, endDate, guestList, description.toString(), reminderType1)

                    val addEvent = AddEvent(event, userId.toString())
                    val result: Boolean = addEvent.add(dataBaseManager)

                    if (result == true) {
                        Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(this, "not saved", Toast.LENGTH_SHORT).show()
                    }
                    monthPagerAdapter.notifyDataSetChanged()
                    dayPagerAdapter.notifyDataSetChanged()
                    weekPagerAdapter.notifyDataSetChanged()
                    val resultIntent = Intent()
                    val bundle = Bundle()
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


    var bottomSheetFragment: BottomSheetFragment? = null
    fun showDialog(view: View) {
        bottomSheetFragment = BottomSheetFragment.newInstance()
        bottomSheetFragment!!.show(
            supportFragmentManager,
            "user_bottom_sheet_fragment"
        )

    }

    fun closeBottomSheet() {
        bottomSheetFragment?.dismiss()
    }

    fun showDialog1() {
        val b = Bundle()
        b.putStringArrayList("nameList", guestList)
        val intent = Intent(this, InviteGuests::class.java)
        intent.putExtras(b)
        startActivityForResult(intent, 1011)

    }


    fun getScreenWidthInDPs(context: Context): Int {
        val dm = DisplayMetrics()
        val windowManager: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(dm)
        return dm.widthPixels
    }

    fun getScreenHeightInDPs(context: Context): Int {
        val dm = DisplayMetrics()
        val windowManager: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(dm)
        return dm.heightPixels
    }

    override fun onClick(v: View?) {
        val chip = v as Chip
        guest_names.removeView(chip)
        this.guestList.remove(chip.text)
        if (this.guestList.isEmpty()) {
            guests.text = "Add Guests"
            val params = guests.layoutParams
            params.width = 500
            params.height = 150
            guests.layoutParams = params


        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1011) {
            if (resultCode == 1) {
                val dbm = DataBaseManager(this)
                val bundle = data!!.getBundleExtra("bundle")
                guestList.removeAll(guestList)
                guest_names.removeAllViews()
                val stockkList: LongArray? = bundle!!.getLongArray("arrayList")
                val contactSelected = ArrayList<Contacts?>()
                for (i in 0..stockkList!!.size - 1) {
                    contactSelected.add(dbm.getSingleContact(stockkList.get(i)))
                }
                if (stockkList.size == 0) {

                    val params = guests.layoutParams
                    params.width = 500
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    guests.layoutParams = params
                    guests.text = "Add Guests"
                    guestLayout.visibility = View.INVISIBLE
                }
                if (stockkList.isNotEmpty()) {
                    guests.text = ""
                    val params = guests.layoutParams
                    params.width = 1
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    guests.layoutParams = params
                    guestLayout.visibility = View.VISIBLE
                }
                for (i in 0..stockkList.size - 1) {
                    onItemSelected(contactSelected.get(i)!!)
                }

            }
        } else if (requestCode == 999) {

            if (resultCode == Activity.RESULT_OK) {
                val bundle = intent.extras

//Extract the data…
                val choosenReminderType = bundle!!.getInt("Position")
                val item = reminderTypeAndKey.get(position1)
                reminderType.text = item.toString()


            }
        }
    }


    fun onItemSelected(contact: Contacts) {
        setTheme(R.style.Theme_MaterialComponents_Light_DarkActionBar)
        val chip = Chip(this@AddEvent)
        chip.text = contact.name
        val options = BitmapFactory.Options()
        val bitmap =
            BitmapFactory.decodeByteArray(contact.picId, 0, contact.picId.size, options) //Convert bytearray to bitmap
        Bitmap.createScaledBitmap(bitmap, 60, 40, true)
        val d: RoundedBitmapDrawable =
            RoundedBitmapDrawableFactory.create(resources, bitmap)
        d.isCircular = true
        chip.chipIcon = d
        chip.isCloseIconVisible = true
        chip.isCheckable = false
        chip.isClickable = false
        chip.setOnCloseIconClickListener(this@AddEvent)

        if (!guestList.isEmpty()) {
            if (!guestList.contains(contact.name)) {
                guest_names.addView(chip)
                guestList.add(contact.name)
            }

        } else {
            guestList.add(contact.name)

            guest_names.addView(chip)
        }

        guest_names.visibility = View.VISIBLE
    }
    private fun hideSoftKeyboard(activity: Activity) {
        if (keyBoardState == true) {
            val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetectorCompat!!.onTouchEvent(event)
        return true
    }

    fun setupUI(view: View) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view is EditText)) {
            view.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View, event: MotionEvent): Boolean {
                    hideSoftKeyboard(this@AddEvent)
                    return false
                }
            })
        }
        //If a layout container, iterate over children and seed recursion.
        if (view is ViewGroup) {
            for (i in 0..(view as ViewGroup).getChildCount() - 1) {
                val innerView = (view as ViewGroup).getChildAt(i)
                setupUI(innerView)
            }
        }
    }

}

