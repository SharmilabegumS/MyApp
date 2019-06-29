package com.example.Calendar.domain

import com.example.Calendar.dataBase.DataBaseManager


import com.example.Calendar.entity.Event

class AddEvent(private val event: Event, private val userId: String) {
    private var status: Boolean = false

    fun add(dbm: DataBaseManager): Boolean {

        status = dbm.addEvent(userId, event)

        return status

    }
}
