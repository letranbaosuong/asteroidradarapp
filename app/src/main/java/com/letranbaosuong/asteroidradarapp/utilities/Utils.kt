package com.letranbaosuong.asteroidradarapp.utilities

import android.annotation.SuppressLint
import android.os.Build
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@SuppressLint("WeekBasedYear")
fun getDateString(day: Date): String {
    val date = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
    } else {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    }
    return date.format(day.time)
}

@SuppressLint("WeekBasedYear")
fun getCurrentDate(): Date {
    val day = Calendar.getInstance()
    return day.time
}

@SuppressLint("WeekBasedYear")
fun getEndDate(): Date {
    val day = Calendar.getInstance()
    day.add(Calendar.DAY_OF_YEAR, Constants.DEFAULT_END_DATE_DAYS)
    return day.time
}