package com.letranbaosuong.asteroidradarapp.main

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.letranbaosuong.asteroidradarapp.database.AsteroidDatabase
import com.letranbaosuong.asteroidradarapp.database.AsteroidDatabaseDao
import com.letranbaosuong.asteroidradarapp.models.Asteroid
import com.letranbaosuong.asteroidradarapp.models.PictureOfDay
import com.letranbaosuong.asteroidradarapp.repositories.Repository
import com.letranbaosuong.asteroidradarapp.utilities.Constants
import com.letranbaosuong.asteroidradarapp.utilities.Constants.DEFAULT_END_DATE_DAYS
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private var _asteroidDatabaseDao: AsteroidDatabaseDao
    private var _repository: Repository
    val image: LiveData<PictureOfDay>
    val listAsteroid: LiveData<List<Asteroid>>

    init {
        _asteroidDatabaseDao =
            AsteroidDatabase.getDatabaseInstance(application).asteroidDatabaseDao
        _repository = Repository(_asteroidDatabaseDao)
        fetchData()
        image = _repository.image
        listAsteroid = _repository.listAsteroid
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

    private fun fetchData() {
        try {
            val startDate = getDateString(getStartDate())
            val endDate = getDateString(getEndDate())

            viewModelScope.launch {
                _repository.getImageInfo(apiKey = Constants.apiKey)
                _repository.asteroidsByDates(
                    startDate = startDate,
                    endDate = endDate,
                    apiKey = Constants.apiKey,
                )

            }
        } catch (e: Exception) {
            Log.e("fetchAsteroidsByDates", "${e.message}")
        }

    }
}