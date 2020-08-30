
package com.example.android.trackmysleepquality.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_sleep_quality_table")
data class SleepNight(

        @PrimaryKey(autoGenerate = true)
        val nightId: Long = 0L,

        @ColumnInfo(name = "star_time")
        val startTime: Long = System.currentTimeMillis(),

        @ColumnInfo(name = "end_time")
        var endTime: Long = startTime,

        @ColumnInfo(name = "quality_rating")
        var sleepQuality: Int = -1
)