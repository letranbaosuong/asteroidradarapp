package com.letranbaosuong.asteroidradarapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.letranbaosuong.asteroidradarapp.models.Asteroid
import java.util.concurrent.Executors

//@Database(entities = [Asteroid::class], version = 1, exportSchema = false)
@Database(entities = [Asteroid::class], version = 1, exportSchema = true)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDatabaseDao: AsteroidDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: AsteroidDatabase? = null
        fun getDatabaseInstance(context: Context): AsteroidDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AsteroidDatabase::class.java,
                        "asteroid_database_db"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}

object DatabaseProvider {
    private var database: AsteroidDatabase? = null

    fun getDatabaseInstance(context: Context): AsteroidDatabase {
        return database ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AsteroidDatabase::class.java,
                "asteroid_database_db"
            ).build()
            database = instance
            instance
        }
    }
}

