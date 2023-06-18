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
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(DelicateCoroutinesApi::class)
class MainViewModel(application: Application) : AndroidViewModel(application) {
    private var _asteroidDatabaseDao: AsteroidDatabaseDao
    private var _repository: Repository
    val image: LiveData<PictureOfDay>

    init {
        val dataList = arrayListOf(
            Asteroid(0, "name", "", 0.0, 0.0, 0.0, 0.0, true),
            Asteroid(0, "name", "", 0.0, 0.0, 0.0, 0.0, true),
        )
        _asteroidDatabaseDao =
            AsteroidDatabase.getDatabaseInstance(application).asteroidDatabaseDao
        _repository = Repository(_asteroidDatabaseDao)
        image = _repository.image
        GlobalScope.launch(Dispatchers.IO) {
            _asteroidDatabaseDao.insertAll(dataList)
        }
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
                _repository.asteroidsByDates(
                    startDate = startDate,
                    endDate = endDate,
                    apiKey = Constants.apiKey,
                )
                _repository.getImageInfo(apiKey = Constants.apiKey)
            } catch (e: Exception) {
                Log.e("Exception", "${e.message}")
            }
        }
    }
}