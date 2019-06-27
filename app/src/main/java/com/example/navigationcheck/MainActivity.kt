package com.example.navigationcheck

import android.annotation.SuppressLint
import android.app.*
import android.app.PendingIntent.getActivity
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
import com.example.navigationcheck.dataBase.DataBaseManager
import com.example.navigationcheck.adapter.MonthPageAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import java.util.*
import android.content.res.ColorStateList
import android.content.res.Resources
import android.os.AsyncTask
import android.os.Build
import androidx.core.content.ContextCompat
import android.text.style.ForegroundColorSpan
import android.text.SpannableString
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.ColorRes
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.navigationcheck.adapter.DayPagerAdapter
import com.example.navigationcheck.adapter.WeekPagerAdapter
import com.example.navigationcheck.entity.Event
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.app_bar_main.fab
import kotlinx.android.synthetic.main.nav_header_main.*
import java.lang.reflect.Field
import java.net.URL
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

lateinit var monthPagerAdapter: MonthPageAdapter
lateinit var dayPagerAdapter: DayPagerAdapter
lateinit var weekPagerAdapter: WeekPagerAdapter
var screenWidthCommon = 0
var screenHeightCommon = 0
lateinit var pager1: ViewPager
lateinit var dbm: DataBaseManager
var request = 1

