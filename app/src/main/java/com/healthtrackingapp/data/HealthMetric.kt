package com.healthtrackingapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "health_metrics")
data class HealthMetric(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String,
    val value: Float,
    val timestamp: Long = System.currentTimeMillis()
)
