package com.example.Calendar.utility

import java.util.UUID

object UUIDGenerator {
    fun generateUUID(): String {
        return UUID.randomUUID().toString()
    }

}
