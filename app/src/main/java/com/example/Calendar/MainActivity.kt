package com.example.Calendar

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.*
import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import android.util.DisplayMetrics
import androidx.viewpager.widget.ViewPager
import com.example.Calendar.dataBase.DataBaseManager
import com.example.Calendar.adapter.MonthPageAdapter
import java.util.*
import android.content.res.ColorStateList
import android.content.res.Resources
import androidx.core.content.ContextCompat
import android.text.style.ForegroundColorSpan
import android.text.SpannableString
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.ColorRes
import androidx.core.graphics.drawable.DrawableCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.Calendar.adapter.DayPagerAdapter
import com.example.Calendar.adapter.WeekPagerAdapter
import com.example.Calendar.entity.Event
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import java.lang.reflect.Field
import java.text.SimpleDateFormat

lateinit var monthPagerAdapter: MonthPageAdapter
lateinit var dayPagerAdapter: DayPagerAdapter
lateinit var weekPagerAdapter: WeekPagerAdapter
var screenWidthCommon = 0
var screenHeightCommon = 0
lateinit var pager1: ViewPager
var request = 1
var dayList = LinkedList<String>()
var weekList = LinkedList<String>()
var monthList = LinkedList<String>()
var status111 = true
var status11 = true
var lastSelection: String = "month"
var userName = "sharmilabegum97@gmail.com"
var isDayFragmentSwiped=false

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var channel_id = "test_channel_01"
    var date: Date = Date()
    var day: Int = 0
    var month: Int = 0
    lateinit var bottomDateHolder: TextView
    private lateinit var fabLayout: LinearLayout

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        createchannel()
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        //Service that runs in background to send notification
        var dbm=DataBaseManager(this)
        //MyJobIntentService.enqueueWork(this, intent)

        val context: Context? = applicationContext
        screenWidthCommon = getScreenWidthInDPs(context!!)
        screenHeightCommon = getScreenHeightInDPs(context)
        fabLayout = findViewById(R.id.coordinator_layout)
        bottomDateHolder = findViewById(R.id.bottom_date_holder)

        toolbar.setTitleTextColor(Color.WHITE)
        getWindow().setStatusBarColor(Color.TRANSPARENT)
        nav_view.setNavigationItemSelectedListener(this)

        //Switch user in the navigation drawer
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        val headerView: View = navigationView.getHeaderView(0)
        val navUsername: TextView = headerView.findViewById(R.id.name)
        val navUserMail: TextView = headerView.findViewById(R.id.mail_id)
        val navProfile1 = headerView.findViewById<CircleImageView>(R.id.profile_image)
        val navProfile2 = headerView.findViewById<CircleImageView>(R.id.profile_image1)
        val navProfile3 = headerView.findViewById<CircleImageView>(R.id.profile_image2)

        navProfile2.setOnClickListener {

            if (status11 == true) {
                navProfile1.setImageResource(R.drawable.profile1)
                navProfile2.setImageResource(R.drawable.profile)
                navProfile3.setImageResource(R.drawable.profile2)
                status11 = false
                navUsername.setText(getString(R.string.User2))
                navUserMail.setText(getString(R.string.User2Mail))
                userName = getString(R.string.User2Mail)

            } else {
                navProfile1.setImageResource(R.drawable.profile)
                navProfile2.setImageResource(R.drawable.profile1)
                navProfile3.setImageResource(R.drawable.profile2)
                status11 = true
                navUsername.setText(getString(R.string.User1))
                navUserMail.setText(getString(R.string.User1Mail))
                userName = getString(R.string.User1Mail)

            }

        }

        navProfile3.setOnClickListener {
            if (status111 == true) {
                navProfile1.setImageResource(R.drawable.profile2)
                navProfile3.setImageResource(R.drawable.profile)
                navProfile2.setImageResource(R.drawable.profile1)
                status111 = false
                navUsername.setText(getString(R.string.User3))
                navUserMail.setText(getString(R.string.User3Mail))
                userName = getString(R.string.User3Mail)
            } else {
                navProfile1.setImageResource(R.drawable.profile)
                navProfile3.setImageResource(R.drawable.profile2)
                navProfile2.setImageResource(R.drawable.profile1)
                status111 = true
                navUsername.setText(getString(R.string.User1))
                navUserMail.setText(getString(R.string.User1Mail))
                userName = getString(R.string.User1Mail)
            }
        }
        fab.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                // Handler code here.
                val intent = Intent(this@MainActivity, AddEvent::class.java)
                intent.putExtra(getString(R.string.StartDate), eventStartTime[0])
                intent.putExtra(getString(R.string.StartTime), eventStartTime[1])
                intent.putExtra(getString(R.string.EndDate), eventEndTime[0])
                intent.putExtra(getString(R.string.EndTime), eventEndTime[1])
                startActivityForResult(intent, 1000)
            }
        })

        pager1 = findViewById(R.id.pager1)
        pager1.offscreenPageLimit = 2
