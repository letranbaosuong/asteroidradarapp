package com.letranbaosuong.asteroidradarapp.repositories

import androidx.lifecycle.LiveData
import com.letranbaosuong.asteroidradarapp.api.NasaApi
import com.letranbaosuong.asteroidradarapp.api.parseAsteroidsJsonResult
import com.letranbaosuong.asteroidradarapp.database.AsteroidDatabaseDao
import com.letranbaosuong.asteroidradarapp.models.Asteroid
import com.letranbaosuong.asteroidradarapp.models.PictureOfDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

enum class FilterAsteroid { SAVED, TODAY, WEEK }
class Repository(private val database: AsteroidDatabaseDao) {
    val image: LiveData<PictureOfDay> = database.getAsteroidPicture()
    val getAsteroidList: LiveData<List<Asteroid>> = database.getAsteroidList()
    fun getWeekAsteroids(startWeek: String, endWeek: String) =
        database.getWeekAsteroids(startWeek = startWeek, endWeek = endWeek)

    fun getTodayAsteroids(today: String) = database.getTodayAsteroids(today)

    suspend fun asteroidsByDates(startDate: String, endDate: String, apiKey: String) {
        withContext(Dispatchers.IO) {
            val data = parseAsteroidsJsonResult(
                JSONObject(
                    NasaApi.retrofitService.getAsteroids(
                        startDate = startDate,
                        endDate = endDate,
                        apiKey = apiKey,
                    )
                )
            )
            database.deleteAsteroid()
            database.insertAsteroidAll(data)
        }
    }

    suspend fun getPicture(apiKey: String) {
        withContext(Dispatchers.IO) {
            val data = NasaApi.retrofitService.getImageInfo(apiKey)
            database.insertAsteroidPicture(data)
        }
    }
}