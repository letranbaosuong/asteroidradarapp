package com.letranbaosuong.asteroidradarapp.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.letranbaosuong.asteroidradarapp.database.AsteroidDatabase
import com.letranbaosuong.asteroidradarapp.repositories.AsteroidRepository
import retrofit2.HttpException

class RefreshAsteroidDataWork(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {
    companion object {
        const val WORK_NAME = "RefreshAsteroidDataWork"
    }

    override suspend fun doWork(): Result {
        val database = AsteroidDatabase.getDatabaseInstance(applicationContext).asteroidDatabaseDao
        val repository = AsteroidRepository(database)
        return try {
            repository.refreshAsteroidData()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}