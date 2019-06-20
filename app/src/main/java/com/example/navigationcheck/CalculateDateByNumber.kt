package com.example.navigationcheck


import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.IntegerRes
import kotlinx.android.synthetic.main.fragment_calculate_date_by_number.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */

class CalculateDateByNumber : Fragment() {
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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_calculate_date_by_number, container, false)
        startDate = view.findViewById<TextView>(R.id.start_date)
        numberOfDays = view.findViewById<TextView>(R.id.number_of_days)
        forwardOrBackward = view.findViewById<TextView>(R.id.forward_or_backward)
        //activity!!.date=Date()
        activity1 = activity as CalculateDateActivity
        date = activity1!!.date!!
        // Inflate the layout for this fragment

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
            if (forwardOrBackward.text.equals("Forward")) {
                forwardOrBackward.setText("Backward")
            } else if (forwardOrBackward.text.equals("Backward")) {
                forwardOrBackward.setText("Forward")
            }
        }
        return view
    }

    fun getEndDate1() {
        if (numberOfDays.text.toString().equals("")) {
            var toast=Toast.makeText(context, "Enter the number of days", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show()
            activity1!!.endDate = null
        } else {
            println("numberOfDays:" + numberOfDays.text + "Hi")
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

}
