package com.example.navigationcheck

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v7.app.AlertDialog
import android.util.DisplayMetrics
import android.view.*
import kotlinx.android.synthetic.main.activity_add_event.*
import java.text.SimpleDateFormat
import java.util.*
import android.widget.*
import android.widget.TextView


var screenWidth=0
var screenHeight=0
class  add_event : AppCompatActivity() {
    var textview_start_date: TextView? = null
    var textview_end_date: TextView? = null
    var textview_start_time: TextView? = null
    var textview_end_time: TextView? = null
    var bottomSheetDialog: BottomSheetDialog?=null
    var cal = Calendar.getInstance()
    var time:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_add_event)
        setSupportActionBar(toolbar_add)
        val context: Context?= applicationContext
     screenWidth=getScreenWidthInDPs(context!!)
  screenHeight=getScreenHeightInDPs(context)




        // Now get the support action bar
        val actionBar = supportActionBar

        // Set toolbar title/app title
        actionBar!!.title = "Event"

        // Set action bar/toolbar sub title


        // Set action bar elevation
        actionBar.elevation = 4.0F

        // Display the app icon in action bar/toolbar
        actionBar.setDisplayShowHomeEnabled(true)
        actionBar.setDisplayUseLogoEnabled(false)
        var title:TextView=findViewById(R.id.title)
        setDimensions(title,screenWidth/5,(screenHeight/10))
        var title_input:EditText=findViewById(R.id.title_input)
        setDimensions(title_input,(screenWidth/1.33).toInt(),(screenHeight/10))
        var user:TextView=findViewById(R.id.user)
        setDimensions(user,screenWidth/5,(screenHeight/10))
        var user_name:TextView=findViewById(R.id.user_name)
        setDimensions(user_name,(screenWidth/1.33).toInt(),(screenHeight/10))
        var all_day:TextView=findViewById(R.id.all_day)
        setDimensions(all_day,(screenWidth/1.64).toInt(),(screenHeight/15))
        var switch:Switch=findViewById(R.id.switch1)
        setDimensions(switch,screenWidth/4,(screenHeight/15))
        var start_date:TextView=findViewById(R.id.start_date)
        setDimensions(start_date,screenWidth/2,(screenHeight/10))
        var end_date:TextView=findViewById(R.id.end_date)
        setDimensions(end_date,screenWidth/2,(screenHeight/10))
        var start_time:TextView=findViewById(R.id.start_time)
        setDimensions(start_time,screenWidth/2,(screenHeight/10))
        var end_time:TextView=findViewById(R.id.end_time)
        setDimensions(end_time,screenWidth/2,(screenHeight/10))
        var guest:TextView=findViewById(R.id.guests)
        setDimensions(guest,screenWidth,(screenHeight/20))
        var guest_names:EditText=findViewById(R.id.guest_names)
        setDimensions(guest_names,screenWidth,(screenHeight/9))
        var description:TextView=findViewById(R.id.description)
        setDimensions(description,screenWidth,(screenHeight/20))
        var description_event:EditText=findViewById(R.id.description_event)
        setDimensions(description_event,screenWidth,(screenHeight/9))
        textview_start_date = this.start_date
        textview_end_date = this.end_date
       textview_start_time=this.start_time
        textview_end_time=this.end_time
        createBottomSheetDialog()

        var e1:TextView=findViewById(R.id.start_time)
        var e2:TextView=findViewById(R.id.end_time)
        var d1:TextView=findViewById(R.id.start_date)
        var d2:TextView=findViewById(R.id.end_date)
        textview_start_time!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {

                clickTimePicker(e1)
            }
        })
        textview_end_time!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {

                clickTimePicker(e2)
            }
        })
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            }
        }

        textview_start_date!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(this@add_event,
                    dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
                updateDateInView(d1)
            }

        })
        textview_end_date!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(this@add_event,
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
                updateDateInView(d2)
            }

        })
        val sw = findViewById<Switch>(R.id.switch1)
        sw?.setOnCheckedChangeListener({ _, isChecked ->
        if (isChecked){
            e1.setText("")
            e2.setText("")
            e1.isEnabled =false
            e2.isEnabled =false

            } else {
            e1.isEnabled =true
            e2.isEnabled =true
        }
        })

    }

    private fun updateDateInView(editText: TextView) {
        val myFormat = "MM/dd/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        editText!!.setText(sdf.format(cal.getTime()))
    }
    fun clickTimePicker(editText:TextView) {
        val c = Calendar.getInstance()
        var hour = c.get(Calendar.HOUR)
        var minute = c.get(Calendar.MINUTE)
        lateinit var amOrPm:String

        val tpd = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener(function = { view, h, m ->
            if(h < 12) {
                amOrPm= "AM";
            } else {
                amOrPm= "PM";
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
            var hours=""
            if (hour < 10)
                hours = "0$hour"
            else
                hours =hour.toString()
            var min = ""
            if (minute < 10)
                min = "0$minute"
            else
                min = minute.toString()

            val aTime = StringBuilder().append(hours).append(':')
                .append(min).append(" ").append(timeSet).toString()
            editText.setText(aTime)
            time=String.format("%02d",h) + " : " + String.format("%02d",m) +" "+amOrPm

        }),hour,minute,false
        )
        tpd.show()
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
            R.id.close -> {
                Toast.makeText(this,"closed",Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.done -> {
                Toast.makeText(this,"saved",Toast.LENGTH_SHORT).show()

                return true
            }
else->
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
    fun getScreenWidthInDPs(context:Context ):Int{
        var dm = DisplayMetrics();
        var windowManager: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager;
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels
    }
    fun getScreenHeightInDPs(context:Context ):Int{
        var dm = DisplayMetrics()
        var windowManager: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels
    }

}