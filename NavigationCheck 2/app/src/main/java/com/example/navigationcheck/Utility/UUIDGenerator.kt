package com.example.navigationcheck.Utility
import java.util.UUID

object UUIDGenerator {
    fun generateUUID(): String {
        return UUID.randomUUID().toString()
    }

}
