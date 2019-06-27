package com.example.navigationcheck

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.*
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.transition.FragmentTransitionSupport
import androidx.fragment.app.FragmentTransaction
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.CalendarView
import android.widget.DatePicker
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.navigationcheck.DataBase.DataBaseManager
import com.example.navigationcheck.Entity.Event
import com.example.navigationcheck.adapters.MonthPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


var screenWidthCommon=0
var screenHeightCommon=0
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
lateinit var btn:Button
    var year1:Int=0
    var day:Int=0
    var month:Int=0
    val DIALOG_ID=0
    //var date2=Date()
   // var date0=Date()


    var dataBaseManager:DataBaseManager= DataBaseManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val context: Context?= applicationContext
        screenWidthCommon=getScreenWidthInDPs(context!!)
        screenHeightCommon=getScreenHeightInDPs(context)
        fab.setOnClickListener (object :View.OnClickListener{
            override fun onClick(view: View): Unit {
                // Handler code here.
                val intent = Intent(this@MainActivity, add_event::class.java);
                startActivity(intent);
            }
        })

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        toolbar.setTitleTextColor(Color.WHITE);

        nav_view.setNavigationItemSelectedListener(this)
        var fragment=MonthFragment()
        var ft: androidx.fragment.app.FragmentTransaction =supportFragmentManager.beginTransaction()
        ft.replace(R.id.framelayout,fragment).addToBackStack("tag")
        ft.commit()
       /* var pager = findViewById<ViewPager>(R.id.pager1)
        var monthPagerAdapter = MonthPagerAdapter(context, supportFragmentManager, 3)
        pager.adapter = monthPagerAdapter*/


    }

fun setActionBarTitle(title:String){
        supportActionBar?.setTitle(title)

}
    fun setActionBarContents(){
        supportActionBar?.setHomeButtonEnabled(true)
    }
    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }

        if (getSupportFragmentManager().getBackStackEntryCount() == 1 || getSupportFragmentManager().getBackStackEntryCount() > 0){
           supportFragmentManager.popBackStackImmediate();
            finish();
        }
        else {
            super.onBackPressed();
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.calculate_date -> return true
            R.id.jump_to_date -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
           /*R.id.nav_settings -> {
                var ft: androidx.fragment.app.FragmentTransaction =supportFragmentManager.beginTransaction()
                ft.replace(R.id.flMain,SettingFragment()).addToBackStack("tag")
                ft.commit()
            }
            R.id.nav_home-> {
                var ft: androidx.fragment.app.FragmentTransaction =supportFragmentManager.beginTransaction()
                ft.replace(R.id.flMain,HomeFragment()).addToBackStack("tag")
                ft.commit()
            }
            R.id.nav_day_mode-> {
                var mPager = findViewById<ViewPager>(R.id.pager);
                var mpa=DayPageAdapter(supportFragmentManager)
                mPager.setAdapter(mpa);
            }*/
            R.id.nav_Month_mode-> {
                var fragment=MonthFragment()
                var ft: androidx.fragment.app.FragmentTransaction =supportFragmentManager.beginTransaction()
                ft.replace(R.id.framelayout,fragment).addToBackStack("tag")
                ft.commit()
            }/*
            R.id.nav_Week_mode-> {
                var mPager = findViewById<ViewPager>(R.id.pager);
                var mpa=WeekPageAdapter(supportFragmentManager)
                mPager.setAdapter(mpa);
            }*/
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
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
   fun setColour(view:View) {
       var gradientDrawable:     StateListDrawable =  view.getBackground() as (StateListDrawable)
       var drawableContainerState: DrawableContainer.DrawableContainerState =  gradientDrawable.getConstantState() as (DrawableContainer.DrawableContainerState)
       var children = drawableContainerState.getChildren()
       var selectedItem:  GradientDrawable  = children[0] as GradientDrawable
       selectedItem.setColor(Color.BLUE)

    }

    /*private inner class MonthPageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

     override fun getCount(): Int{
         return 3
     }


        override fun getItem(pos: Int): Fragment? {
            when (pos) {

                0 -> return MonthFragment.newInstance(date0)
                1 -> return MonthFragment.newInstance(Date())
                2 -> return MonthFragment.newInstance(date2)
                else -> return null
            }
        }
    }
    private inner class WeekPageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getCount(): Int{
            return 3
        }


        override fun getItem(pos: Int): Fragment? {
            when (pos) {


                0 -> return WeekFragment.newInstance(date0)
                1 -> return WeekFragment.newInstance(Date())
                2 -> return WeekFragment.newInstance(date2)
                                else -> return null
            }
        }
    }
    private inner class DayPageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getCount(): Int{
            return 3
        }


        override fun getItem(pos: Int): Fragment? {
            when (pos) {

                  0 -> return DayFragment.newInstance(date0)
                1 -> return DayFragment.newInstance(Date())
                2 -> return DayFragment.newInstance(date2)
                else -> return null
            }
        }
    }*/
}

