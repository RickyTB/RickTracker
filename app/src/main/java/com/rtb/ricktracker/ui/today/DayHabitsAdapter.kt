package com.rtb.ricktracker.ui.today

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.rtb.ricktracker.R
import com.rtb.ricktracker.databinding.CardCurrentHabitBinding
import com.rtb.ricktracker.model.Habit

class DayHabitsAdapter(
    private var _values: List<Habit> = listOf()
) : RecyclerView.Adapter<DayHabitsAdapter.ViewHolder>() {

    var values: List<Habit>
        get() = _values
        set(value) {
            _values = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CardCurrentHabitBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = _values[position]
        holder.descriptionTxt.text = item.description
        holder.routineTxt.text = holder.routineTxt.context.getString(R.string.no_routine)
        // TODO: Other values.
    }

    override fun getItemCount(): Int = _values.size

    inner class ViewHolder(binding: CardCurrentHabitBinding) : RecyclerView.ViewHolder(binding.root) {
        val descriptionTxt = binding.descriptionTxt
        val routineTxt = binding.routineTxt
    }

}