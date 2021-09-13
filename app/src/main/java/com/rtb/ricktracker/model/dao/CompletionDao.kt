package com.rtb.ricktracker.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import com.rtb.ricktracker.model.Completion

@Dao
interface CompletionDao {
    @Insert(onConflict = IGNORE)
    fun insert(completion: Completion)

    @Delete
    fun delete(completion: Completion)
}