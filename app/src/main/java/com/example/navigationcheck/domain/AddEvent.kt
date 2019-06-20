package com.example.navigationcheck.domain

import com.example.navigationcheck.dataBase.DataBaseManager


import com.example.navigationcheck.entity.Event

class AddEvent(private val event: Event, private val userId: String) {
    private var status: Boolean = false

    fun add(dbm: DataBaseManager): Boolean {

        status = dbm.addEvent(userId, event)

        return status

    }
}
