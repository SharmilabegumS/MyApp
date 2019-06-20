package com.example.navigationcheck

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.util.DisplayMetrics
import android.view.*
import android.view.inputmethod.InputMethodManager
import java.text.SimpleDateFormat
import java.util.*
import android.widget.*
import android.widget.TextView
import com.example.navigationcheck.dataBase.DataBaseManager
import com.example.navigationcheck.domain.AddEvent
import com.example.navigationcheck.entity.Event
import com.example.navigationcheck.utility.UUIDGenerator
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import android.widget.Toast
import android.widget.ArrayAdapter
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.view.GestureDetectorCompat
import com.example.navigationcheck.entity.Contacts
import com.example.navigationcheck.utility.DateStringConvertor
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_add_event.end_date
import kotlinx.android.synthetic.main.activity_add_event.end_time
import kotlinx.android.synthetic.main.activity_add_event.guests
import kotlinx.android.synthetic.main.activity_add_event.start_date
import kotlinx.android.synthetic.main.activity_add_event.start_time
import kotlinx.android.synthetic.main.activity_add_event.toolbar1
import kotlinx.android.synthetic.main.activity_main.*
import java.text.ParseException
import kotlin.collections.ArrayList


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

    private var gestureDetectorCompat: GestureDetectorCompat? = null

    var calendar = Calendar.getInstance()
    var textview_start_date: TextInputEditText? = null
    var textview_end_date: TextInputEditText? = null
    var textview_start_time: TextInputEditText? = null
    var textview_end_time: TextInputEditText? = null
    var bottomSheetDialog: BottomSheetDialog? = null
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

    lateinit var guestLayout: LinearLayout
    lateinit var guestsFieldLayout:RelativeLayout
    var guestList = ArrayList<String>()

    var time: String = ""
    @SuppressLint("LocalSuppress")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_event_modified)
        val context: Context? = applicationContext
        screenWidth = getScreenWidthInDPs(context!!)
        screenHeight = getScreenHeightInDPs(context)
        setSupportActionBar(toolbar1)

        // Now get the support action bar
        val actionBar = supportActionBar
        // Set toolbar title/app title

        actionBar!!.title = "Event"

        // Set action bar/toolbar sub title


        // Set action bar elevation
        actionBar.elevation = 4.0F
        actionBar.setDisplayHomeAsUpEnabled(true)
        toolbar1.setTitleTextColor(Color.WHITE);
        user_name = findViewById(R.id.user_name)
        title_input = findViewById(R.id.title_input)
        description = findViewById(R.id.description)
        guestLayout = findViewById(R.id.guestslayout)
        guestsFieldLayout = findViewById(R.id.guests_field_layout)
        var paramsGuestLayout: RelativeLayout.LayoutParams =
            guestLayout.getLayoutParams() as RelativeLayout.LayoutParams

        paramsGuestLayout.setMargins(0, 2, 0, 0);
        guestLayout.setLayoutParams(paramsGuestLayout);

      var status=false
        var status1=false
