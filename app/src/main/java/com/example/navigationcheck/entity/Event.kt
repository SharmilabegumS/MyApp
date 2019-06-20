package com.example.navigationcheck.entity


class Event(
    val eventId: String, val title: String, var startDate: Long?, var endDate: Long?, val guests: List<String>,
    val description: String
)
