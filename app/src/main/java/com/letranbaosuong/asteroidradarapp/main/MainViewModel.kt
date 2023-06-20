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
import com.letranbaosuong.asteroidradarapp.repositories.AsteroidRepository
import com.letranbaosuong.asteroidradarapp.repositories.FilterAsteroid
import com.letranbaosuong.asteroidradarapp.utilities.Constants
import com.letranbaosuong.asteroidradarapp.utilities.getDateString
import com.letranbaosuong.asteroidradarapp.utilities.getEndDate
import com.letranbaosuong.asteroidradarapp.utilities.getStartDate
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.M)
class MainViewModel(application: Application) : AndroidViewModel(application) {
    private var _asteroidDatabaseDao =
        AsteroidDatabase.getDatabaseInstance(application).asteroidDatabaseDao
    private var _asteroidRepository = AsteroidRepository(_asteroidDatabaseDao)
    private val _filterAsteroid = MutableLiveData(FilterAsteroid.SAVED)
    val image: LiveData<PictureOfDay> = _asteroidRepository.image
    val listAsteroid = _filterAsteroid.switchMap {
        val startDate = getDateString(getStartDate())
        val endDate = getDateString(getEndDate())
        when (it) {
            FilterAsteroid.TODAY -> _asteroidRepository.getTodayAsteroids(startDate)
            FilterAsteroid.WEEK -> _asteroidRepository.getWeekAsteroids(
                startWeek = startDate,
                endWeek = endDate
            )

            else -> _asteroidRepository.getAsteroidList
        }
    }

    init {
        if (hasNetworkAvailable(application)) {
            fetchData()
        }
    }

    @SuppressLint("LogNotTimber")
    private fun fetchData() {
        try {
            val startDate = getDateString(getStartDate())
            val endDate = getDateString(getEndDate())

            viewModelScope.launch {
                _asteroidRepository.getPicture(apiKey = Constants.apiKey)
                _asteroidRepository.getAsteroidsByDates(
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
            R.id.show_week_menu -> _filterAsteroid.value = FilterAsteroid.WEEK
            R.id.show_today_menu -> _filterAsteroid.value = FilterAsteroid.TODAY
            else -> _filterAsteroid.value = FilterAsteroid.SAVED
        }
    }
}