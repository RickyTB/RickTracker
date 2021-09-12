package com.rtb.ricktracker.util

import androidx.room.TypeConverter
import java.time.LocalDate
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

    @TypeConverter
    fun fromEpoch(value: Long?): LocalDate? {
        return value?.let { LocalDate.ofEpochDay(it) }
    }

    @TypeConverter
    fun dateToEpoch(date: LocalDate?): Long? {
        return date?.toEpochDay()
    }

    @TypeConverter
    fun fromBooleanSequence(value: String?): List<Boolean>? {
        return value?.toCharArray()?.map { char -> char == '1' }?.toList()
    }

    @TypeConverter
    fun booleanListToSequence(list: List<Boolean>?): String? {
        return list?.fold("", { acc, value -> if (value) acc + "1" else acc + "0" })
    }
}
