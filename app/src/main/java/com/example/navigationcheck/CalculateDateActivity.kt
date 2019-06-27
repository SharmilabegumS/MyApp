package com.example.navigationcheck

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.example.navigationcheck.adapter.TabAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_add_event.*
import java.text.SimpleDateFormat
import java.util.*

class CalculateDateActivity : AppCompatActivity() {

    var tabLayout: TabLayout? = null;
    var viewPager: ViewPager? = null
    var date: Date? = null
    var startDate: Date? = null
    var endDate: Date? = null
    var numberOfDays: Int? = null
    var forwardOrBackward: String? = null
    var status1:Boolean?=null
    val format = SimpleDateFormat("EEEE, dd MMMM, yyyy")
    var format1 = SimpleDateFormat("EEEE")
    var format2 = SimpleDateFormat("dd MMMM, yyyy")
var context:Activity?=null
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculate_date)
        val intent = intent
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        date = sdf.parse(intent.getStringExtra("startDate"))
        setSupportActionBar(toolbar1)
context= this
        // Now get the support action bar
        val actionBar = supportActionBar
        // Set toolbar title/app title

        actionBar!!.title = "Calculate date"

        // Set action bar/toolbar sub title


        // Set action bar elevation
        var calculateDateByNumber = CalculateDateByNumber()
        var calculateDateByInterval = CalculateDateByInterval()
        actionBar.elevation = 4.0F
        actionBar.setDisplayHomeAsUpEnabled(true)
        toolbar1.setTitleTextColor(Color.WHITE);
        viewPager = findViewById(R.id.pager_calculate_date);
        tabLayout = findViewById(R.id.tabLayout);
        var adapter: TabAdapter = TabAdapter(getSupportFragmentManager())
        adapter.addFragment(calculateDateByNumber, "Calculate date");
        adapter.addFragment(calculateDateByInterval, "Interval");
        viewPager!!.setAdapter(adapter);
        tabLayout!!.setupWithViewPager(viewPager);
        println("endDate: " + endDate)
        println("start Date: " + startDate)
        var calculateButton = findViewById<Button>(R.id.calculate_date_button)

        calculateButton.setOnClickListener {
            var fragment = adapter.getItem(viewPager!!.currentItem)
            if (fragment is CalculateDateByNumber) {
                fragment.getEndDate1()
                fragment.getStartDate1()
                fragment.getNumberDays()

            } else if (fragment is CalculateDateByInterval) {
                fragment.getEndDate1()
                fragment.getStartDate1()
                fragment.getNumberDays()

            }
            var calendarStart=Calendar.getInstance()
            calendarStart.time=startDate
            var calendarEnd=Calendar.getInstance()
            calendarEnd.set(Calendar.YEAR,2099)
            calendarEnd.set(Calendar.MONTH,11)
            calendarEnd.set(Calendar.DAY_OF_MONTH,31)

            val difference = dsc.getDateInMillis1(calendarEnd.time)- dsc.getDateInMillis1(calendarStart.time)
            val daysBetween = (difference / (1000 * 60 * 60 * 24))


            if (endDate != null && numberOfDays != null) {
                if (numberOfDays!! > daysBetween) {
                    var toast=Toast.makeText(this,"You can enter up to $daysBetween days!",Toast.LENGTH_SHORT)
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show()

                } else{
                val intent = Intent(this@CalculateDateActivity, CalculateDateResult::class.java);
                intent.putExtra("endDate", format2.format(endDate))
                intent.putExtra("endDay", format1.format(endDate))
                intent.putExtra("startDate", format.format(startDate))
                intent.putExtra("numberOfDays", numberOfDays.toString())
                intent.putExtra("forwardOrBackward", forwardOrBackward)
                println("No of days: " + numberOfDays)

                startActivityForResult(intent, 1001);
            }
            }
            else{
                Toast.makeText(this,"Fill the fields properly!",Toast.LENGTH_SHORT).show()
            }

        }
        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
               hideSoftKeyboard(context!!)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001) {
            if (resultCode == Activity.RESULT_OK) {
                var bundle = data!!.getBundleExtra("bundle");
                println("received date: " + bundle.getString("endDate"))
                /*val sdf = SimpleDateFormat("dd MMMM, yyyy")

                date = sdf.parse(bundle.getString("endDate"))*/

                val resultIntent = Intent()
                resultIntent.putExtra("bundle", bundle)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
            else if(resultCode==111){
                finish()
            }
            else {
            }
        }

    }
    fun showKeyBoard(){
        status1=true
        var view: View =this.currentFocus
        if(view!=null){
            var imm:InputMethodManager=getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view,0)
        }
    }
    fun hideSoftKeyboard(activity: Activity) {
        if (status1 == true) {
            val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
        }
    }

}
