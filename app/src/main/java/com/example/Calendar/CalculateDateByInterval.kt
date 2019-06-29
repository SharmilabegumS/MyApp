package com.example.Calendar


import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TextView
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
class CalculateDateByInterval : Fragment() {
    var activity1 :CalculateDateActivity?=null
    lateinit var endDate:Date
    lateinit var startDate:TextView
    lateinit var endDate1:TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       var view=inflater.inflate(R.layout.fragment_calculate_date_by_interval, container, false)
       startDate=view.findViewById<TextView>(R.id.start_date)
     endDate1=view.findViewById<TextView>(R.id.end_date)
    activity1=activity as CalculateDateActivity
        //activity!!.date=Date()
        var date=activity1!!.date
        val myFormat = "EEEE, dd MMMM, yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        calendar.time=date
        startDate!!.setText(sdf.format(calendar.getTime()))
        endDate1!!.setText(sdf.format(calendar.getTime()))
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(
                view: DatePicker, year: Int, monthOfYear: Int,
                dayOfMonth: Int
            ) {
                mDay = dayOfMonth
                mMonth = monthOfYear
                mYear = year
                updateDateInView(startDate)
                /* var result = checkDates(startDate,endDate1)
                if (result == false) {

                   startDate.setTextColor(Color.RED)

                    //d1.setTextAppearance(getApplicationContext(), R.style.MyTextInputLayout);
                    //var colorStateList:      ColorStateList = ColorStateList.valueOf(Color.RED)
                    // d1.supportBackgroundTintList=colorStateList
                    // e1.setError("Start time is after event end time!")
                    // e1.supportBackgroundTintList=colorStateList
                } else if (result == true) {
                    startDate.setTextColor(Color.GRAY)


                }*/
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
                mDay = dayOfMonth
                mMonth = monthOfYear
                mYear = year
                updateDateInView(endDate1)
                //if(e1.text!=null&&e2.text!=null && d1.text!=null &&d2.text!=null)
                //  checkDateAndTime(d1,d2,e1,e2)
                //else if(d1.text!=null &&d2.text!=null){
              /*  var result = checkDates(startDate,endDate1)
                if (result == false) {
                    startDate.setTextColor(Color.RED)

                    // d1.setError("Start date is after event end date!")
                    // var colorStateList:      ColorStateList = ColorStateList.valueOf(Color.RED)
                    // d1.supportBackgroundTintList=colorStateList
                    //e1.setError("Start time is after event end time!")
                    // e1.supportBackgroundTintList=colorStateList
                } else if (result == true) {
                    startDate.setTextColor(Color.GRAY)

                }*/

                //}

            }
        }




        startDate.setOnClickListener{
            DatePickerDialog(
                context, R.style.MyDialogTheme,
                dateSetListener,
                mYear, mMonth, mDay
            ).show()


        }
        endDate1.setOnClickListener{
            DatePickerDialog(
                context, R.style.MyDialogTheme,
                dateSetListener1,
                mYear, mMonth, mDay
            ).show()
        }

        return view
    }

    private fun updateDateInView(tv:TextView) {
        val myFormat = "EEEE, dd MMMM, yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.DAY_OF_MONTH, mDay)
        calendar.set(Calendar.MONTH, mMonth)
        tv!!.setText(sdf.format(calendar.getTime()))

    }
   /* private fun checkDates(d1: TextView, d2: TextView): Boolean {
        var status = false
        try {

            val formatter1 = SimpleDateFormat("EEEE, dd MMMM, yyyy")
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


    }*/
    fun getEndDate1(){
        val sdf = SimpleDateFormat("EEEE, dd MMMM, yyyy")
        date = sdf.parse(endDate1.text.toString())
       activity1!!.endDate=date
    }
    fun getStartDate1(){
        val sdf = SimpleDateFormat("EEEE, dd MMMM, yyyy")
        date = sdf.parse(startDate.text.toString())
        activity1!!.startDate=date
    }
    fun getNumberDays(){
        activity1!!.forwardOrBackward= "after"
        val sdf = SimpleDateFormat("EEEE, dd MMMM, yyyy")
        val difference = dsc.getDateInMillis1(sdf.parse(endDate1.text.toString()))- dsc.getDateInMillis1(sdf.parse(startDate.text.toString()))
        val daysBetween = (difference / (1000 * 60 * 60 * 24))
        activity1!!.numberOfDays=daysBetween.toInt()
        if(daysBetween.toInt()<0){
            activity1!!.numberOfDays=daysBetween.toInt()-(2*daysBetween.toInt())
            activity1!!.forwardOrBackward= "before"
        }
    }

}
