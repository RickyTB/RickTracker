package com.rtb.ricktracker.model

import androidx.room.ColumnInfo
import java.time.LocalDate

data class DayHabit(
    val uid: Int = 0,
    val description: String,
    @ColumnInfo(name = "start_date") val startDate: LocalDate,
    val duration: Int,
    @ColumnInfo(name = "duration_unit") val durationUnit: String,
    val days: List<Boolean>,
    val completed: LocalDate?
)