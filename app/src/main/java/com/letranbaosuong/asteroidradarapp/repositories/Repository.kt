package com.letranbaosuong.asteroidradarapp.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.letranbaosuong.asteroidradarapp.api.NasaApi
import com.letranbaosuong.asteroidradarapp.api.parseAsteroidsJsonResult
import com.letranbaosuong.asteroidradarapp.database.AsteroidDatabaseDao
import com.letranbaosuong.asteroidradarapp.models.PictureOfDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class Repository(private val database: AsteroidDatabaseDao) {
    private val _images = MutableLiveData<PictureOfDay>()
    val image: LiveData<PictureOfDay> = _images
    suspend fun asteroidsByDates(startDate: String, endDate: String, apiKey: String) {
        withContext(Dispatchers.IO) {
            val data = parseAsteroidsJsonResult(
                JSONObject(
                    NasaApi.retrofitService.getAsteroids(
                        startDate, endDate, apiKey,
                    )
                )
            )
//            database.delete()
//            database.insertAll(data)
        }
    }

    suspend fun getImageInfo(apiKey: String) {
        val scope = withContext(Dispatchers.IO) {
            return@withContext NasaApi.retrofitService.getImageInfo(apiKey)
        }
        scope.let {
            _images.value = it
        }
    }
}