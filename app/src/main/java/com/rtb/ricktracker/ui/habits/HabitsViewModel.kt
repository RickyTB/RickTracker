package com.rtb.ricktracker.ui.habits

import android.app.Application
import androidx.lifecycle.*
import com.rtb.ricktracker.AppDatabase
import com.rtb.ricktracker.model.Habit
import com.rtb.ricktracker.model.dao.HabitDao

class HabitsViewModel(application: Application) : AndroidViewModel(application) {
    private val habitDao: HabitDao = AppDatabase.getDatabase(application).habitDao()
    val getHabits: LiveData<List<Habit>> = habitDao.getAll()
}