var monthList1 = LinkedList<String>()
var monthList11 = LinkedList<String>()
var monthList111 = LinkedList<String>()

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var btn: Button
    var id = "test_channel_01"
    var date: Date = Date()
    var year1: Int = 0
    var day: Int = 0
    var month: Int = 0
    lateinit var bdh: TextView


    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dbm = DataBaseManager(this)
        setSupportActionBar(toolbar)
        createchannel()
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        MyJobIntentService.enqueueWork(this, intent)
        val context: Context? = applicationContext
        screenWidthCommon = getScreenWidthInDPs(context!!)
        screenHeightCommon = getScreenHeightInDPs(context)
        bdh = findViewById(R.id.bottom_date_holder)
        fab.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View): Unit {
                // Handler code here.
                val intent = Intent(this@MainActivity, AddEvent::class.java);
                intent.putExtra("startDate", eventStartTime[0])
                intent.putExtra("startTime", eventStartTime[1])
                intent.putExtra("endDate", eventEndTime[0])
                intent.putExtra("endTime", eventEndTime[1])

                startActivityForResult(intent, 1000);
            }
        })


        toolbar.setTitleTextColor(Color.WHITE);
        nav_view.setNavigationItemSelectedListener(this)
        pager1 = findViewById<ViewPager>(R.id.pager1)
        pager1.offscreenPageLimit = 2
        /*var navigationView: NavigationView = findViewById(R.id.nav_view);
          var headerLayout=findViewById<LinearLayout>(R.id.nav_header)
          headerLayout.setOnClickListener{

          }
        var headerView: View = navigationView.getHeaderView(0);
         var navUsername: TextView = headerView.findViewById(R.id.name);
         navUsername.setText("Shubham kumar")
         var navUserMail: TextView = headerView.findViewById(R.id.mail_id)
         navUserMail.setText("shubhamkumar2b1@gmail.com");*/

        for (i in -100..100) {
            calendar.setTime(date)
            calendar.add(Calendar.DAY_OF_MONTH, i)
            var day1: Int = calendar.get(Calendar.DAY_OF_MONTH)
            var month1: Int = calendar.get(Calendar.MONTH)
            var year1: Int = calendar.get(Calendar.YEAR)
            var dayMonthYear =
                String.format("%02d", day1) + String.format("%02d", month1) + String.format("%04d", year1)
            monthList1.add(dayMonthYear)
        }

        dayPagerAdapter = DayPagerAdapter(supportFragmentManager, this, monthList1)


        var calendar: Calendar = Calendar.getInstance()
        for (i in -100..100) {
            calendar.setTime(date)
            calendar.add(Calendar.MONTH, i)
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            var month1: Int = calendar.get(Calendar.MONTH)
            var year1: Int = calendar.get(Calendar.YEAR)
            var monthYear = String.format("%02d", month1) + String.format("%04d", year1)
            monthList111.add(monthYear)
        }

        monthPagerAdapter = MonthPageAdapter(supportFragmentManager, this, monthList111)

        for (i in -100..100) {
            calendar.setTime(date)
            calendar.add(Calendar.DAY_OF_MONTH, i * 7)
            var day1: Int = calendar.get(Calendar.DAY_OF_MONTH)
            var month1: Int = calendar.get(Calendar.MONTH)
            var year1: Int = calendar.get(Calendar.YEAR)
            var dayMonthYear =
                String.format("%02d", day1) + String.format("%02d", month1) + String.format("%04d", year1)
            monthList11.add(dayMonthYear)
        }
        weekPagerAdapter = WeekPagerAdapter(supportFragmentManager, this, monthList11)



        resetAllMenuItemsTextColor(nav_view);
        nav_view.getMenu().getItem(0).setChecked(true);

        pager1.adapter = monthPagerAdapter

        pager1.setCurrentItem(100, true)
        var lastPage = 100
        pager1?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {


            private var thresholdOffset = 0.5f
            private var scrollStarted: Boolean = false
            private var checkDirection: Boolean = false
            var calendar1: Calendar = Calendar.getInstance()
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }


            override fun onPageSelected(position: Int) {
                if (lastPage >= position) {
                    var date = monthList111.get(position)
                    var month = Integer.parseInt(date.substring(0, 2))
                    var year = Integer.parseInt(date.substring(2, 6))
                    calendar1.set(Calendar.MONTH, month)
                    calendar1.set(Calendar.YEAR, year)
                    calendar1.set(Calendar.DAY_OF_MONTH, 1)
                    calendar1.add(Calendar.MONTH, -1)
                    var month1: Int = calendar1.get(Calendar.MONTH)
                    var year1: Int = calendar1.get(Calendar.YEAR)
                    var monthYear = String.format("%02d", month1) + String.format("%04d", year1)
                    var status = monthList111.contains(monthYear)
                    if (status == false) {
                        monthList111.addFirst(monthYear)
                        monthList111.removeLast()
                        monthPagerAdapter.monthList = monthList111
                        monthPagerAdapter.notifyDataSetChanged()
                        pager1.setCurrentItem(monthList111.indexOf(date), true)
                    }

                } else if (lastPage < position) {
                    var date = monthList111.get(position)
                    var month = Integer.parseInt(date.substring(0, 2))
                    var year = Integer.parseInt(date.substring(2, 6))
                    calendar1.set(Calendar.MONTH, month)
                    calendar1.set(Calendar.YEAR, year)
                    calendar1.set(Calendar.DAY_OF_MONTH, 1)
                    calendar1.add(Calendar.MONTH, 1)
                    var month1: Int = calendar1.get(Calendar.MONTH)
                    var year1: Int = calendar1.get(Calendar.YEAR)
                    var monthYear = String.format("%02d", month1) + String.format("%04d", year1)
                    var status = monthList111.contains(monthYear)
                    if (status == false) {
                        monthList111.addLast(monthYear)
                        monthPagerAdapter.monthList = monthList111
                        monthPagerAdapter.notifyDataSetChanged()
                        pager1.setCurrentItem(monthList111.indexOf(date), true)
                    }

                }
                lastPage = position

            }

            override fun onPageScrollStateChanged(state: Int) {
                if (!scrollStarted && state == ViewPager.SCROLL_STATE_DRAGGING) {
                    scrollStarted = true;
                    checkDirection = true;
                } else {
                    scrollStarted = false;
                }
            }
        })
    }


    fun setActionBarTitle(title: String) {
        supportActionBar?.setTitle(title)

    }

    fun setActionBarContents() {
        supportActionBar?.setHomeButtonEnabled(true)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }

        if (getSupportFragmentManager().getBackStackEntryCount() == 1 || getSupportFragmentManager().getBackStackEntryCount() > 0) {
            supportFragmentManager.popBackStackImmediate();
            finish();
        } else {
            super.onBackPressed();
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.calculate_date -> {
                val intent = Intent(this@MainActivity, CalculateDateActivity::class.java);
                intent.putExtra("startDate", format.format(date))
                startActivityForResult(intent, 100);
                return true
            }
            R.id.jump_to_date -> {
                var mDateSetListener = DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                    var month = month
                    month = month + 1
                    println("onDateSet: dd/MM/yyy: $day/$month/$year")

                    val date = "$day/$month/$year"
                    println("date choosen: " + date)

                    val sdf = SimpleDateFormat("dd/MM/yyyy")

                    var date1 = sdf.parse(date)
                    generateMonthView(date1)

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
                colorizeDatePicker(dialog.getDatePicker());
                dialog.show()


                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun colorizeDatePicker(datePicker: DatePicker) {
        var system: Resources = Resources.getSystem();
        var dayId = system.getIdentifier("day", "id", "android");
        var monthId = system.getIdentifier("month", "id", "android");
        var yearId = system.getIdentifier("year", "id", "android");

        var dayPicker: NumberPicker = datePicker.findViewById(dayId);
        var monthPicker: NumberPicker = datePicker.findViewById(monthId);
        var yearPicker: NumberPicker = datePicker.findViewById(yearId);

        setDividerColor(dayPicker);
        setDividerColor(monthPicker);
        setDividerColor(yearPicker);
    }

    fun setDividerColor(picker: NumberPicker) {
        if (picker == null)
            return;

        val count = picker.getChildCount();
        for (i in 0..count) {
            try {
                var dividerField: Field = picker.javaClass.getDeclaredField("mSelectionDivider");
                dividerField.setAccessible(true);
                var colorDrawable: ColorDrawable = ColorDrawable(picker.getResources().getColor(R.color.colorPrimary));
                dividerField.set(picker, colorDrawable);
                picker.invalidate();
            } catch (e: Exception) {
                Log.w("setDividerColor", e);
            }
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        resetAllMenuItemsTextColor(nav_view);
        setTextColorForMenuItem(item, R.color.colorPrimary, R.color.colorPrimary);
        when (item.itemId) {
            R.id.nav_day_mode -> {
                request = 3
                drawerClose("day")
            }
            R.id.nav_Month_mode -> {
                request = 1
                drawerClose("month")
            }
            R.id.nav_Week_mode -> {
                request = 2
                drawerClose("week")
            }
        }


        return true
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

    fun setColour(view: View) {
        var gradientDrawable: StateListDrawable = view.getBackground() as (StateListDrawable)
        var drawableContainerState: DrawableContainer.DrawableContainerState =
            gradientDrawable.getConstantState() as (DrawableContainer.DrawableContainerState)
        var children = drawableContainerState.getChildren()
        var selectedItem: GradientDrawable = children[0] as GradientDrawable
        selectedItem.setColor(Color.BLUE)

    }

    @SuppressLint("NewApi")
    private fun setTextColorForMenuItem(menuItem: MenuItem, @ColorRes color: Int, @ColorRes color1: Int) {
        val spanString = SpannableString(menuItem.title.toString())
        spanString.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, color)), 0, spanString.length, 0)
        menuItem.title = spanString
        var drawable: Drawable = menuItem.icon
        drawable = DrawableCompat.wrap(drawable);
        var colourValue = Color.parseColor("#006cff")
        DrawableCompat.setTint(drawable.mutate(), colourValue);
        menuItem.icon.setTint(colourValue)
        setNavMenuItemThemeColors(colourValue)
    }

    private fun resetAllMenuItemsTextColor(navigationView: NavigationView) {
        for (i in 0..navigationView.getMenu().size() - 1) {
            setTextColorForMenuItem(navigationView.getMenu().getItem(i), R.color.deselect_item, R.color.deselect_item);
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
                var bundle = data!!.getBundleExtra("bundle");
                val sdf = SimpleDateFormat("dd MMMM, yyyy")

                var date = sdf.parse(bundle.getString("endDate"))
                generateMonthView(date)

            }
        }
        if (requestCode == 1000) {
            var dbm = DataBaseManager(this)
            if (resultCode == Activity.RESULT_OK) {
                var bundle = data!!.getBundleExtra("bundle")

                var result = bundle.getString("result")
                var eventId = bundle.getString("eventId")
                var event = dbm.getEvent(eventId)
                if (result.equals("true")) {
                    val mySnackbar = Snackbar.make(
                        findViewById(R.id.drawer_layout),
                        "Event added successfully", Snackbar.LENGTH_LONG
                    )
                    mySnackbar.setAction("View", View.OnClickListener {
                        val i = Intent(this, ViewEvent::class.java)
                        i.putExtra("ID", event!!.eventId)
                        startActivity(i)
                    })
                    val navDefaultTextColor = Color.parseColor("#006cff")
                    mySnackbar.setActionTextColor(navDefaultTextColor)
                    mySnackbar.show()
                }

            }
        }
    }

    fun generateMonthView(date: Date) {
        var monthList111 = LinkedList<String>()
        var calendar: Calendar = Calendar.getInstance()
        for (i in -100..100) {
            calendar.setTime(date)
            calendar.add(Calendar.MONTH, i)
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            var month1: Int = calendar.get(Calendar.MONTH)
            var year1: Int = calendar.get(Calendar.YEAR)
            var monthYear = String.format("%02d", month1) + String.format("%04d", year1)
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
        pager1?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {


            private var thresholdOffset = 0.5f
            private var scrollStarted: Boolean = false
            private var checkDirection: Boolean = false
            var calendar1: Calendar = Calendar.getInstance()
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }


            override fun onPageSelected(position: Int) {
                if (lastPage >= position) {
                    var date = monthList111.get(position)
                    var month = Integer.parseInt(date.substring(0, 2))
                    var year = Integer.parseInt(date.substring(2, 6))
                    calendar1.set(Calendar.MONTH, month)
                    calendar1.set(Calendar.YEAR, year)
                    calendar1.set(Calendar.DAY_OF_MONTH, 1)
                    calendar1.add(Calendar.MONTH, -1)
                    var month1: Int = calendar1.get(Calendar.MONTH)
                    var year1: Int = calendar1.get(Calendar.YEAR)
                    var monthYear = String.format("%02d", month1) + String.format("%04d", year1)
                    var status = monthList111.contains(monthYear)
                    if (status == false) {
                        monthList111.addFirst(monthYear)
                        monthList111.removeLast()
                        monthPagerAdapter.monthList = monthList111
                        monthPagerAdapter.notifyDataSetChanged()
                        pager1.setCurrentItem(monthList111.indexOf(date), true)
                    }

                } else if (lastPage < position) {
                    var date = monthList111.get(position)
                    var month = Integer.parseInt(date.substring(0, 2))
                    var year = Integer.parseInt(date.substring(2, 6))
                    calendar1.set(Calendar.MONTH, month)
                    calendar1.set(Calendar.YEAR, year)
                    calendar1.set(Calendar.DAY_OF_MONTH, 1)
                    calendar1.add(Calendar.MONTH, 1)
                    var month1: Int = calendar1.get(Calendar.MONTH)
                    var year1: Int = calendar1.get(Calendar.YEAR)
                    var monthYear = String.format("%02d", month1) + String.format("%04d", year1)
                    var status = monthList111.contains(monthYear)
                    if (status == false) {
                        monthList111.addLast(monthYear)
                        monthPagerAdapter.monthList = monthList111
                        monthPagerAdapter.notifyDataSetChanged()
                        pager1.setCurrentItem(monthList111.indexOf(date), true)
                    }

                }
                lastPage = position

            }

            override fun onPageScrollStateChanged(state: Int) {
                if (!scrollStarted && state == ViewPager.SCROLL_STATE_DRAGGING) {
                    scrollStarted = true;
                    checkDirection = true;
                } else {
                    scrollStarted = false;
                }
            }
        })
    }

    fun getCurrentWeekView() {

        nav_view.getMenu().getItem(2).setChecked(true);
        pager1.adapter = weekPagerAdapter

        pager1.setCurrentItem(100, true)
        var lastPage = 100
        pager1.setOffscreenPageLimit(2);
        pager1?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            private var thresholdOffset = 0.5f
            private var scrollStarted: Boolean = false
            private var checkDirection: Boolean = false
            var calendar1: Calendar = Calendar.getInstance()
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                if (lastPage >= position) {//User Move to left
                    var date = monthList11.get(position)
                    var day = Integer.parseInt(date.substring(0, 2))
                    var month = Integer.parseInt(date.substring(2, 4))
                    var year = Integer.parseInt(date.substring(4, 8))
                    calendar1.set(Calendar.DAY_OF_MONTH, day)
                    calendar1.set(Calendar.MONTH, month)
                    calendar1.set(Calendar.YEAR, year)
                    calendar1.add(Calendar.DAY_OF_MONTH, -7)
                    var day1: Int = calendar1.get(Calendar.DAY_OF_MONTH)
                    var month1: Int = calendar1.get(Calendar.MONTH)
                    var year1: Int = calendar1.get(Calendar.YEAR)
                    var monthYear =
                        String.format("%02d", day1) + String.format("%02d", month1) + String.format(
                            "%04d",
                            year1
                        )

                    var status = monthList11.contains(monthYear)
                    if (status == false) {
                        monthList11.addFirst(monthYear)
                        weekPagerAdapter.monthList = monthList11
                        weekPagerAdapter.notifyDataSetChanged()
                        pager1.setCurrentItem(monthList11.indexOf(date), true)
                    }

                } else if (lastPage < position) {
                    var date = monthList11.get(position)
                    var day = Integer.parseInt(date.substring(0, 2))
                    var month = Integer.parseInt(date.substring(2, 4))
                    var year = Integer.parseInt(date.substring(4, 8))
                    calendar1.set(Calendar.DAY_OF_MONTH, day)
                    calendar1.set(Calendar.MONTH, month)
                    calendar1.set(Calendar.YEAR, year)
                    calendar1.add(Calendar.DAY_OF_MONTH, 7)
                    var day1: Int = calendar1.get(Calendar.DAY_OF_MONTH)
                    var month1: Int = calendar1.get(Calendar.MONTH)
                    var year1: Int = calendar1.get(Calendar.YEAR)
                    var monthYear =
                        String.format("%02d", day1) + String.format("%02d", month1) + String.format(
                            "%04d",
                            year1
                        )
                    var status = monthList11.contains(monthYear)
                    if (status == false) {
                        monthList11.addLast(monthYear)
                        weekPagerAdapter.monthList = monthList11
                        weekPagerAdapter.notifyDataSetChanged()
                        pager1.setCurrentItem(monthList11.indexOf(date), true)
                    }

                }
                lastPage = position

            }

            override fun onPageScrollStateChanged(state: Int) {
                if (!scrollStarted && state == ViewPager.SCROLL_STATE_DRAGGING) {
                    scrollStarted = true;
                    checkDirection = true;
                } else {
                    scrollStarted = false;
                }
            }
        })
    }

    fun getCurrentDayView() {
        nav_view.getMenu().getItem(1).setChecked(true);
        pager1.adapter = dayPagerAdapter

        pager1.setCurrentItem(100, true)
        var lastPage = 100
        pager1.setOffscreenPageLimit(2);
        pager1?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            var calendar1: Calendar = Calendar.getInstance()
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                date = dayPagerAdapter.getDateDayAdapter()!!
                if (lastPage >= position) {//User Move to left
                    var date = monthList1.get(position)
                    var day = Integer.parseInt(date.substring(0, 2))
                    var month = Integer.parseInt(date.substring(2, 4))
                    var year = Integer.parseInt(date.substring(4, 8))
                    calendar1.set(Calendar.DAY_OF_MONTH, day)
                    calendar1.set(Calendar.MONTH, month)
                    calendar1.set(Calendar.YEAR, year)
                    calendar1.add(Calendar.DAY_OF_MONTH, -1)
                    var day1: Int = calendar1.get(Calendar.DAY_OF_MONTH)
                    var month1: Int = calendar1.get(Calendar.MONTH)
                    var year1: Int = calendar1.get(Calendar.YEAR)
                    var monthYear =
                        String.format("%02d", day1) + String.format("%02d", month1) + String.format(
                            "%04d",
                            year1
                        )
                    var status = monthList1.contains(monthYear)
                    if (status == false) {
                        monthList1.addFirst(monthYear)
                        dayPagerAdapter.monthList = monthList1
                        dayPagerAdapter.notifyDataSetChanged()
                        pager1.setCurrentItem(monthList1.indexOf(date), true)
                    }

                } else if (lastPage < position) {
                    var date = monthList1.get(position)
                    var day = Integer.parseInt(date.substring(0, 2))
                    var month = Integer.parseInt(date.substring(2, 4))
                    var year = Integer.parseInt(date.substring(4, 8))
                    calendar1.set(Calendar.DAY_OF_MONTH, day)
                    calendar1.set(Calendar.MONTH, month)
                    calendar1.set(Calendar.YEAR, year)
                    calendar1.add(Calendar.DAY_OF_MONTH, 1)
                    var day1: Int = calendar1.get(Calendar.DAY_OF_MONTH)
                    var month1: Int = calendar1.get(Calendar.MONTH)
                    var year1: Int = calendar1.get(Calendar.YEAR)
                    var monthYear =
                        String.format("%02d", day1) + String.format("%02d", month1) + String.format(
                            "%04d",
                            year1
                        )

                    var status = monthList1.contains(monthYear)
                    if (status == false) {
                        monthList1.addLast(monthYear)
                        dayPagerAdapter.monthList = monthList1
                        dayPagerAdapter.notifyDataSetChanged()
                        pager1.setCurrentItem(monthList1.indexOf(date), true)
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
                id,
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

                            nav_view.getMenu().getItem(1).setChecked(true);
                            pager1.adapter = dayPagerAdapter

                            pager1.setCurrentItem(100, true)
                            var lastPage = 100
                            pager1.setOffscreenPageLimit(2);
                            pager1?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                                var calendar1: Calendar = Calendar.getInstance()
                                override fun onPageScrolled(
                                    position: Int,
                                    positionOffset: Float,
                                    positionOffsetPixels: Int
                                ) {
                                }

                                override fun onPageSelected(position: Int) {
                                    date = dayPagerAdapter.getDateDayAdapter()!!
                                    if (lastPage >= position) {//User Move to left
                                        var date = monthList1.get(position)
                                        var day = Integer.parseInt(date.substring(0, 2))
                                        var month = Integer.parseInt(date.substring(2, 4))
                                        var year = Integer.parseInt(date.substring(4, 8))
                                        calendar1.set(Calendar.DAY_OF_MONTH, day)
                                        calendar1.set(Calendar.MONTH, month)
                                        calendar1.set(Calendar.YEAR, year)
                                        calendar1.add(Calendar.DAY_OF_MONTH, -1)
                                        var day1: Int = calendar1.get(Calendar.DAY_OF_MONTH)
                                        var month1: Int = calendar1.get(Calendar.MONTH)
                                        var year1: Int = calendar1.get(Calendar.YEAR)
                                        var monthYear =
                                            String.format("%02d", day1) + String.format("%02d", month1) + String.format(
                                                "%04d",
                                                year1
                                            )
                                        var status = monthList1.contains(monthYear)
                                        if (status == false) {
                                            monthList1.addFirst(monthYear)
                                            dayPagerAdapter.monthList = monthList1
                                            dayPagerAdapter.notifyDataSetChanged()
                                            pager1.setCurrentItem(monthList1.indexOf(date), true)
                                        }

                                    } else if (lastPage < position) {
                                        var date = monthList1.get(position)
                                        var day = Integer.parseInt(date.substring(0, 2))
                                        var month = Integer.parseInt(date.substring(2, 4))
                                        var year = Integer.parseInt(date.substring(4, 8))
                                        calendar1.set(Calendar.DAY_OF_MONTH, day)
                                        calendar1.set(Calendar.MONTH, month)
                                        calendar1.set(Calendar.YEAR, year)
                                        calendar1.add(Calendar.DAY_OF_MONTH, 1)
                                        var day1: Int = calendar1.get(Calendar.DAY_OF_MONTH)
                                        var month1: Int = calendar1.get(Calendar.MONTH)
                                        var year1: Int = calendar1.get(Calendar.YEAR)
                                        var monthYear =
                                            String.format("%02d", day1) + String.format("%02d", month1) + String.format(
                                                "%04d",
                                                year1
                                            )

                                        var status = monthList1.contains(monthYear)
                                        if (status == false) {
                                            monthList1.addLast(monthYear)
                                            dayPagerAdapter.monthList = monthList1
                                            dayPagerAdapter.notifyDataSetChanged()
                                            pager1.setCurrentItem(monthList1.indexOf(date), true)
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
                            nav_view.getMenu().getItem(0).setChecked(true);

                            pager1.adapter = monthPagerAdapter

                            pager1.setCurrentItem(100, true)
                            var lastPage = 100
                            pager1?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {


                                private var thresholdOffset = 0.5f
                                private var scrollStarted: Boolean = false
                                private var checkDirection: Boolean = false
                                var calendar1: Calendar = Calendar.getInstance()
                                override fun onPageScrolled(
                                    position: Int,
                                    positionOffset: Float,
                                    positionOffsetPixels: Int
                                ) {
                                }


                                override fun onPageSelected(position: Int) {
                                    if (lastPage >= position) {
                                        var date = monthList111.get(position)
                                        var month = Integer.parseInt(date.substring(0, 2))
                                        var year = Integer.parseInt(date.substring(2, 6))
                                        calendar1.set(Calendar.MONTH, month)
                                        calendar1.set(Calendar.YEAR, year)
                                        calendar1.set(Calendar.DAY_OF_MONTH, 1)
                                        calendar1.add(Calendar.MONTH, -1)
                                        var month1: Int = calendar1.get(Calendar.MONTH)
                                        var year1: Int = calendar1.get(Calendar.YEAR)
                                        var monthYear = String.format("%02d", month1) + String.format("%04d", year1)
                                        var status = monthList111.contains(monthYear)
                                        if (status == false) {
                                            monthList111.addFirst(monthYear)
                                            monthList111.removeLast()
                                            monthPagerAdapter.monthList = monthList111
                                            monthPagerAdapter.notifyDataSetChanged()
                                            pager1.setCurrentItem(monthList111.indexOf(date), true)
                                        }

                                    } else if (lastPage < position) {
                                        var date = monthList111.get(position)
                                        var month = Integer.parseInt(date.substring(0, 2))
                                        var year = Integer.parseInt(date.substring(2, 6))
                                        calendar1.set(Calendar.MONTH, month)
                                        calendar1.set(Calendar.YEAR, year)
                                        calendar1.set(Calendar.DAY_OF_MONTH, 1)
                                        calendar1.add(Calendar.MONTH, 1)
                                        var month1: Int = calendar1.get(Calendar.MONTH)
                                        var year1: Int = calendar1.get(Calendar.YEAR)
                                        var monthYear = String.format("%02d", month1) + String.format("%04d", year1)
                                        var status = monthList111.contains(monthYear)
                                        if (status == false) {
                                            monthList111.addLast(monthYear)
                                            monthPagerAdapter.monthList = monthList111
                                            monthPagerAdapter.notifyDataSetChanged()
                                            pager1.setCurrentItem(monthList111.indexOf(date), true)
                                        }

                                    }
                                    lastPage = position

                                }

                                override fun onPageScrollStateChanged(state: Int) {
                                    if (!scrollStarted && state == ViewPager.SCROLL_STATE_DRAGGING) {
                                        scrollStarted = true;
                                        checkDirection = true;
                                    } else {
                                        scrollStarted = false;
                                    }
                                }
                            })
                        }
                    }
                    "week" -> {
                        if (request == 2) {

                            nav_view.getMenu().getItem(2).setChecked(true);
                            pager1.adapter = weekPagerAdapter

                            pager1.setCurrentItem(100, true)
                            var lastPage = 100
                            pager1.setOffscreenPageLimit(2);
                            pager1?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                                private var thresholdOffset = 0.5f
                                private var scrollStarted: Boolean = false
                                private var checkDirection: Boolean = false
                                var calendar1: Calendar = Calendar.getInstance()
                                override fun onPageScrolled(
                                    position: Int,
                                    positionOffset: Float,
                                    positionOffsetPixels: Int
                                ) {
                                }

                                override fun onPageSelected(position: Int) {
                                    if (lastPage >= position) {//User Move to left
                                        var date = monthList11.get(position)
                                        var day = Integer.parseInt(date.substring(0, 2))
                                        var month = Integer.parseInt(date.substring(2, 4))
                                        var year = Integer.parseInt(date.substring(4, 8))
                                        calendar1.set(Calendar.DAY_OF_MONTH, day)
                                        calendar1.set(Calendar.MONTH, month)
                                        calendar1.set(Calendar.YEAR, year)
                                        calendar1.add(Calendar.DAY_OF_MONTH, -7)
                                        var day1: Int = calendar1.get(Calendar.DAY_OF_MONTH)
                                        var month1: Int = calendar1.get(Calendar.MONTH)
                                        var year1: Int = calendar1.get(Calendar.YEAR)
                                        var monthYear =
                                            String.format("%02d", day1) + String.format("%02d", month1) + String.format(
                                                "%04d",
                                                year1
                                            )

                                        var status = monthList11.contains(monthYear)
                                        if (status == false) {
                                            monthList11.addFirst(monthYear)
                                            weekPagerAdapter.monthList = monthList11
                                            weekPagerAdapter.notifyDataSetChanged()
                                            pager1.setCurrentItem(monthList11.indexOf(date), true)
                                        }

                                    } else if (lastPage < position) {
                                        var date = monthList11.get(position)
                                        var day = Integer.parseInt(date.substring(0, 2))
                                        var month = Integer.parseInt(date.substring(2, 4))
                                        var year = Integer.parseInt(date.substring(4, 8))
                                        calendar1.set(Calendar.DAY_OF_MONTH, day)
                                        calendar1.set(Calendar.MONTH, month)
                                        calendar1.set(Calendar.YEAR, year)
                                        calendar1.add(Calendar.DAY_OF_MONTH, 7)
                                        var day1: Int = calendar1.get(Calendar.DAY_OF_MONTH)
                                        var month1: Int = calendar1.get(Calendar.MONTH)
                                        var year1: Int = calendar1.get(Calendar.YEAR)
                                        var monthYear =
                                            String.format("%02d", day1) + String.format("%02d", month1) + String.format(
                                                "%04d",
                                                year1
                                            )
                                        var status = monthList11.contains(monthYear)
                                        if (status == false) {
                                            monthList11.addLast(monthYear)
                                            weekPagerAdapter.monthList = monthList11
                                            weekPagerAdapter.notifyDataSetChanged()
                                            pager1.setCurrentItem(monthList11.indexOf(date), true)
                                        }

                                    }
                                    lastPage = position

                                }

                                override fun onPageScrollStateChanged(state: Int) {
                                    if (!scrollStarted && state == ViewPager.SCROLL_STATE_DRAGGING) {
                                        scrollStarted = true;
                                        checkDirection = true;
                                    } else {
                                        scrollStarted = false;
                                    }
                                }
                            })
                        }
                    }
                }

            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
            }
        })

    }

}


