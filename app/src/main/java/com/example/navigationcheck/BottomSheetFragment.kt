package com.example.navigationcheck


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment







// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */

class BottomSheetFragment : BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       var view= inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
var activity=activity as AddEvent

        var listView = view.findViewById<ListView>(R.id.listView)
        val adapter = ArrayAdapter(
            context,
            android.R.layout.simple_list_item_1, resources.getStringArray(R.array.UserList)
        )
        listView.setAdapter(adapter)
        listView.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->

            val itemValue = listView.getItemAtPosition(position) as String
           activity!!.user_name.setText(itemValue)
         activity!!.closeBottomSheet()


        })
        return view
    }
    companion object {

        fun newInstance(): BottomSheetFragment {
            return BottomSheetFragment()
        }
    }
}// Required empty public constructor

