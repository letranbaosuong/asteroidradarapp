package com.letranbaosuong.asteroidradarapp.repositories

import androidx.lifecycle.LiveData
import com.letranbaosuong.asteroidradarapp.api.NasaApi
import com.letranbaosuong.asteroidradarapp.api.parseAsteroidsJsonResult
import com.letranbaosuong.asteroidradarapp.database.AsteroidDatabaseDao
import com.letranbaosuong.asteroidradarapp.models.Asteroid
import com.letranbaosuong.asteroidradarapp.models.PictureOfDay
import com.letranbaosuong.asteroidradarapp.utilities.Constants
import com.letranbaosuong.asteroidradarapp.utilities.getDateString
import com.letranbaosuong.asteroidradarapp.utilities.getEndDate
import com.letranbaosuong.asteroidradarapp.utilities.getStartDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

enum class FilterAsteroid { SAVED, TODAY, WEEK }
class AsteroidRepository(private val database: AsteroidDatabaseDao) {
    val image: LiveData<PictureOfDay> = database.getAsteroidPicture()
    val getAsteroidList: LiveData<List<Asteroid>> = database.getAsteroidList()
    fun getWeekAsteroids(startWeek: String, endWeek: String) =
        database.getWeekAsteroids(startWeek = startWeek, endWeek = endWeek)

    fun getTodayAsteroids(today: String) = database.getTodayAsteroids(today)

    suspend fun getAsteroidsByDates(startDate: String, endDate: String, apiKey: String) {
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

    suspend fun refreshAsteroidData() {
        val startDate = getDateString(getStartDate())
        val endDate = getDateString(getEndDate())
        getPicture(apiKey = Constants.apiKey)
        getAsteroidsByDates(
            startDate = startDate,
            endDate = endDate,
            apiKey = Constants.apiKey,
        )
    }
}