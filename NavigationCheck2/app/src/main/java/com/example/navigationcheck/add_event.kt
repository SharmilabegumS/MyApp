package com.example.navigationcheck

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
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
import kotlinx.android.synthetic.main.activity_add_event.*
import java.text.SimpleDateFormat
import java.util.*
import android.widget.*
import android.widget.TextView
import com.example.navigationcheck.DataBase.DataBaseManager
import com.example.navigationcheck.Domain.AddEvent
import com.example.navigationcheck.Entity.Event
import com.example.navigationcheck.Utility.UUIDGenerator
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import android.widget.Toast
import android.widget.ArrayAdapter
import androidx.core.app.NavUtils
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.fragment.app.Fragment
import com.example.navigationcheck.Utility.DateStringConvertor
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_add_event.description
import kotlinx.android.synthetic.main.activity_add_event.end_date
import kotlinx.android.synthetic.main.activity_add_event.end_time
import kotlinx.android.synthetic.main.activity_add_event.guests
import kotlinx.android.synthetic.main.activity_add_event.start_date
import kotlinx.android.synthetic.main.activity_add_event.start_time
import kotlinx.android.synthetic.main.activity_add_event.title_input
import kotlinx.android.synthetic.main.activity_add_event.toolbar1
import kotlinx.android.synthetic.main.activity_add_event.user_name
import kotlinx.android.synthetic.main.add_event_modified.*
import java.io.ByteArrayOutputStream
import java.text.ParseException
import kotlin.collections.ArrayList


var screenWidth=0
var screenHeight=0
var cal = Calendar.getInstance()
var uuid=UUIDGenerator
var c = Calendar.getInstance()
var mYear = c.get(Calendar.YEAR)
var mMonth = c.get(Calendar.MONTH)
var mDay = c.get(Calendar.DAY_OF_MONTH)
var dsc=DateStringConvertor(cal)
@SuppressLint("NewApi")
class  add_event : AppCompatActivity(),View.OnClickListener {
    var calendar = Calendar.getInstance()
    var textview_start_date: TextInputEditText? = null
    var textview_end_date: TextInputEditText? = null
    var textview_start_time: TextInputEditText? = null
    var textview_end_time: TextInputEditText? = null
    var bottomSheetDialog: BottomSheetDialog? = null
lateinit var user_name:TextView
    lateinit var title_input:EditText
    lateinit var description:EditText
    lateinit var guest_names: ChipGroup
    lateinit var guestLayout: LinearLayout
    var guestList = ArrayList<String>()

    var time: String = ""
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
        user_name=findViewById(R.id.user_name)
        title_input=findViewById(R.id.title_input)
        description=findViewById(R.id.description)
        guestLayout = findViewById(R.id.guestslayout)
        var paramsGuestLayout:RelativeLayout.LayoutParams = guestLayout.getLayoutParams() as RelativeLayout.LayoutParams

        paramsGuestLayout.setMargins(0, 2, 0, 0);
        guestLayout.setLayoutParams(paramsGuestLayout);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
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
            var paramsStartDate:ViewGroup.LayoutParams =start_date.getLayoutParams()
            paramsStartDate.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            paramsStartDate.width= (screenWidth/2.5).toInt()
            start_date.setLayoutParams(paramsStartDate)

            var end_date: TextView = findViewById(R.id.end_date)
            var paramsEndDate:ViewGroup.LayoutParams =end_date.getLayoutParams()
            paramsEndDate.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            paramsEndDate.width= (screenWidth/2.5).toInt()
            end_date.setLayoutParams(paramsEndDate);

            var start_time: TextView = findViewById(R.id.start_time)
            var paramsStartTime:ViewGroup.LayoutParams = start_time.getLayoutParams()
            paramsStartTime.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            paramsStartTime.width= (screenWidth/2.5).toInt()
            start_time.setLayoutParams(paramsStartTime);


            var end_time: TextView = findViewById(R.id.end_time)
            var paramsEndTime:ViewGroup.LayoutParams = end_time.getLayoutParams()
            paramsEndTime.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            paramsEndTime.width= (screenWidth/2.5).toInt()
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
        guest_names = findViewById<ChipGroup>(R.id.guest_names)

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
           /* if(e1.text!=null && e2.text!=null && d1.text!=null && d2.text!=null)
                checkDateAndTime(d1,d2,e1,e2)
            else if(d1.text!=null &&d2.text!=null){
                checkDates(d1,d2)
            }*/
        }
        e2!!.setOnClickListener{
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
                mDay=dayOfMonth
                mMonth=monthOfYear
                mYear=year
                updateDateInView(d1)
                updateDateInView(d2)
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
                mDay=dayOfMonth
                mMonth=monthOfYear
                mYear=year
                updateDateInView(d2)
                //if(e1.text!=null&&e2.text!=null && d1.text!=null &&d2.text!=null)
                  //  checkDateAndTime(d1,d2,e1,e2)
                //else if(d1.text!=null &&d2.text!=null){
                   var result= checkDates(d1,d2)
                if(result==false){
                    d1.setError("Start date is after event end date!")
                    e1.setError("Start time is after event end time!")
                }
                else if(result==true){
                    d1.error=null
                    e1.error=null
                }

                //}

            }
        }



        d1!!.setOnClickListener(
            {

               DatePickerDialog(
                    this@add_event,R.style.MyDialogTheme,
                    dateSetListener,
                    mYear, mMonth, mDay
                ).show()

            })
       d2!!.setOnClickListener(
            {

                DatePickerDialog(
                    this@add_event,R.style.MyDialogTheme,
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
        val intent = intent
        val startDate = intent.getStringExtra("startDate")
        val startTime=intent.getStringExtra("startTime")
        val endDate = intent.getStringExtra("endDate")
        val endTime=intent.getStringExtra("endTime")

        d1.setText(startDate)
        e1.setText(startTime)
        d2.setText(endDate)
        e2.setText(endTime)
    }

    private fun checkDateAndTime(d1: TextInputEditText, d2: TextInputEditText, e1: TextInputEditText, e2: TextInputEditText) {


    }
    private fun checkDates(d1: TextInputEditText, d2: TextInputEditText):Boolean {
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
                 guests.text=""
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
        var chip= Chip(this@add_event)
        chip.setText(contact.name)
      val options = BitmapFactory.Options()


      var bitmap = BitmapFactory.decodeByteArray(contact.picId, 0, contact.picId.size, options) //Convert bytearray to bitmap
      var d:RoundedBitmapDrawable  =
          RoundedBitmapDrawableFactory.create(getResources(), bitmap);
    d.setCircular(true);


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
        chip.setOnCloseIconClickListener(this@add_event)

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

