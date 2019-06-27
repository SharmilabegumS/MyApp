package com.example.navigationcheck


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager

import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.widget.TextView

class HomeFragment : androidx.fragment.app.Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_home, container, false)
        (getActivity() as MainActivity).setActionBarTitle("Home")
        return view
    }


}