title_input.setOnClickListener{
    if(status==true){
        showKeyBoard()
        status=false
    }
    else {
        hideKeyBoard()
        status=true
    }
}
        description.setOnClickListener{
            if(status==true){
                showKeyBoard()
                status=false
            }
            else {

                hideKeyBoard()
                status=true
            }
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            /* var title_input: EditText = findViewById(R.id.title_input)
             var paramsTitleInput:ViewGroup.LayoutParams = title_input.getLayoutParams()
             paramsTitleInput.width=ViewGroup.LayoutParams.MATCH_PARENT;
             paramsTitleInput.height = ViewGroup.LayoutParams.WRAP_CONTENT;
             title_input.setLayoutParams(paramsTitleInput);
             var user_name: EditText = findViewById(R.id.user_name)
             var paramsUserName:ViewGroup.LayoutParams =user_name.getLayoutParams()
             paramsUserName.width=ViewGroup.LayoutParams.MATCH_PARENT;
             paramsUserName.height = ViewGroup.LayoutParams.WRAP_CONTENT;
             user_name.setLayoutParams(paramsUserName);
             var all_day: TextView = findViewById(R.id.all_day)
             var paramsAllDay:ViewGroup.LayoutParams =all_day.getLayoutParams()
             paramsAllDay.width=(screenWidth / 1.64).toInt()
             paramsAllDay.height = ViewGroup.LayoutParams.WRAP_CONTENT;
             all_day.setLayoutParams(paramsAllDay);
             var switch: Switch = findViewById(R.id.switch1)
             var paramsSwitch:ViewGroup.LayoutParams =switch.getLayoutParams()
             paramsSwitch.width=(screenWidth / 4)
             paramsSwitch.height = paramsAllDay.height
             switch.setLayoutParams(paramsSwitch)*/
            var start_date: TextView = findViewById(R.id.start_date)
            var paramsStartDate: ViewGroup.LayoutParams = start_date.getLayoutParams()
            paramsStartDate.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            paramsStartDate.width = (screenWidth / 2.5).toInt()
            start_date.setLayoutParams(paramsStartDate)

            var end_date: TextView = findViewById(R.id.end_date)
            var paramsEndDate: ViewGroup.LayoutParams = end_date.getLayoutParams()
            paramsEndDate.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            paramsEndDate.width = (screenWidth / 2.5).toInt()
            end_date.setLayoutParams(paramsEndDate);

            var start_time: TextView = findViewById(R.id.start_time)
            var paramsStartTime: ViewGroup.LayoutParams = start_time.getLayoutParams()
            paramsStartTime.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            paramsStartTime.width = (screenWidth / 2.5).toInt()
            start_time.setLayoutParams(paramsStartTime);


            var end_time: TextView = findViewById(R.id.end_time)
            var paramsEndTime: ViewGroup.LayoutParams = end_time.getLayoutParams()
            paramsEndTime.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            paramsEndTime.width = (screenWidth / 2.5).toInt()
            end_time.setLayoutParams(paramsEndTime);


            /*  var guestLayout = findViewById<LinearLayout>(R.id.guestslayout)
              var params: ViewGroup.LayoutParams = guest_names.getLayoutParams();
              params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
              guest_names.setLayoutParams(params);
              var params1: ViewGroup.LayoutParams = guestLayout.getLayoutParams();
              params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
              guestLayout.setLayoutParams(params1);
              var description_event: EditText = findViewById(R.id.description)
              setDimensions(description_event, screenWidth, (screenHeight / 9))
  */
        }
        guest_names = findViewById(R.id.guest_names)

        createBottomSheetDialog()
        var e1: TextInputEditText = findViewById(R.id.start_time)
        var e2: TextInputEditText = findViewById(R.id.end_time)
        var d1: TextInputEditText = findViewById(R.id.start_date)
        var d2: TextInputEditText = findViewById(R.id.end_date)
      e11Layout = findViewById(R.id.start_time_field)
         e12Layout = findViewById(R.id.end_time_field)
      d11Layout = findViewById(R.id.start_date_layout)
        d12Layout= findViewById(R.id.end_date_layout)
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

            /* if(e1.text!=null && e2.text!=null && d1.text!=null && d2.text!=null)
                 checkDateAndTime(d1,d2,e1,e2)
             else if(d1.text!=null &&d2.text!=null){
                 checkDates(d1,d2)
             }*/
        }
        e2!!.setOnClickListener {

            clickTimePicker(e2)

            /*if(e1.text!=null&&e2.text!=null && d1.text!=null &&d2.text!=null)
                checkDateAndTime(d1,d2,e1,e2)
            else if(d1.text!=null &&d2.text!=null){
                checkDates(d1,d2)
            }*/
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
                /*if(e1.text!=null&&e2.text!=null && d1.text!=null &&d2.text!=null)
                    checkDateAndTime(d1,d2,e1,e2)
                else if(d1.text!=null &&d2.text!=null){
                    checkDates(d1,d2)
                }*/

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
                //if(e1.text!=null&&e2.text!=null && d1.text!=null &&d2.text!=null)
                //  checkDateAndTime(d1,d2,e1,e2)
                //else if(d1.text!=null &&d2.text!=null){
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

                //}

            }
        }



        d1!!.setOnClickListener(
            {

                DatePickerDialog(
                    this@AddEvent, R.style.MyDialogTheme,
                    dateSetListener,
                    mYear, mMonth, mDay
                ).show()

            })
        d2!!.setOnClickListener(
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
        var allday = findViewById<TextView>(R.id.all_day)
        allday.setOnClickListener {
            var status = sw.isChecked()
            sw.isChecked = !status
        }
       /* guests.setOnClickListener {
            showDialog1()
        }
        guest_names.setOnClickListener {
            showDialog1()
        }*/
        guestsFieldLayout.setOnClickListener {
            showDialog1()
        }
        guestsFieldLayout
        val intent = intent
        val startDate = intent.getStringExtra("startDate")
        val startTime = intent.getStringExtra("startTime")
        val endDate = intent.getStringExtra("endDate")
        val endTime = intent.getStringExtra("endTime")

        d1.setText(startDate)
        e1.setText(startTime)
        d2.setText(endDate)
        e2.setText(endTime)
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

    private fun checkDates(d1: TextInputEditText, d2: TextInputEditText): Boolean {
        var status = false
        try {

            val formatter1 = SimpleDateFormat("dd/MM/yyyy")
            formatter1.timeZone = TimeZone.getTimeZone("GMT")
            var date = formatter1.parse(d1.text.toString())
            var date1 = formatter1.parse(d2.text.toString())

            if (date1.after(date) || date1.equals(date)) {
                status = true
            } else
                status = false
            return status

        } catch (e: ParseException) {
            return false

        }


    }

    override fun onSupportNavigateUp(): Boolean {

        AlertDialog.Builder(this)
            .setIcon(R.drawable.ic_warning_black_24dp)
            .setTitle("Cancel")
            .setMessage("Discard your changes?")
            .setPositiveButton("Yes") { dialog, which -> finish() }
            .setNegativeButton("No", null)
            .show()
        return true

    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setIcon(R.drawable.ic_warning_black_24dp)
            .setTitle("Cancel")
            .setMessage("Discard your changes?")
            .setPositiveButton("Yes") { dialog, which -> finish() }
            .setNegativeButton("No", null)
            .show()
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
                    amOrPm = "am";
                } else {
                    amOrPm = "pm";
                }
                hour = h
                minute = m
                var timeSet = ""
                if (hour > 12) {
                    hour -= 12
                    timeSet = "pm"
                } else if (hour === 0) {
                    hour += 12
                    timeSet = "am"
                } else if (hour === 12) {
                    timeSet = "pm"
                } else {
                    timeSet = "am"
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
                var eventId = uuid.generateUUID()
                var title = title_input.text
                var userId = user_name.text
                var startDate = dsc.getDateInMillis("${start_date.text} ${start_time.text}")
                var endDate = dsc.getDateInMillis("${end_date.text} ${end_time.text}")
                var description = description.text
                var event = Event(eventId, title.toString(), startDate, endDate, guestList, description.toString())
                var addEvent = AddEvent(event, userId.toString())
                var result: Boolean = addEvent.add(dataBaseManager)

                if (result == true) {
                    Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(this, "not saved", Toast.LENGTH_SHORT).show()
                }
                monthPagerAdapter.notifyDataSetChanged()
                dayPagerAdapter.notifyDataSetChanged()
                weekPagerAdapter.notifyDataSetChanged()
                //setResult(Activity.RESULT_OK)
                finish()
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
        var b:Bundle=Bundle();
        b.putStringArrayList("nameList", guestList);
        println("GuestList: "+guestList)
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
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1011) {
            if (resultCode == 1) {
                var bundle = data!!.getBundleExtra("bundle");
                guestList.removeAll(guestList)
                guest_names.removeAllViews()
                var stockkList: ArrayList<Contacts> = bundle!!.getParcelableArrayList("arrayList");
                if (stockkList.size == 0) {

                    var params = guests.layoutParams
                    params.width = 500
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    guests.layoutParams = params
                    guests.text = "Add Guests"
                    guestLayout.visibility = View.INVISIBLE
                }
               // guest_names.removeAllViews()
                if(stockkList.size!=0){
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
                    onItemSelected(stockkList.get(i))
                }

            }
        } else if (requestCode == 2) {

        }
    }

    fun onItemSelected(contact: Contacts) {


        setTheme(R.style.Theme_MaterialComponents_Light_DarkActionBar)
        var chip = Chip(this@AddEvent)
        chip.setText(contact.name)
        val options = BitmapFactory.Options()


        var bitmap = BitmapFactory.decodeByteArray(contact.picId, 0, contact.picId.size, options) //Convert bytearray to bitmap
       Bitmap.createScaledBitmap(bitmap, 60, 40, true)
        var d: RoundedBitmapDrawable =
            RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        d.setCircular(true);
       //var d: Drawable = BitmapDrawable(resources,bitmap);


        /* Bitmap bitmap = BitmapFactory.decodeFile("/path/images/image.jpg");
   ByteArrayOutputStream blob = new ByteArrayOutputStream();
   bitmap.compress(CompressFormat.PNG, 0 /* Ignored for PNGs */, blob);
   byte[] bitmapdata = blob.toByteArray();
         val options = BitmapFactory.Options()
         var bitmap = BitmapFactory.decodeByteArray(contacts[position].picId, 0, contacts[position].picId.size, options) //Convert bytearray to bitmap
         im!!.setImageBitmap(bitmap);
         chip.setImage
         val options = BitmapFactory.Options()
         var bitmap = BitmapFactory.decodeByteArray(contact.picId, 0, contact.picId.size, options) //Convert bytearray to bitmap*/

        chip.setChipIcon(d)
        chip.setCloseIconVisible(true)
        chip.setCheckable(false)
        chip.setClickable(false)
        chip.setOnCloseIconClickListener(this@AddEvent)

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

    fun hideKeyBoard(){
        var view:View=this.currentFocus
        if(view!=null){
            var imm:InputMethodManager=getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken,0)
        }
    }
    fun showKeyBoard(){
        var view:View=this.currentFocus
        if(view!=null){
            var imm:InputMethodManager=getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view,0)
        }
    }
 override fun onTouchEvent( event:MotionEvent) :Boolean{
        // Pass activity on touch event to the gesture detector.
        gestureDetectorCompat!!.onTouchEvent(event);
        // Return true to tell android OS that event has been consumed, do not pass it to other event listeners.
        return true;
    }

}

