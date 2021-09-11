package com.rtb.ricktracker.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "habit", indices = [Index("start_date")])
data class Habit(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    val description: String,
    @ColumnInfo(name = "start_date") val startDate: Date,
    val duration: Int,
    @ColumnInfo(name = "duration_unit") val durationUnit: String,
    val days: List<Boolean>,
    val finished: Date? = null
)
