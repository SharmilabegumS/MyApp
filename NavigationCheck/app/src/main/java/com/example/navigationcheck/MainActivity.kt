package com.example.navigationcheck

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.transition.FragmentTransitionSupport
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.DatePicker
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
var screenWidthCommon=0
var screenHeightCommon=0
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
lateinit var btn:Button
    var year1:Int=0
    var day:Int=0
    var month:Int=0
    val DIALOG_ID=0


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

        nav_view.setNavigationItemSelectedListener(this)
        var ft:FragmentTransaction=supportFragmentManager.beginTransaction()
        ft.replace(R.id.flMain,MonthFragment())
        ft.commit()
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
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_settings -> {
                val i:Intent=Intent(this@MainActivity,SettingActivity::class.java)
                        startActivity(i)
            }
            R.id.nav_home-> {
                var ft:FragmentTransaction=supportFragmentManager.beginTransaction()
                ft.replace(R.id.flMain,HomeFragment())
                ft.commit()
            }
            R.id.nav_day_mode-> {
                var ft:FragmentTransaction=supportFragmentManager.beginTransaction()
                ft.replace(R.id.flMain,DayFragment())
                ft.commit()
            }
            R.id.nav_Month_mode-> {
                var ft:FragmentTransaction=supportFragmentManager.beginTransaction()
                ft.replace(R.id.flMain,MonthFragment())
                ft.commit()
            }
            R.id.nav_Week_mode-> {
                var ft:FragmentTransaction=supportFragmentManager.beginTransaction()
                ft.replace(R.id.flMain,WeekFragment())
                ft.commit()
            }
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

}
