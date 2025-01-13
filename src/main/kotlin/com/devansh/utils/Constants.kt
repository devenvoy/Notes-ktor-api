package com.devansh.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


val DefaultFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

fun LocalDateTime.toString(formatter: DateTimeFormatter = DefaultFormatter): String {
    return format(formatter)
}

fun LocalDate?.toString(formatter: DateTimeFormatter = DefaultFormatter): String?{
    return this?.format(formatter)
}