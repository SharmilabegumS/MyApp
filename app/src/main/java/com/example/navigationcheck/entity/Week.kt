package com.example.navigationcheck.entity


import java.util.Calendar
import java.util.Date

class Day(val date: Date, val events: List<Event>) {
    val day: DayOfWeek?
    private val calendar = Calendar.getInstance()

    enum class DayOfWeek {
        SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY
    }

    init {
        calendar.time = date
        val dow = calendar.get(Calendar.DAY_OF_WEEK)
        day = findDay(dow)
    }

    private fun findDay(dow: Int): DayOfWeek? {

        when (dow) {
            1 -> return DayOfWeek.SUNDAY
            2 -> return DayOfWeek.MONDAY
            3 -> return DayOfWeek.TUESDAY
            4 -> return DayOfWeek.WEDNESDAY
            5 -> return DayOfWeek.THURSDAY
            6 -> return DayOfWeek.FRIDAY
            7 -> return DayOfWeek.SATURDAY
            else -> return null
        }
    }

}
