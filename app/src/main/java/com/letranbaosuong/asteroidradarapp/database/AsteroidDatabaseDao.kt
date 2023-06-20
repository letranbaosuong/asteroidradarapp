package com.letranbaosuong.asteroidradarapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.letranbaosuong.asteroidradarapp.models.Asteroid
import com.letranbaosuong.asteroidradarapp.models.PictureOfDay

@Dao
interface AsteroidDatabaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAsteroidAll(asteroidList: ArrayList<Asteroid>)

    @Query("select * from asteroid_database")
    fun getAsteroidList(): LiveData<List<Asteroid>>

    @Query("SELECT * FROM asteroid_database WHERE closeApproachDate BETWEEN :startWeek AND :endWeek ORDER by closeApproachDate ASC")
    fun getWeekAsteroids(startWeek: String, endWeek: String): LiveData<List<Asteroid>>

    @Query("SELECT * FROM asteroid_database WHERE closeApproachDate == :today ORDER by closeApproachDate ASC")
    fun getTodayAsteroids(today: String): LiveData<List<Asteroid>>

    @Query("delete from asteroid_database")
    fun deleteAsteroid()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAsteroidPicture(pictureOfDay: PictureOfDay)

    @Query("SELECT * FROM picture_database")
    fun getAsteroidPicture(): LiveData<PictureOfDay>
}