//Generate dayList
        for (i in -100..100) {
            calendar.setTime(date)
            calendar.add(Calendar.DAY_OF_MONTH, i)
            val day1: Int = calendar.get(Calendar.DAY_OF_MONTH)
            val month1: Int = calendar.get(Calendar.MONTH)
            val year1: Int = calendar.get(Calendar.YEAR)
            val dayMonthYear =
                String.format("%02d", day1) + String.format("%02d", month1) + String.format("%04d", year1)
            dayList.add(dayMonthYear)
        }
        dayPagerAdapter = DayPagerAdapter(supportFragmentManager, this, dayList)
        dayPagerAdapter.notifyDataSetChanged()
        println("First day list: "+ dayList)

//Generate monthList
        val calendar: Calendar = Calendar.getInstance()
        for (i in -100..100) {
            calendar.setTime(date)
            calendar.add(Calendar.MONTH, i)
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            val month1: Int = calendar.get(Calendar.MONTH)
            val year1: Int = calendar.get(Calendar.YEAR)
            val monthYear = String.format("%02d", month1) + String.format("%04d", year1)
            monthList.add(monthYear)
        }

        monthPagerAdapter = MonthPageAdapter(supportFragmentManager, this, monthList)
        monthPagerAdapter.notifyDataSetChanged()
        println("First month list: "+ monthList)
//Generate weekList
        for (i in -100..100) {
            calendar.setTime(date)
            calendar.add(Calendar.DAY_OF_MONTH, i * 7)
            val day1: Int = calendar.get(Calendar.DAY_OF_MONTH)
            val month1: Int = calendar.get(Calendar.MONTH)
            val year1: Int = calendar.get(Calendar.YEAR)
            val dayMonthYear =
                String.format("%02d", day1) + String.format("%02d", month1) + String.format("%04d", year1)
            weekList.add(dayMonthYear)
        }
        weekPagerAdapter = WeekPagerAdapter(supportFragmentManager, this, weekList)
        weekPagerAdapter.notifyDataSetChanged()
