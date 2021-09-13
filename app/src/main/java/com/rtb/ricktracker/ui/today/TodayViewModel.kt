package com.rtb.ricktracker.ui.today

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kizitonwose.calendarview.utils.yearMonth
import com.rtb.ricktracker.AppDatabase
import com.rtb.ricktracker.model.Completion
import com.rtb.ricktracker.model.DayHabit
import com.rtb.ricktracker.model.dao.CompletionDao
import com.rtb.ricktracker.model.dao.HabitDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Integer.max
import java.time.LocalDate
import java.time.YearMonth

data class DayProgress(var completed: Int, var total: Int)

class TodayViewModel(application: Application) : AndroidViewModel(application) {
    private val habitDao: HabitDao = AppDatabase.getDatabase(application).habitDao()
    private val completionDao: CompletionDao = AppDatabase.getDatabase(application).completionDao()

    private val _progress: MutableLiveData<Map<LocalDate, Float>> = MutableLiveData(mapOf())
    private val _dayHabits: MutableLiveData<List<DayHabit>> = MutableLiveData(listOf())

    val progress: LiveData<Map<LocalDate, Float>>
        get() = _progress

    val dayHabits: LiveData<List<DayHabit>>
        get() = _dayHabits

    fun loadMonthHabits(month: YearMonth) {
        val firstDay = month.atDay(1)
        val lastDay = month.atEndOfMonth()
        viewModelScope.launch(Dispatchers.IO) {
            val habits = habitDao.getFromRange(firstDay, lastDay)
            val progressCount = mutableMapOf<LocalDate, DayProgress>()
            for (i in 1..month.lengthOfMonth()) {
                val day = month.atDay(i)
                progressCount[day] = DayProgress(0, 0)
                habits.forEach { habit ->
                    if (habit.habit.days[day.dayOfWeek.value - 1] && habit.habit.startDate <= day && (habit.habit.finished == null || habit.habit.finished >= day)) {
                        progressCount[day]!!.total += 1
                    }
                    for (completion in habit.completions) {
                        if (completion.date == day) {
                            progressCount[day]!!.completed += 1
                            break
                        }
                    }
                }
            }
            val progress =
                progressCount.entries.fold(mutableMapOf<LocalDate, Float>(), { acc, entry ->
                    val total = max(entry.value.total, 1)
                    acc[entry.key] = entry.value.completed.toFloat() / total.toFloat()
                    acc
                })
            _progress.postValue(progress)
        }
    }

    fun loadDayHabits(day: LocalDate) {
        viewModelScope.launch(Dispatchers.IO) {
            val dayOfWeek = day.dayOfWeek.value
            val habits = habitDao.getDayHabits(day).filter { habit ->
                habit.days[dayOfWeek - 1]
            }
            _dayHabits.postValue(habits)
        }
    }

    fun changeHabitStatus(id: Int, selectedDate: LocalDate, status: Boolean) {
        val completion = Completion(selectedDate, id)
        viewModelScope.launch(Dispatchers.IO) {
            if (status) {
                completionDao.insert(completion)
            } else {
                completionDao.delete(completion)
            }
        }
        loadMonthHabits(selectedDate.yearMonth)
    }
}