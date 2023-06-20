package com.letranbaosuong.asteroidradarapp

import android.app.Application
import android.os.Build
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.letranbaosuong.asteroidradarapp.work.RefreshAsteroidDataWork
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

class DevAsteroidApplication : Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        delayedInit()
    }

    private fun delayedInit() = applicationScope.launch {
        setupRecurringWork()
    }

    private fun setupRecurringWork() {
        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true).setRequiresCharging(true).apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setRequiresDeviceIdle(true)
                }
            }.build()
        val repeatingRequest =
            PeriodicWorkRequestBuilder<RefreshAsteroidDataWork>(1, TimeUnit.DAYS).setConstraints(
                constraints
            ).build()
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            RefreshAsteroidDataWork.WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, repeatingRequest
        )
    }
}