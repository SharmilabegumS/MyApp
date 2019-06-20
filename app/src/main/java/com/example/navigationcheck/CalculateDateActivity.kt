package com.example.navigationcheck

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.viewpager.widget.ViewPager
import com.example.navigationcheck.adapter.TabAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_add_event.*
import java.text.SimpleDateFormat
import java.util.*

class CalculateDateActivity : AppCompatActivity() {

    var tabLayout: TabLayout ?=null;
    var viewPager:ViewPager?=null
    var date:Date?=null
    var startDate:Date?=null
    var endDate:Date?=null
    var numberOfDays:Int?=null
    var forwardOrBackward:String?=null
    val format = SimpleDateFormat("EEEE, dd MMMM, yyyy")
    var format1=SimpleDateFormat("EEEE")
    var format2=SimpleDateFormat("dd MMMM, yyyy")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculate_date)
        val intent = intent
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        date = sdf.parse(intent.getStringExtra("startDate"))
        setSupportActionBar(toolbar1)

        // Now get the support action bar
        val actionBar = supportActionBar
        // Set toolbar title/app title

        actionBar!!.title = "Calculate date"

        // Set action bar/toolbar sub title


        // Set action bar elevation
        var  calculateDateByNumber=CalculateDateByNumber()
        var calculateDateByInterval=CalculateDateByInterval()
        actionBar.elevation = 4.0F
        actionBar.setDisplayHomeAsUpEnabled(true)
        toolbar1.setTitleTextColor(Color.WHITE);
        viewPager = findViewById(R.id.pager_calculate_date);
        tabLayout =  findViewById(R.id.tabLayout);
        var adapter:TabAdapter =  TabAdapter(getSupportFragmentManager())
        adapter.addFragment(calculateDateByNumber, "Tab 1");
        adapter.addFragment(calculateDateByInterval, "Tab 2");
        viewPager!!.setAdapter(adapter);
        tabLayout!!.setupWithViewPager(viewPager);
        println("endDate: "+endDate)
        println("start Date: "+startDate)
        var calculateButton=findViewById<Button>(R.id.calculate_date_button)

            calculateButton.setOnClickListener {
var fragment=adapter.getItem(viewPager!!.currentItem)
                if(fragment is CalculateDateByNumber ) {
                    fragment.getEndDate1()
                    fragment.getStartDate1()
                    fragment.getNumberDays()
                }
                else if(fragment is CalculateDateByInterval){
                    fragment.getEndDate1()
                    fragment.getStartDate1()
                    fragment.getNumberDays()
                }
                if(endDate!=null && numberOfDays!=null) {
                val intent = Intent(this@CalculateDateActivity, CalculateDateResult::class.java);
                    intent.putExtra("endDate", format2.format(endDate))
                    intent.putExtra("endDay",format1.format(endDate))
                    intent.putExtra("startDate", format.format(startDate))
                    intent.putExtra("numberOfDays", numberOfDays.toString())
                    intent.putExtra("forwardOrBackward",forwardOrBackward)
                    println("No of days: "+numberOfDays)

                    startActivityForResult(intent,1001);
                    finish()
            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==1001){
            if(resultCode== Activity.RESULT_OK){
                var bundle = data!!.getBundleExtra("bundle");
                println("received date: "+bundle.getString("endDate"))
                /*val sdf = SimpleDateFormat("dd MMMM, yyyy")

                date = sdf.parse(bundle.getString("endDate"))*/

                val resultIntent = Intent()
                resultIntent.putExtra("bundle", bundle)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }
    }

    fun setDateOfActivity(dateEvent: Date?) {
        val activity = parentActivityIntent as MainActivity
        //activity!!.date=Date()
        activity!!.setDateCommon(dateEvent!!)
        activity!!.getCurrentMonthView()
    }


}
