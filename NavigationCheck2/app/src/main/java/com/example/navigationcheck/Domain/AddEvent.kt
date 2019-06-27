package com.example.navigationcheck.Domain

import com.example.navigationcheck.DataBase.DataBaseManager


import com.example.navigationcheck.Entity.Event

class AddEvent(private val event: Event,private val userId:String) {
    private var status: Boolean = false

    fun add(dbm: DataBaseManager): Boolean {

        status = dbm.addEvent(userId,event)

        return status

    }
}