println("First week list: "+ weekList)
//Set default fragment as month fragment
        resetAllMenuItemsTextColor(nav_view)
        nav_view.getMenu().getItem(0).setChecked(true)

        pager1.adapter = monthPagerAdapter

        pager1.setCurrentItem(100, true)
        var lastPage = 100
        pager1.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            var calendar1: Calendar = Calendar.getInstance()
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                if (lastPage >= position) {
                    val date = monthList.get(position)
                    val month = Integer.parseInt(date.substring(0, 2))
                    val year = Integer.parseInt(date.substring(2, 6))
                    calendar1.set(Calendar.MONTH, month)
                    calendar1.set(Calendar.YEAR, year)
                    calendar1.set(Calendar.DAY_OF_MONTH, 1)
                    calendar1.add(Calendar.MONTH, -1)
                    val month1: Int = calendar1.get(Calendar.MONTH)
                    val year1: Int = calendar1.get(Calendar.YEAR)
                    val monthYear = String.format("%02d", month1) + String.format("%04d", year1)
                    val status = monthList.contains(monthYear)
                    if (status == false) {
                        monthList.addFirst(monthYear)
                        monthList.removeLast()
                        monthPagerAdapter.monthList = monthList
                        monthPagerAdapter.notifyDataSetChanged()
                        pager1.setCurrentItem(monthList.indexOf(date), true)
                    }

                } else if (lastPage < position) {
                    val date = monthList.get(position)
                    val month = Integer.parseInt(date.substring(0, 2))
                    val year = Integer.parseInt(date.substring(2, 6))
                    calendar1.set(Calendar.MONTH, month)
                    calendar1.set(Calendar.YEAR, year)
                    calendar1.set(Calendar.DAY_OF_MONTH, 1)
                    calendar1.add(Calendar.MONTH, 1)
                    val month1: Int = calendar1.get(Calendar.MONTH)
                    val year1: Int = calendar1.get(Calendar.YEAR)
                    val monthYear = String.format("%02d", month1) + String.format("%04d", year1)
                    val status = monthList.contains(monthYear)
                    if (status == false) {
                        monthList.addLast(monthYear)
                        monthPagerAdapter.monthList = monthList
                        monthPagerAdapter.notifyDataSetChanged()
                        pager1.setCurrentItem(monthList.indexOf(date), true)
                    }
                }
                lastPage = position

            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }


    fun setActionBarTitle(title: String) {
        supportActionBar?.setTitle(title)

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }

        if (getSupportFragmentManager().getBackStackEntryCount() == 1 || getSupportFragmentManager().getBackStackEntryCount() > 0) {
            supportFragmentManager.popBackStackImmediate()
            finish()
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        menu.findItem(R.id.jump_to_date).setVisible(true);
        menu.findItem(R.id.jump_to_date).setVisible(true);
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.calculate_date -> {
                val intent = Intent(this@MainActivity, CalculateDateActivity::class.java)
                intent.putExtra("startDate", format.format(date))
                startActivityForResult(intent, 100)
                return true
            }
            R.id.jump_to_date -> {
                val mDateSetListener = DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                    var jumpMonth = month
                    jumpMonth = jumpMonth + 1
                    val date = "$day/$jumpMonth/$year"
                    val sdf = SimpleDateFormat("dd/MM/yyyy")
                    val date1 = sdf.parse(date)

                    request=1
                    generateMonthView(date1)
                    drawerClose("month")

                }
                val cal = Calendar.getInstance()
                val year = cal.get(Calendar.YEAR)
                val month = cal.get(Calendar.MONTH)
                val day = cal.get(Calendar.DAY_OF_MONTH)
                val dialog = DatePickerDialog(
                    this@MainActivity,
                    AlertDialog.THEME_HOLO_LIGHT,
                    mDateSetListener,
                    year, month, day
                )
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                colorizeDatePicker(dialog.getDatePicker())
                dialog.show()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun colorizeDatePicker(datePicker: DatePicker) {
        val system: Resources = Resources.getSystem()
        val dayId = system.getIdentifier("day", "id", "android")
        val monthId = system.getIdentifier("month", "id", "android")
        val yearId = system.getIdentifier("year", "id", "android")
        val dayPicker: NumberPicker = datePicker.findViewById(dayId)
        val monthPicker: NumberPicker = datePicker.findViewById(monthId)
        val yearPicker: NumberPicker = datePicker.findViewById(yearId)
        setDividerColor(dayPicker)
        setDividerColor(monthPicker)
        setDividerColor(yearPicker)
    }

    fun setDividerColor(picker: NumberPicker) {
        val count = picker.getChildCount()
        for (i in 0..count) {
            try {
                val dividerField: Field = picker.javaClass.getDeclaredField("mSelectionDivider")
                dividerField.isAccessible = true
                val colorDrawable = ColorDrawable(picker.getResources().getColor(R.color.colorPrimary))
                dividerField.set(picker, colorDrawable)
                picker.invalidate()
            } catch (e: Exception) {
                Log.w("setDividerColor", e)
            }
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        resetAllMenuItemsTextColor(nav_view)
        setTextColorForMenuItem(item, R.color.colorPrimary)
        when (item.itemId) {
            R.id.nav_day_mode -> {
                request = 3
                lastSelection = "day"
                getDayList()
                drawerClose("day")
            }
            R.id.nav_Month_mode -> {
                request = 1
                lastSelection = "month"
                getMonthList()
                drawerClose("month")

            }
            R.id.nav_Week_mode -> {
                request = 2
                lastSelection = "week"
                getWeekList()
                drawerClose("week")

            }
        }
        return true
    }

    fun getScreenWidthInDPs(context: Context): Int {
        val dm = DisplayMetrics()
        val windowManager: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.getDefaultDisplay().getMetrics(dm)
        return dm.widthPixels
    }

    fun getScreenHeightInDPs(context: Context): Int {
        val dm = DisplayMetrics()
        val windowManager: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.getDefaultDisplay().getMetrics(dm)
        return dm.heightPixels
    }

    @SuppressLint("NewApi")
    private fun setTextColorForMenuItem(menuItem: MenuItem, @ColorRes color: Int) {
        val spanString = SpannableString(menuItem.title.toString())
        spanString.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, color)), 0, spanString.length, 0)
        menuItem.title = spanString
        var drawable: Drawable = menuItem.icon
        drawable = DrawableCompat.wrap(drawable)
        var colourValue = Color.parseColor("#006cff")
        DrawableCompat.setTint(drawable.mutate(), colourValue)
        menuItem.icon.setTint(colourValue)
        setNavMenuItemThemeColors(colourValue)
    }

    private fun resetAllMenuItemsTextColor(navigationView: NavigationView) {
        for (i in 0..navigationView.getMenu().size() - 1) {
            setTextColorForMenuItem(navigationView.getMenu().getItem(i), R.color.deselect_item)
        }
    }

    fun setNavMenuItemThemeColors(color: Int) {
        //Setting default colors for menu item Text and Icon
        val navDefaultTextColor = Color.parseColor("#202020")
        val navDefaultIconColor = Color.parseColor("#737373")

        //Defining ColorStateList for menu item Text
        val navMenuTextList = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked),
                intArrayOf(android.R.attr.state_enabled),
                intArrayOf(android.R.attr.state_pressed),
                intArrayOf(android.R.attr.state_focused),
                intArrayOf(android.R.attr.state_pressed)
            ),
            intArrayOf(color, navDefaultTextColor, navDefaultTextColor, navDefaultTextColor, navDefaultTextColor)
        )

        //Defining ColorStateList for menu item Icon
        val navMenuIconList = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked),
                intArrayOf(android.R.attr.state_enabled),
                intArrayOf(android.R.attr.state_pressed),
                intArrayOf(android.R.attr.state_focused),
                intArrayOf(android.R.attr.state_pressed)
            ),
            intArrayOf(color, navDefaultIconColor, navDefaultIconColor, navDefaultIconColor, navDefaultIconColor)
        )

        nav_view.setItemTextColor(navMenuTextList)
        nav_view.setItemIconTintList(navMenuIconList)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                val bundle = data!!.getBundleExtra("bundle");
                val sdf = SimpleDateFormat("dd MMMM, yyyy")

                val date = sdf.parse(bundle.getString("endDate"))
                request=1
                generateMonthView(date)
                drawerClose("month")

            }
        }
        if (requestCode == 1000) {
            val dbm = DataBaseManager(this)
            if (resultCode == Activity.RESULT_OK) {
                val bundle = data!!.getBundleExtra("bundle")

                val result = bundle.getString("result")
                val eventId = bundle.getString("eventId")
                val event = dbm.getEvent(eventId!!)
                if (result!!.equals("true")) {
                    showSnackBar(event!!, getString(R.string.EventAddedMessage), getString(R.string.ActionView))
                }

            }
        }
    }

    fun showSnackBar(event: Event, message: String, action: String) {
        val mySnackbar = Snackbar.make(
            findViewById(R.id.coordinator_layout),
            message, Snackbar.LENGTH_LONG
        )
        mySnackbar.setAction(action, View.OnClickListener {
            if (action.equals(getString(R.string.ActionView))) {
                val i = Intent(this, ViewEvent::class.java)
                i.putExtra(getString(R.string.EventID), event.eventId)
                startActivity(i)
            }
        })
        mySnackbar.addCallback(object : Snackbar.Callback() {
            override fun onDismissed(snackbar: Snackbar, event: Int) {
                fabLayout.visibility = View.VISIBLE
            }

            override fun onShown(snackbar: Snackbar) {
                fabLayout.visibility = View.INVISIBLE
            }
        })
        val navDefaultTextColor = Color.parseColor("#006cff")
        mySnackbar.setActionTextColor(navDefaultTextColor)
        mySnackbar.show()
    }

    fun generateMonthView(date: Date) {
        val monthList111 = LinkedList<String>()
        val calendar: Calendar = Calendar.getInstance()
        for (i in -100..100) {
            calendar.setTime(date)
            calendar.add(Calendar.MONTH, i)
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            val month1: Int = calendar.get(Calendar.MONTH)
            val year1: Int = calendar.get(Calendar.YEAR)
            val monthYear = String.format("%02d", month1) + String.format("%04d", year1)
            monthList111.add(monthYear)
        }
        monthPagerAdapter.monthList.removeAll(monthPagerAdapter.monthList)
        monthPagerAdapter.monthList.addAll(monthList111)
        monthPagerAdapter.notifyDataSetChanged()
    }

    fun getCurrentMonthView() {
        nav_view.getMenu().getItem(0).setChecked(true);
        pager1.adapter = monthPagerAdapter
        pager1.setCurrentItem(100, true)
        var lastPage = 100
        pager1.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            var calendar1: Calendar = Calendar.getInstance()
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                if (lastPage >= position) {
                    val date = monthList.get(position)
                    val month = Integer.parseInt(date.substring(0, 2))
                    val year = Integer.parseInt(date.substring(2, 6))
                    calendar1.set(Calendar.MONTH, month)
                    calendar1.set(Calendar.YEAR, year)
                    calendar1.set(Calendar.DAY_OF_MONTH, 1)
                    calendar1.add(Calendar.MONTH, -1)
                    val month1: Int = calendar1.get(Calendar.MONTH)
                    val year1: Int = calendar1.get(Calendar.YEAR)
                    val monthYear = String.format("%02d", month1) + String.format("%04d", year1)
                    val status = monthList.contains(monthYear)
                    if (status == false) {
                        monthList.addFirst(monthYear)
                        monthList.removeLast()
                        monthPagerAdapter.monthList = monthList
                        monthPagerAdapter.notifyDataSetChanged()
                        pager1.setCurrentItem(monthList.indexOf(date), true)
                    }

                } else if (lastPage < position) {
                    val date = monthList.get(position)
                    val month = Integer.parseInt(date.substring(0, 2))
                    val year = Integer.parseInt(date.substring(2, 6))
                    calendar1.set(Calendar.MONTH, month)
                    calendar1.set(Calendar.YEAR, year)
                    calendar1.set(Calendar.DAY_OF_MONTH, 1)
                    calendar1.add(Calendar.MONTH, 1)
                    val month1: Int = calendar1.get(Calendar.MONTH)
                    val year1: Int = calendar1.get(Calendar.YEAR)
                    val monthYear = String.format("%02d", month1) + String.format("%04d", year1)
                    val status = monthList.contains(monthYear)
                    if (status == false) {
                        monthList.addLast(monthYear)
                        monthPagerAdapter.monthList = monthList
                        monthPagerAdapter.notifyDataSetChanged()
                        pager1.setCurrentItem(monthList.indexOf(date), true)
                    }

                }
                lastPage = position

            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }

    fun getCurrentWeekView() {

        nav_view.getMenu().getItem(2).setChecked(true);
        pager1.adapter = weekPagerAdapter

        pager1.setCurrentItem(100, true)
        var lastPage = 100
        pager1.offscreenPageLimit = 2;
        pager1.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            var calendar1: Calendar = Calendar.getInstance()
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                if (lastPage >= position) {//User Move to left
                    val date = weekList.get(position)
                    val day = Integer.parseInt(date.substring(0, 2))
                    val month = Integer.parseInt(date.substring(2, 4))
                    val year = Integer.parseInt(date.substring(4, 8))
                    calendar1.set(Calendar.DAY_OF_MONTH, day)
                    calendar1.set(Calendar.MONTH, month)
                    calendar1.set(Calendar.YEAR, year)
                    calendar1.add(Calendar.DAY_OF_MONTH, -7)
                    val day1: Int = calendar1.get(Calendar.DAY_OF_MONTH)
                    val month1: Int = calendar1.get(Calendar.MONTH)
                    val year1: Int = calendar1.get(Calendar.YEAR)
                    val monthYear =
                        String.format("%02d", day1) + String.format("%02d", month1) + String.format(
                            "%04d",
                            year1
                        )

                    val status = weekList.contains(monthYear)
                    if (status == false) {
                        weekList.addFirst(monthYear)
                        weekPagerAdapter.monthList = weekList
                        weekPagerAdapter.notifyDataSetChanged()
                        pager1.setCurrentItem(weekList.indexOf(date), true)
                    }

                } else if (lastPage < position) {
                    val date = weekList.get(position)
                    val day = Integer.parseInt(date.substring(0, 2))
                    val month = Integer.parseInt(date.substring(2, 4))
                    val year = Integer.parseInt(date.substring(4, 8))
                    calendar1.set(Calendar.DAY_OF_MONTH, day)
                    calendar1.set(Calendar.MONTH, month)
                    calendar1.set(Calendar.YEAR, year)
                    calendar1.add(Calendar.DAY_OF_MONTH, 7)
                    val day1: Int = calendar1.get(Calendar.DAY_OF_MONTH)
                    val month1: Int = calendar1.get(Calendar.MONTH)
                    val year1: Int = calendar1.get(Calendar.YEAR)
                    val monthYear =
                        String.format("%02d", day1) + String.format("%02d", month1) + String.format(
                            "%04d",
                            year1
                        )
                    val status = weekList.contains(monthYear)
                    if (status == false) {
                        weekList.addLast(monthYear)
                        weekPagerAdapter.monthList = weekList
                        weekPagerAdapter.notifyDataSetChanged()
                        pager1.setCurrentItem(weekList.indexOf(date), true)
                    }

                }
                lastPage = position

            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }

    fun getCurrentDayView() {
        dayPagerAdapter =
            DayPagerAdapter(supportFragmentManager, applicationContext,dayList)

        nav_view.getMenu().getItem(1).setChecked(true)
        pager1.adapter = dayPagerAdapter

        pager1.setCurrentItem(100, true)
        var lastPage = 100
        pager1.setOffscreenPageLimit(2)
        pager1.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            var calendar1: Calendar = Calendar.getInstance()
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                if (lastPage >= position) {//User Move to left
                    val date = dayList.get(position)
                    val day = Integer.parseInt(date.substring(0, 2))
                    val month = Integer.parseInt(date.substring(2, 4))
                    val year = Integer.parseInt(date.substring(4, 8))
                    calendar1.set(Calendar.DAY_OF_MONTH, day)
                    calendar1.set(Calendar.MONTH, month)
                    calendar1.set(Calendar.YEAR, year)
                    calendar1.add(Calendar.DAY_OF_MONTH, -1)
                    val day1: Int = calendar1.get(Calendar.DAY_OF_MONTH)
                    val month1: Int = calendar1.get(Calendar.MONTH)
                    val year1: Int = calendar1.get(Calendar.YEAR)
                    val monthYear =
                        String.format("%02d", day1) + String.format("%02d", month1) + String.format(
                            "%04d",
                            year1
                        )
                    val status = dayList.contains(monthYear)
                    if (status == false) {
                        dayList.addFirst(monthYear)
                        dayPagerAdapter.monthList = dayList
                        dayPagerAdapter.notifyDataSetChanged()
                        pager1.setCurrentItem(dayList.indexOf(date), true)
                    }

                } else if (lastPage < position) {
                    val date = dayList.get(position)
                    val day = Integer.parseInt(date.substring(0, 2))
                    val month = Integer.parseInt(date.substring(2, 4))
                    val year = Integer.parseInt(date.substring(4, 8))
                    calendar1.set(Calendar.DAY_OF_MONTH, day)
                    calendar1.set(Calendar.MONTH, month)
                    calendar1.set(Calendar.YEAR, year)
                    calendar1.add(Calendar.DAY_OF_MONTH, 1)
                    val day1: Int = calendar1.get(Calendar.DAY_OF_MONTH)
                    val month1: Int = calendar1.get(Calendar.MONTH)
                    val year1: Int = calendar1.get(Calendar.YEAR)
                    val monthYear =
                        String.format("%02d", day1) + String.format("%02d", month1) + String.format(
                            "%04d",
                            year1
                        )

                    val status = dayList.contains(monthYear)
                    if (status == false) {
                        dayList.addLast(monthYear)
                        dayPagerAdapter.monthList = dayList
                        dayPagerAdapter.notifyDataSetChanged()
                        pager1.setCurrentItem(dayList.indexOf(date), true)
                    }

                }
                lastPage = position

            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }

    fun setDateCommon(date: Date) {
        this.date = date
    }

    private fun createchannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val mChannel = NotificationChannel(
                channel_id,
                getString(R.string.channel_name), //name of the channel
                NotificationManager.IMPORTANCE_DEFAULT
            )   //importance level
            //important level: default is is high on the phone.  high is urgent on the phone.  low is medium, so none is low?
            // Configure the notification channel.
            mChannel.description = getString(R.string.channel_description)
            mChannel.enableLights(true)
            // Sets the notification light color for notifications posted to this channel, if the device supports this feature.
            mChannel.lightColor = Color.RED
            mChannel.enableVibration(true)
            mChannel.setShowBadge(true)
            mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            nm.createNotificationChannel(mChannel)

        }
    }

    fun drawerClose(item: String) {
        drawer_layout.closeDrawer(GravityCompat.START)
        drawer_layout.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {

            override fun onDrawerClosed(view: View) {
                when (item) {
                    "day" -> {

                        if (request == 3) {
                            getDayList()
                            pager1.adapter = dayPagerAdapter
                            //
                            // val monthList11 = LinkedList<String>()
                            nav_view.getMenu().getItem(1).setChecked(true);


                           /* for (i in -100..100) {
                                calendar.setTime(date)
                                calendar.add(Calendar.db.DAY_OF_MONTH, i)
                                val day1: Int = calendar.get(Calendar.db.DAY_OF_MONTH)
                                val month1: Int = calendar.get(Calendar.db.MONTH)
                                val year1: Int = calendar.get(Calendar.db.YEAR)
                                val dayMonthYear =
                                    String.format("%02d", day1) + String.format("%02d", month1) + String.format(
                                        "%04d",
                                        year1
                                    )
                                monthList11.add(dayMonthYear)
                            }

                            dayPagerAdapter = DayPagerAdapter(supportFragmentManager, applicationContext, dayList)*/


                            pager1.setCurrentItem(100, true)
                            var lastPage = 100
                            pager1.setOffscreenPageLimit(2);
                            pager1.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                                var calendar1: Calendar = Calendar.getInstance()
                                override fun onPageScrolled(
                                    position: Int,
                                    positionOffset: Float,
                                    positionOffsetPixels: Int
                                ) {
                                }

                                override fun onPageSelected(position: Int) {
                                    var fm=dayPagerAdapter.getItem(pager1.currentItem)
                                    if(fm is DayFragment){
//                                      setDateCommon(fm.dayDate)
                                    }

                                    if (lastPage >= position) {//User Move to left
                                        val date = dayList.get(position)
                                        val day = Integer.parseInt(date.substring(0, 2))
                                        val month = Integer.parseInt(date.substring(2, 4))
                                        val year = Integer.parseInt(date.substring(4, 8))
                                        calendar1.set(Calendar.DAY_OF_MONTH, day)
                                        calendar1.set(Calendar.MONTH, month)
                                        calendar1.set(Calendar.YEAR, year)
                                        calendar1.add(Calendar.DAY_OF_MONTH, -1)
                                        val day1: Int = calendar1.get(Calendar.DAY_OF_MONTH)
                                        val month1: Int = calendar1.get(Calendar.MONTH)
                                        val year1: Int = calendar1.get(Calendar.YEAR)
                                        val monthYear =
                                            String.format("%02d", day1) + String.format("%02d", month1) + String.format(
                                                "%04d",
                                                year1
                                            )
                                        val status = dayList.contains(monthYear)
                                        if (status == false) {
                                            dayList.addFirst(monthYear)
                                            dayPagerAdapter.monthList = dayList
                                            dayPagerAdapter.notifyDataSetChanged()
                                            pager1.setCurrentItem(dayList.indexOf(date), true)
                                        }

                                    } else if (lastPage < position) {
                                        val date = dayList.get(position)
                                        val day = Integer.parseInt(date.substring(0, 2))
                                        val month = Integer.parseInt(date.substring(2, 4))
                                        val year = Integer.parseInt(date.substring(4, 8))
                                        calendar1.set(Calendar.DAY_OF_MONTH, day)
                                        calendar1.set(Calendar.MONTH, month)
                                        calendar1.set(Calendar.YEAR, year)
                                        calendar1.add(Calendar.DAY_OF_MONTH, 1)
                                        val day1: Int = calendar1.get(Calendar.DAY_OF_MONTH)
                                        val month1: Int = calendar1.get(Calendar.MONTH)
                                        val year1: Int = calendar1.get(Calendar.YEAR)
                                        val monthYear =
                                            String.format("%02d", day1) + String.format("%02d", month1) + String.format(
                                                "%04d",
                                                year1
                                            )

                                        val status = dayList.contains(monthYear)
                                        if (status == false) {
                                            dayList.addLast(monthYear)
                                            dayPagerAdapter.monthList = dayList
                                            dayPagerAdapter.notifyDataSetChanged()
                                            pager1.setCurrentItem(dayList.indexOf(date), true)
                                        }

                                    }
                                    lastPage = position
                                }

                                override fun onPageScrollStateChanged(state: Int) {
                                }
                            })
                        }
                    }
                    "month" -> {
                        if (request == 1) {
                            getMonthList()
                            pager1.adapter = monthPagerAdapter
                            nav_view.getMenu().getItem(0).setChecked(true);


                           /* val calendar: Calendar.db = Calendar.db.getInstance()
                            for (i in -100..100) {
                                calendar.setTime(date)
                                calendar.add(Calendar.db.MONTH, i)
                                calendar.set(Calendar.db.DAY_OF_MONTH, 1)
                                val month1: Int = calendar.get(Calendar.db.MONTH)
                                val year1: Int = calendar.get(Calendar.db.YEAR)
                                val monthYear = String.format("%02d", month1) + String.format("%04d", year1)
                                monthList111.add(monthYear)
                            }

                            monthPagerAdapter =
                                MonthPageAdapter(supportFragmentManager, applicationContext, monthList111)*/


                            pager1.setCurrentItem(100, true)
                            var lastPage = 100
                            pager1.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                                var calendar1: Calendar = Calendar.getInstance()
                                override fun onPageScrolled(
                                    position: Int,
                                    positionOffset: Float,
                                    positionOffsetPixels: Int
                                ) {
                                }

                                override fun onPageSelected(position: Int) {
                                    var fm= monthPagerAdapter.getItem(pager1.currentItem)
                                    if(fm is MonthFragment){
//                                        setDateCommon(fm.datePassed!!)
                                    }
                                    if (lastPage >= position) {
                                        val date = monthList.get(position)
                                        val month = Integer.parseInt(date.substring(0, 2))
                                        val year = Integer.parseInt(date.substring(2, 6))
                                        calendar1.set(Calendar.MONTH, month)
                                        calendar1.set(Calendar.YEAR, year)
                                        calendar1.set(Calendar.DAY_OF_MONTH, 1)
                                        calendar1.add(Calendar.MONTH, -1)
                                        val month1: Int = calendar1.get(Calendar.MONTH)
                                        val year1: Int = calendar1.get(Calendar.YEAR)
                                        val monthYear = String.format("%02d", month1) + String.format("%04d", year1)
                                        val status = monthList.contains(monthYear)
                                        if (status == false) {
                                            monthList.addFirst(monthYear)
                                            monthList.removeLast()
                                            monthPagerAdapter.monthList = monthList
                                            monthPagerAdapter.notifyDataSetChanged()
                                            pager1.setCurrentItem(monthList.indexOf(date), true)
                                        }

                                    } else if (lastPage < position) {
                                        val date = monthList.get(position)
                                        val month = Integer.parseInt(date.substring(0, 2))
                                        val year = Integer.parseInt(date.substring(2, 6))
                                        calendar1.set(Calendar.MONTH, month)
                                        calendar1.set(Calendar.YEAR, year)
                                        calendar1.set(Calendar.DAY_OF_MONTH, 1)
                                        calendar1.add(Calendar.MONTH, 1)
                                        val month1: Int = calendar1.get(Calendar.MONTH)
                                        val year1: Int = calendar1.get(Calendar.YEAR)
                                        val monthYear = String.format("%02d", month1) + String.format("%04d", year1)
                                        val status = monthList.contains(monthYear)
                                        if (status == false) {
                                            monthList.addLast(monthYear)
                                            monthPagerAdapter.monthList = monthList
                                            monthPagerAdapter.notifyDataSetChanged()
                                            pager1.setCurrentItem(monthList.indexOf(date), true)
                                        }
                                    }
                                    lastPage = position

                                }

                                override fun onPageScrollStateChanged(state: Int) {
                                }
                            })
                        }
                    }
                    "week" -> {
                        if (request == 2) {
                            getWeekList()
                            nav_view.getMenu().getItem(2).isChecked = true
                            pager1.adapter = weekPagerAdapter
                            pager1.setCurrentItem(100, true)
                            var lastPage = 100
                            pager1.offscreenPageLimit = 2
                            pager1.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                                var calendar1: Calendar = Calendar.getInstance()
                                override fun onPageScrolled(
                                    position: Int,
                                    positionOffset: Float,
                                    positionOffsetPixels: Int
                                ) {
                                }

                                override fun onPageSelected(position: Int) {
                                    var fm= weekPagerAdapter.getItem(pager1.currentItem)
                                    if(fm is WeekFragment){
                                        setDateCommon(fm.dateWeek)
                                    }
                                    if (lastPage >= position) {//User Move to left
                                        val date = weekList.get(position)
                                        val day = Integer.parseInt(date.substring(0, 2))
                                        val month = Integer.parseInt(date.substring(2, 4))
                                        val year = Integer.parseInt(date.substring(4, 8))
                                        calendar1.set(Calendar.DAY_OF_MONTH, day)
                                        calendar1.set(Calendar.MONTH, month)
                                        calendar1.set(Calendar.YEAR, year)
                                        calendar1.add(Calendar.DAY_OF_MONTH, -7)
                                        val day1: Int = calendar1.get(Calendar.DAY_OF_MONTH)
                                        val month1: Int = calendar1.get(Calendar.MONTH)
                                        val year1: Int = calendar1.get(Calendar.YEAR)
                                        val monthYear =
                                            String.format("%02d", day1) + String.format("%02d", month1) + String.format(
                                                "%04d",
                                                year1
                                            )

                                        val status = weekList.contains(monthYear)
                                        if (status == false) {
                                            weekList.addFirst(monthYear)
                                            weekPagerAdapter.monthList = weekList
                                            weekPagerAdapter.notifyDataSetChanged()
                                            pager1.setCurrentItem(weekList.indexOf(date), true)
                                        }

                                    } else if (lastPage < position) {
                                        val date = weekList.get(position)
                                        val day = Integer.parseInt(date.substring(0, 2))
                                        val month = Integer.parseInt(date.substring(2, 4))
                                        val year = Integer.parseInt(date.substring(4, 8))
                                        calendar1.set(Calendar.DAY_OF_MONTH, day)
                                        calendar1.set(Calendar.MONTH, month)
                                        calendar1.set(Calendar.YEAR, year)
                                        calendar1.add(Calendar.DAY_OF_MONTH, 7)
                                        val day1: Int = calendar1.get(Calendar.DAY_OF_MONTH)
                                        val month1: Int = calendar1.get(Calendar.MONTH)
                                        val year1: Int = calendar1.get(Calendar.YEAR)
                                        val monthYear =
                                            String.format("%02d", day1) + String.format("%02d", month1) + String.format(
                                                "%04d",
                                                year1
                                            )
                                        val status = weekList.contains(monthYear)
                                        if (!status) {
                                            weekList.addLast(monthYear)
                                            weekPagerAdapter.monthList = weekList
                                            weekPagerAdapter.notifyDataSetChanged()
                                            pager1.setCurrentItem(weekList.indexOf(date), true)
                                        }

                                    }
                                    lastPage = position

                                }

                                override fun onPageScrollStateChanged(state: Int) {
                                }
                            })
                        }
                    }
                }

            }
        })

    }

    fun getDayList() {
        dayList.removeAll(dayList)
        for (i in -100..100) {
            val calendar: Calendar = Calendar.getInstance()
            calendar.setTime(date)
            calendar.add(Calendar.DAY_OF_MONTH, i)
            val day1: Int = calendar.get(Calendar.DAY_OF_MONTH)
            val month1: Int = calendar.get(Calendar.MONTH)
            val year1: Int = calendar.get(Calendar.YEAR)
            val dayMonthYear =
                String.format("%02d", day1) + String.format("%02d", month1) + String.format("%04d", year1)
            dayList.add(dayMonthYear)
        }

        dayPagerAdapter = DayPagerAdapter(supportFragmentManager, this, dayList)


    }

    fun getWeekList() {
        weekList.removeAll(weekList)
        for (i in -100..100) {
            val calendar: Calendar = Calendar.getInstance()
            calendar.setTime(date)
            calendar.add(Calendar.DAY_OF_MONTH, i * 7)
            val day1: Int = calendar.get(Calendar.DAY_OF_MONTH)
            val month1: Int = calendar.get(Calendar.MONTH)
            val year1: Int = calendar.get(Calendar.YEAR)
            val dayMonthYear =
                String.format("%02d", day1) + String.format("%02d", month1) + String.format("%04d", year1)
            weekList.add(dayMonthYear)
        }
        weekPagerAdapter = WeekPagerAdapter(supportFragmentManager, this, weekList)

    }


    fun getMonthList() {
        monthList.removeAll(monthList)
        val calendar: Calendar = Calendar.getInstance()
        for (i in -100..100) {
            calendar.setTime(date)
            calendar.add(Calendar.MONTH, i)
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            val month1: Int = calendar.get(Calendar.MONTH)
            val year1: Int = calendar.get(Calendar.YEAR)
            val monthYear = String.format("%02d", month1) + String.format("%04d", year1)
            monthList.add(monthYear)
        }
       monthPagerAdapter = MonthPageAdapter(supportFragmentManager, this, monthList)

    }

}


