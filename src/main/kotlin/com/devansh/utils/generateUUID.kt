package com.devansh.utils

import java.util.*

fun generateUUID(): String {
    val uuid = UUID.randomUUID()
    return uuid.toString()  // Convert UUID to string
}