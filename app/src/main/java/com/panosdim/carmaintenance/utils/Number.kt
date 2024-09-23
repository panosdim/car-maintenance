package com.panosdim.carmaintenance.utils

import java.text.NumberFormat
import java.util.Locale

fun getFormattedNumber(number: String): String {
    val numberFormat = NumberFormat.getNumberInstance(Locale("el", "GR"))
    return if (number.isNotEmpty()) {
        try {
            val num = number.toInt()
            "${numberFormat.format(num)} km"
        } catch (_: NumberFormatException) {
            "$number km"
        }
    } else {
        "20.000km"
    }
}