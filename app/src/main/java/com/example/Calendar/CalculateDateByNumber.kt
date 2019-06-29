package com.example.Calendar


import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.graphics.Rect
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.IntegerRes
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */

class CalculateDateByNumber : Fragment() {
    var keyBoardState:Boolean=false
    companion object {
        fun newInstance(date: Date) {

        }
    }

    var activity1: CalculateDateActivity? = null
    lateinit var date: Date
    lateinit var endDate: Date
    lateinit var startDate: TextView
    lateinit var numberOfDays: TextView
    lateinit var forwardOrBackward: TextView
    lateinit var lv:ListView
    @SuppressLint("NewApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var selectMovement:Boolean=false
        var view = inflater.inflate(R.layout.fragment_calculate_date_by_number, container, false)
        startDate = view.findViewById<TextView>(R.id.start_date)
        numberOfDays = view.findViewById<TextView>(R.id.number_of_days)
        forwardOrBackward = view.findViewById<TextView>(R.id.forward_or_backward)

        setupUI(view.findViewById(R.id.rl))
        var layout=view.findViewById<RelativeLayout>(R.id.rl)
        layout.getViewTreeObserver().addOnGlobalLayoutListener(object :ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                var r = Rect()
                layout.getWindowVisibleDisplayFrame(r);
                var screenHeight = layout.getRootView().getHeight();
                var keypadHeight = screenHeight - r.bottom;
                if (keypadHeight > screenHeight * 0.15) {
                    keyBoardState=true
                } else {
                    keyBoardState=false
                }
            }
        });
        //activity!!.date=Date()
        lv=view.findViewById(R.id.lv)
        activity1 = activity as CalculateDateActivity
        date = activity1!!.date!!
        // Inflate the layout for this fragment
        numberOfDays!!.setShowSoftInputOnFocus(false)

        numberOfDays.setOnClickListener{
activity1!!.showKeyBoard()
        }
        val myFormat = "EEEE, dd MMMM, yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        calendar.time = date
        startDate!!.setText(sdf.format(calendar.getTime()))
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(
                view: DatePicker, year: Int, monthOfYear: Int,
                dayOfMonth: Int
            ) {
                mDay = dayOfMonth
                mMonth = monthOfYear
                mYear = year
                updateDateInView(startDate)

            }
        }
        startDate.setOnClickListener {
            DatePickerDialog(
                context, R.style.MyDialogTheme,
                dateSetListener,
                mYear, mMonth, mDay
            ).show()
        }
        forwardOrBackward.setOnClickListener {
if(selectMovement==false){
    lv.visibility=View.VISIBLE
    val img = context!!.resources.getDrawable(R.drawable.ic_arrow_up,null)
    img.setBounds( 0, 0, 80, 80 );
    forwardOrBackward.setCompoundDrawables(null,null,img,null)
    selectMovement=true

}
            else{
    lv.visibility=View.INVISIBLE
    val img = context!!.resources.getDrawable(R.drawable.arrow_down,null)
    img.setBounds( 0, 0, 80, 80 );
    forwardOrBackward.setCompoundDrawables(null,null,img,null)
    selectMovement=false
            }
              /*if (forwardOrBackward.text.equals("Forward")) {
                forwardOrBackward.setText("Backward")
            } else if (forwardOrBackward.text.equals("Backward")) {
                forwardOrBackward.setText("Forward")
            }*/
        }
var values=ArrayList<String>()
        values.add("Forward")
        values.add("Backward")
        val adapter = ArrayAdapter<String>(context, R.layout.list_time_interval,R.id.tv, values)
        adapter.notifyDataSetChanged()
        lv.adapter = adapter


        lv.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                forwardOrBackward.setText(values.get(position))
                val img = context!!.resources.getDrawable(R.drawable.arrow_down,null)
                img.setBounds( 0, 0, 80, 80 );
                forwardOrBackward.setCompoundDrawables(null,null,img,null)
                lv.visibility=View.INVISIBLE


            }
        })
        return view
    }

    fun getEndDate1() {
        if (numberOfDays.text.toString().equals("")) {
            var toast=Toast.makeText(context, "Enter the number of days", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show()
            activity1!!.endDate = null
        }
        else
         {
            var cal = Calendar.getInstance()
            cal.time = date

            if (forwardOrBackward.text.equals("Forward")) {
                cal.add(Calendar.DAY_OF_MONTH, Integer.parseInt(numberOfDays.text.toString()))

            } else if (forwardOrBackward.text.equals("Backward")) {
                cal.add(Calendar.DAY_OF_MONTH, -Integer.parseInt(numberOfDays.text.toString()))

            }
            endDate = cal.time
            activity1!!.endDate = endDate

        }
    }

    fun getStartDate1() {
        val sdf = SimpleDateFormat("EEEE, dd MMMM, yyyy")
        date = sdf.parse(startDate.text.toString())
        activity1!!.startDate = date
    }

    private fun updateDateInView(tv: TextView) {
        val myFormat = "EEEE, dd MMMM, yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.DAY_OF_MONTH, mDay)
        calendar.set(Calendar.MONTH, mMonth)
        tv!!.setText(sdf.format(calendar.getTime()))

    }

    fun getNumberDays() {
        if (forwardOrBackward.text.equals("Forward")) {
            activity1!!.forwardOrBackward = "after"
        } else if (forwardOrBackward.text.equals("Backward")) {
            activity1!!.forwardOrBackward = "before"
        }
        if (numberOfDays.text.toString().equals("") == false) {
            activity1!!.numberOfDays = Integer.parseInt(numberOfDays.text.toString())
        } else {
            activity1!!.numberOfDays = null
        }
    }
    private fun hideSoftKeyboard(){
        if(keyBoardState==true) {
            val inputMethodManager = getActivity()!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(activity!!.currentFocus!!.windowToken, 0)
        }
    }


    fun setupUI( view:View) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view is EditText)) {
            view.setOnTouchListener(object :View.OnTouchListener {
                override fun onTouch(v:View , event: MotionEvent):Boolean {
                    hideSoftKeyboard();
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view is ViewGroup) {
            for ( i in 0..(view as ViewGroup).getChildCount()-1) {
                var innerView = ( view as ViewGroup).getChildAt(i);
                setupUI(innerView);
            }
        }
    }


}
