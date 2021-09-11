package com.rtb.ricktracker.ui.createhabit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rtb.ricktracker.AppDatabase
import com.rtb.ricktracker.model.Habit
import com.rtb.ricktracker.model.dao.HabitDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateHabitViewModel(application: Application) : AndroidViewModel(application) {
    private val habitDao: HabitDao = AppDatabase.getDatabase(application).habitDao()

    private var _loading: MutableLiveData<Boolean> = MutableLiveData(false)

    val loading: LiveData<Boolean>
        get() = _loading

    fun createHabit(habit: Habit) {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            habitDao.insert(habit)
            _loading.postValue(false)
        }
    }

}
