package com.example.Calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment



class BottomSheetFragment1 : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
        val activity=activity as EditEvent

        val listView = view.findViewById<ListView>(R.id.listView)
        val adapter = ArrayAdapter(
            context!!,
            android.R.layout.simple_list_item_1, resources.getStringArray(R.array.UserList)
        )
        listView.setAdapter(adapter)
        listView.setOnItemClickListener(AdapterView.OnItemClickListener { _, _, position, id ->

            val itemValue = listView.getItemAtPosition(position) as String
            activity.user_name.setText(itemValue)
            activity.closeBottomSheet()


        })
        return view
    }
    companion object {

        fun newInstance(): BottomSheetFragment1 {
            return BottomSheetFragment1()
        }
    }
}

