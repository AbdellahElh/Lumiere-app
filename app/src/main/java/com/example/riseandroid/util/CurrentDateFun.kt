package com.example.riseandroid.util

import java.util.Calendar
import java.util.Locale

fun getTodayDate(): String {
    val calendar = Calendar.getInstance()
    return String.format(
        Locale.getDefault(),
        "%04d-%02d-%02d",
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH) + 1,
        calendar.get(Calendar.DAY_OF_MONTH)
    )
}