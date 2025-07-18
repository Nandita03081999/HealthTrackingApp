package com.healthtrackingapp.data

import HealthMetricDao
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [HealthMetric::class], version = 1)
abstract class HealthDatabase : RoomDatabase() {
    abstract fun metricDao(): HealthMetricDao

    companion object {
        @Volatile private var INSTANCE: HealthDatabase? = null

        fun getDatabase(context: Context): HealthDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HealthDatabase::class.java,
                    "healthmate_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
