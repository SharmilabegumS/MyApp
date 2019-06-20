package com.example.navigationcheck


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.cardview.widget.CardView
import android.util.DisplayMetrics
import android.view.*
import android.widget.*
import kotlinx.android.synthetic.main.content_day.*
import kotlinx.android.synthetic.main.fragment_day.*
import java.text.SimpleDateFormat
import java.util.*
import android.text.method.Touch.onTouchEvent
import android.view.MotionEvent




// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
var dayDate: Date =Date()
var dayCalendar :Calendar=Calendar.getInstance()

/**
 * A simple [Fragment] subclass.
 *
 */
class DayFragment : androidx.fragment.app.Fragment() {

    companion object {

        fun newInstance(datePassed:Date):DayFragment{
            val args: Bundle = Bundle()
            args.putSerializable(dayDate.toString(),datePassed)
            dayCalendar.setTime(datePassed)
            dayDate= dayCalendar.time
            val fragment = DayFragment()
            return fragment
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (getActivity() as MainActivity).setActionBarTitle("Day")
        val view= inflater.inflate(R.layout.fragment_day, container, false)
        var container1=view.findViewById<GridLayout>(R.id.container)
        dayCalendar.setTime(dayDate);
        var dayId: TextView =view.findViewById(R.id.day)
        var day_layout:LinearLayout=view.findViewById(R.id.day_layout)
        var day:LinearLayout=view.findViewById(R.id.day_fragment)
        var layoutparams: LinearLayout.LayoutParams = dayId.getLayoutParams() as (LinearLayout.LayoutParams)
        layoutparams.height = (screenHeightCommon/11.38).toInt()
       dayId.setLayoutParams(layoutparams);

        var start = SimpleDateFormat("dd/MM/yyyy").format(dayDate)
        dayId.setText("$start")
        clearForm(this.container)
  /*day.setOnTouchListener(object:OnSwipeTouchListener() {
            override fun onSwipeLeft() {

                dayCalendar.setTime(dayDate)
                dayCalendar.add(Calendar.DAY_OF_MONTH, 1)
                var start = SimpleDateFormat("dd/MM/yyyy").format(dayCalendar.time)
                dayId.setText("$start")
                DayFragment.newInstance(dayCalendar.time)
                val manager = activity!!.supportFragmentManager
                val ft = manager.beginTransaction()
                val newFragment = this@DayFragment
                this@DayFragment.onDestroy()
                ft.remove(this@DayFragment)
                ft.replace(container!!.getId(), newFragment)
                //container is the ViewGroup of current fragment
                ft.addToBackStack(null)
                ft.commit()

            }

            override fun onSwipeRight() {
                dayCalendar.setTime(dayDate)
                dayCalendar.add(Calendar.DAY_OF_MONTH, -1)
                var start = SimpleDateFormat("dd/MM/yyyy").format(dayCalendar.time)
                dayId.setText("$start")
                DayFragment.newInstance(dayCalendar.time)
                val manager = activity!!.supportFragmentManager
                val ft = manager.beginTransaction()
                val newFragment = this@DayFragment
                this@DayFragment.onDestroy()
                ft.remove(this@DayFragment)
                ft.replace(container!!.getId(), newFragment)
                //container is the ViewGroup of current fragment
                ft.addToBackStack(null)
                ft.commit()

            }



        })*/
        return view
    }


}
private fun clearForm(group: ViewGroup?) {
    var i = 1

    while (i < 24) {

        val view = group?.getChildAt(i)
        if (view is TextView) {
            setDimensions(view,(screenWidthCommon-10),(screenHeightCommon/20).toInt())

        }
        if (view is ViewGroup && view.childCount > 0)
            clearForm(view)
        i++
    }

}
private fun setDimensions(view: View, width: Int, height: Int) {
    val params = view.layoutParams
    params.width = width
    params.height = height
    view.layoutParams = params
}