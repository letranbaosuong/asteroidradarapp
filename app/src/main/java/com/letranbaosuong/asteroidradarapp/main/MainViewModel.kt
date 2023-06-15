package com.letranbaosuong.asteroidradarapp.main

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.letranbaosuong.asteroidradarapp.repositories.Repository
import com.letranbaosuong.asteroidradarapp.utilities.Constants
import com.letranbaosuong.asteroidradarapp.utilities.Constants.DEFAULT_END_DATE_DAYS
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainViewModel : ViewModel() {
    private val _response = MutableLiveData<String>()
    private val repos = Repository()
    val image = repos.image
    val response: LiveData<String> get() = _response

    init {
        fetchAsteroidsByDates()
    }

    @SuppressLint("WeekBasedYear")
    private fun getDateString(day: Date): String {
        val date = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        } else {
            TODO("VERSION.SDK_INT < N")
        }
        return date.format(day.time)
    }

    @SuppressLint("WeekBasedYear")
    private fun getStartDate(): Date {
        val day = Calendar.getInstance()
        return day.time
    }

    @SuppressLint("WeekBasedYear")
    private fun getEndDate(): Date {
        val day = Calendar.getInstance()
        day.add(Calendar.DAY_OF_YEAR, DEFAULT_END_DATE_DAYS)
        return day.time
    }

    private fun fetchAsteroidsByDates() {
        val startDate = getDateString(getStartDate())
        val endDate = getDateString(getEndDate())
        viewModelScope.launch {
            try {
                repos.asteroidsByDates(
                    startDate = startDate,
                    endDate = endDate,
                    apiKey = Constants.apiKey,
                )
                repos.getImageInfo(apiKey = Constants.apiKey)
            } catch (e: Exception) {
                Log.e("Exception", "${e.message}")
            }
        }
    }
}