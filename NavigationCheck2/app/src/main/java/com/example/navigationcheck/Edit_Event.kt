package com.example.navigationcheck

import android.annotation.SuppressLint

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
import com.example.navigationcheck.DataBase.DataBaseManager
import com.example.navigationcheck.Entity.Event
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

import android.widget.ArrayAdapter

import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlin.collections.ArrayList

@SuppressLint("NewApi")
class  Edit_Event: AppCompatActivity(),View.OnClickListener {
    lateinit var event:Event
    lateinit var user_name:TextView
    lateinit var title_input:EditText
    lateinit var description:EditText
    var calendar = Calendar.getInstance()
    var bottomSheetDialog: BottomSheetDialog? = null

    lateinit var guest_names: ChipGroup
    lateinit var guestLayout: LinearLayout
    var guestList = ArrayList<String>()

    var time: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_event_modified)
        user_name=findViewById(R.id.user_name)
        title_input=findViewById(R.id.title_input)
        description=findViewById(R.id.description)
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
        guestLayout = findViewById(R.id.guestslayout)
        var paramsGuestLayout:RelativeLayout.LayoutParams = guestLayout.getLayoutParams() as RelativeLayout.LayoutParams

        paramsGuestLayout.setMargins(0, 2, 0, 0);
        guestLayout.setLayoutParams(paramsGuestLayout);
        var start_date: TextView = findViewById(R.id.start_date)
        var end_date: TextView = findViewById(R.id.end_date)
        var end_time: TextView = findViewById(R.id.end_time)
        var start_time: TextView = findViewById(R.id.start_time)
        guest_names = findViewById(R.id.guest_names)
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

            var paramsStartDate:ViewGroup.LayoutParams =start_date.getLayoutParams()
            paramsStartDate.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            paramsStartDate.width= (screenWidth-120)/2
            start_date.setLayoutParams(paramsStartDate)


            var paramsEndDate:ViewGroup.LayoutParams =end_date.getLayoutParams()
            paramsEndDate.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            paramsEndDate.width= (screenWidth-120)/2
            end_date.setLayoutParams(paramsEndDate);


            var paramsStartTime:ViewGroup.LayoutParams = start_time.getLayoutParams()
            paramsStartTime.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            paramsStartTime.width= (screenWidth-120)/2
            start_time.setLayoutParams(paramsStartTime);



            var paramsEndTime:ViewGroup.LayoutParams = end_time.getLayoutParams()
            paramsEndTime.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            paramsEndTime.width= (screenWidth-120)/2
            end_time.setLayoutParams(paramsEndTime);

        }


        createBottomSheetDialog()
        var e1: TextInputEditText = findViewById(R.id.start_time)
        var e2: TextInputEditText = findViewById(R.id.end_time)
        var d1: TextInputEditText = findViewById(R.id.start_date)
        var d2:TextInputEditText = findViewById(R.id.end_date)
        e1!!.setShowSoftInputOnFocus(false)
        e2!!.setShowSoftInputOnFocus(false)
        d1!!.setShowSoftInputOnFocus(false)
        d2!!.setShowSoftInputOnFocus(false)
        e1!!.setOnClickListener{
            clickTimePicker(e1)
        }
        e2!!.setOnClickListener{
            clickTimePicker(e2)
        }

        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(
                view: DatePicker, year: Int, monthOfYear: Int,
                dayOfMonth: Int
            ) {
                mDay=dayOfMonth
                mMonth=monthOfYear
                mYear=year
                updateDateInView(d1)

            }
        }
        val dateSetListener1 = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(
                view: DatePicker, year: Int, monthOfYear: Int,
                dayOfMonth: Int
            ) {
                mDay=dayOfMonth
                mMonth=monthOfYear
                mYear=year
                updateDateInView(d2)

            }
        }

        d1!!.setOnClickListener(
            {

                DatePickerDialog(
                    this@Edit_Event,R.style.MyDialogTheme,
                    dateSetListener,
                    mYear, mMonth, mDay
                ).show()



            })
        d2!!.setOnClickListener(
            {

                DatePickerDialog(
                    this@Edit_Event,R.style.MyDialogTheme,
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

        guests.setOnClickListener{
            showDialog1()
        }
        guest_names.setOnClickListener{
            showDialog1()
        }
        var title:EditText=findViewById(R.id.title_input)
        title.setText(eventID.title)
        var description:EditText=findViewById(R.id.description)
        description.setText(eventID.description)
        var user:TextView=findViewById(R.id.user_name)
        user.setText("sharmilabegum97@gmail.com")
        var sd=dsc.getDateToStringConversion(dsc.getDateFromMillis(eventID.startDate))
        var ed=dsc.getDateToStringConversion(dsc.getDateFromMillis(eventID.endDate))
        sd.substring(0,10)
        sd.substring(11,19)
        ed.substring(0,10)
        ed.substring(11,19)
        d1.setText( sd.substring(0,10))
        d2.setText( ed.substring(0,10))
        e1.setText( sd.substring(11,19))
        e2.setText( ed.substring(11,19))
        if(sd.substring(11,19).equals("12:01 AM") && ed.substring(11,19).equals("11:59 PM")){
            sw.setChecked(true)
        }
        var guests:TextView=findViewById(R.id.guests)
        guests.setText("")
        for(i in 0..eventID.guests.size-1)
        {
            var contact=dataBaseManager.getSingleContact(eventID.guests.get(i).trim())

            onItemSelected(contact!!)

        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun updateDateInView(editText: TextInputEditText) {
        val myFormat = "dd/MM/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.DAY_OF_MONTH,mDay)
        calendar.set(Calendar.MONTH, mMonth)
        editText!!.setText(sdf.format(calendar.getTime()))
    }

    fun clickTimePicker(editText: TextView) {
        val c = Calendar.getInstance()
        var hour = c.get(Calendar.HOUR)
        var minute = c.get(Calendar.MINUTE)
        lateinit var amOrPm: String

        val tpd = TimePickerDialog(
            this,R.style.MyDialogTheme1, TimePickerDialog.OnTimeSetListener(function = { view, h, m ->
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
                var eventId = eventID.eventId
                var title = title_input.text
                var userId = user_name.text
                var startDate = dsc.getDateInMillis("${start_date.text} ${start_time.text}")
                var endDate = dsc.getDateInMillis("${end_date.text} ${end_time.text}")
                var description = description.text
                var event = Event(eventId, title.toString(), startDate, endDate, guestList, description.toString())

                dataBaseManager.update(event,"sharmilabegum97@gmail.com")

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
        var intent = Intent(this, InviteGuests::class.java)
        startActivityForResult(intent, 1);

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
        var chip=v as Chip
        guest_names.removeView(chip)
        guestList.remove(chip.text)
        if(guestList.isEmpty()==true){
            guests.text="Add Guests"
            guestLayout.visibility=View.INVISIBLE
        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {


        if(requestCode==1) {

            if (resultCode == 1) {
                var bundle=data!!.getBundleExtra("bundle");

                var stockkList: ArrayList<Contacts> = bundle!!.getParcelableArrayList("arrayList");
                if(stockkList.size==0){
                    guests.text="Add Guests"
                    guestLayout.visibility=View.INVISIBLE
                }

                for(i in 0..stockkList.size-1) {
                    guestLayout.visibility=View.VISIBLE
                    onItemSelected(stockkList.get(i))
                }

            }
        }
        else if(requestCode==2){

        }
    }
    fun onItemSelected(contact:Contacts) {


        setTheme(R.style.Theme_MaterialComponents_Light_DarkActionBar)
        var chip= Chip(this@Edit_Event)
        chip.setText(contact.name)
        val options = BitmapFactory.Options()


        var bitmap = BitmapFactory.decodeByteArray(contact.picId, 0, contact.picId.size, options) //Convert bytearray to bitmap
        var d:RoundedBitmapDrawable  =
            RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        d.setCircular(true);
        chip.setChipIcon(d)
        chip.setCloseIconVisible(true)
        chip.setCheckable(false)
        chip.setClickable(false)
        chip.setOnCloseIconClickListener(this@Edit_Event)

        if(guestList.isEmpty()==false) {
            if (guestList.contains(contact.name) != true) {
                guest_names.addView(chip)
                guestList.add(contact.name)
            }
        }
        else{
            guestList.add(contact.name)

            guest_names.addView(chip)
        }
        guest_names.visibility= View.VISIBLE

    }
}

