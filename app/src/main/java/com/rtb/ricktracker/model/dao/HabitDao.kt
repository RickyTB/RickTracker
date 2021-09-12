package com.rtb.ricktracker.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.rtb.ricktracker.model.Habit
import java.time.LocalDate

@Dao
interface HabitDao {
    @Insert
    fun insert(vararg habits: Habit)

    @Delete
    fun delete(habit: Habit)

    @Query("SELECT * FROM habit ORDER BY start_date DESC")
    fun getAll(): LiveData<List<Habit>>

    @Update
    fun update(vararg habits: Habit)

    @Query("SELECT * FROM habit WHERE start_date < :endDate AND (finished = NULL OR finished > :startDate) ORDER BY start_date DESC")
    fun getFromRange(startDate: LocalDate, endDate: LocalDate): LiveData<List<Habit>>
}