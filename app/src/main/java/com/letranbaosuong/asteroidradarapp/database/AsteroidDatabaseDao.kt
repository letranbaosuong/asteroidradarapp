package com.letranbaosuong.asteroidradarapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.letranbaosuong.asteroidradarapp.models.Asteroid

@Dao
interface AsteroidDatabaseDao {
    @Query("select * from asteroid_database")
    fun getAsteroidList(): LiveData<List<Asteroid>>

    @Query("SELECT * FROM asteroid_database WHERE closeApproachDate >= :week ORDER by closeApproachDate ASC")
    fun getWeekAsteroids(week: String): LiveData<List<Asteroid>>

    @Query("SELECT * FROM asteroid_database WHERE closeApproachDate == :day ORDER by closeApproachDate ASC")
    fun getTodayAsteroids(day: String): LiveData<List<Asteroid>>

    @Query("delete from asteroid_database")
    fun delete()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(asteroidList: ArrayList<Asteroid>)
}