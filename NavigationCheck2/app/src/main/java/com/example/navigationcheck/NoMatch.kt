package com.example.navigationcheck

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.navigationcheck.Entity.Event
import kotlinx.android.synthetic.main.no_match.*

class NoMatch : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.no_match)
        setSupportActionBar(toolbar_nomatch)
        val actionBar = supportActionBar

        // Set toolbar title/app title

        actionBar!!.title = " "

        // Set action bar/toolbar sub title


        // Set action bar elevation
        actionBar.elevation = 4.0F


        // Display the app icon in action bar/toolbar
        //actionBar.setDisplayShowHomeEnabled(true)
        //actionBar.setDisplayUseLogoEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)
    }
}
