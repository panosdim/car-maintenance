package com.panosdim.carmaintenance.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

fun LocalDate.toEpochMilli(): Long {
    return this.toEpochDay() * (1000 * 60 * 60 * 24)
}

fun Long.toLocalDate(): LocalDate {
    return LocalDate.ofEpochDay(this / (1000 * 60 * 60 * 24))
}

fun LocalDate.toFormattedString(): String {
    return this.format(dateFormatter)
}

fun String.toLocalDate(): LocalDate {
    return try {
        LocalDate.parse(
            this,
            dateFormatter
        )
    } catch (ex: DateTimeParseException) {
        LocalDate.now()
    }
}