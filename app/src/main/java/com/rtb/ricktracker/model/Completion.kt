package com.rtb.ricktracker.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import java.time.LocalDate

@Entity(tableName = "completion", primaryKeys = ["date", "habit_id"]) //, indices = [Index("date")])
data class Completion(
    val date: LocalDate,
    @ColumnInfo(name = "habit_id") val habitId: Int,
)
