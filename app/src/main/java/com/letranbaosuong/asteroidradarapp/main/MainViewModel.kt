package com.letranbaosuong.asteroidradarapp.main

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.util.Log
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.letranbaosuong.asteroidradarapp.R
import com.letranbaosuong.asteroidradarapp.database.AsteroidDatabase
import com.letranbaosuong.asteroidradarapp.models.PictureOfDay
import com.letranbaosuong.asteroidradarapp.repositories.FilterAsteroid
import com.letranbaosuong.asteroidradarapp.repositories.Repository
import com.letranbaosuong.asteroidradarapp.utilities.Constants
import com.letranbaosuong.asteroidradarapp.utilities.Constants.DEFAULT_END_DATE_DAYS
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.M)
class MainViewModel(application: Application) : AndroidViewModel(application) {
    private var _asteroidDatabaseDao =
        AsteroidDatabase.getDatabaseInstance(application).asteroidDatabaseDao
    private var _repository = Repository(_asteroidDatabaseDao)
    private val _filterAsteroid = MutableLiveData(FilterAsteroid.SAVED)
    val image: LiveData<PictureOfDay> = _repository.image
    val listAsteroid = _filterAsteroid.switchMap {
        val startDate = getDateString(getStartDate())
        val endDate = getDateString(getEndDate())
        when (it) {
            FilterAsteroid.TODAY -> _repository.getTodayAsteroids(startDate)
            FilterAsteroid.WEEK -> _repository.getWeekAsteroids(
                startWeek = startDate,
                endWeek = endDate
            )

            else -> _repository.getAsteroidList
        }
    }

    init {
        if (hasNetworkAvailable(application)) {
            fetchData()
        }
    }

    @SuppressLint("WeekBasedYear")
    private fun getDateString(day: Date): String {
        val date = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        } else {
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
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

    private fun fetchData() {
        try {
            val startDate = getDateString(getStartDate())
            val endDate = getDateString(getEndDate())

            viewModelScope.launch {
                _repository.getPicture(apiKey = Constants.apiKey)
                _repository.asteroidsByDates(
                    startDate = startDate,
                    endDate = endDate,
                    apiKey = Constants.apiKey,
                )
            }
        } catch (e: Exception) {
            Log.e("fetchData", "${e.message}")
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun hasNetworkAvailable(application: Application): Boolean {
        val connManager =
            application.baseContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connManager.getNetworkCapabilities(connManager.activeNetwork)
        return networkCapabilities != null
    }

    fun onMenuSelected(item: MenuItem) {
        when (item.itemId) {
            R.id.show_all_menu -> _filterAsteroid.value = FilterAsteroid.WEEK
            R.id.show_rent_menu -> _filterAsteroid.value = FilterAsteroid.TODAY
            else -> _filterAsteroid.value = FilterAsteroid.SAVED
        }
    }
}