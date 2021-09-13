package com.rtb.ricktracker.model

import androidx.room.Embedded
import androidx.room.Relation

data class HabitWithCompletions(
    @Embedded val habit: Habit,
    @Relation(
        parentColumn = "uid",
        entityColumn = "habit_id"
    )
    val completions: List<Completion>
)
