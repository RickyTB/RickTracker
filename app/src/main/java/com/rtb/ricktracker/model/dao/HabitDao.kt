package com.rtb.ricktracker.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.rtb.ricktracker.model.DayHabit
import com.rtb.ricktracker.model.Habit
import com.rtb.ricktracker.model.HabitWithCompletions
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

    @Transaction
    @Query("SELECT * FROM habit WHERE start_date <= :endDate AND COALESCE(finished, :endDate) >= :startDate ORDER BY start_date DESC")
    fun getFromRange(startDate: LocalDate, endDate: LocalDate): List<HabitWithCompletions>

    @Query("SELECT uid, start_date, days, description, duration, duration_unit, date as completed FROM habit LEFT JOIN (SELECT * FROM completion WHERE date = :day) ON uid = habit_id WHERE start_date <= :day AND COALESCE(finished, :day) >= :day ORDER BY start_date DESC")
    fun getDayHabits(day: LocalDate): List<DayHabit>
}