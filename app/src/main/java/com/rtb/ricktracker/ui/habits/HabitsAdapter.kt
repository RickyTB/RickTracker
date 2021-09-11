package com.rtb.ricktracker.ui.habits

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.rtb.ricktracker.R
import com.rtb.ricktracker.databinding.CardHabitBinding
import com.rtb.ricktracker.model.Habit

class HabitsAdapter(
    private var _values: List<Habit> = listOf()
) : RecyclerView.Adapter<HabitsAdapter.ViewHolder>() {

    var values: List<Habit>
        get() = _values
        set(value) {
            _values = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CardHabitBinding.inflate(
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
        holder.numberTxt.text =
            "0/${item.days.fold(0, { acc, value -> acc + if (value) 1 else 0 })}"
        holder.routineTxt.text = holder.routineTxt.context.getString(R.string.no_routine)
        // TODO: Other values.
    }

    override fun getItemCount(): Int = _values.size

    inner class ViewHolder(binding: CardHabitBinding) : RecyclerView.ViewHolder(binding.root) {
        val descriptionTxt = binding.descriptionTxt
        val routineTxt = binding.routineTxt
        val numberTxt = binding.numberTxt
    }

}