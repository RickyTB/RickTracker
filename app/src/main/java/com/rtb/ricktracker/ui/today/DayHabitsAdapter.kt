package com.rtb.ricktracker.ui.today

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.rtb.ricktracker.R
import com.rtb.ricktracker.databinding.CardCurrentHabitBinding
import com.rtb.ricktracker.model.DayHabit

class DayHabitsAdapter(
    private val habitStatusListener: HabitStatusListener,
    private var _values: List<DayHabit> = listOf(),
) : RecyclerView.Adapter<DayHabitsAdapter.ViewHolder>() {

    var values: List<DayHabit>
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
        holder.durationTxt.text = "${item.duration} ${item.durationUnit}"
        holder.checkBox.setOnCheckedChangeListener { _, value ->
            habitStatusListener.onHabitStatusChange(item.uid, value)
        }
        holder.checkBox.isChecked = item.completed != null
    }

    override fun getItemCount(): Int = _values.size

    inner class ViewHolder(binding: CardCurrentHabitBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val descriptionTxt = binding.descriptionTxt
        val routineTxt = binding.routineTxt
        val durationTxt = binding.durationTxt
        val checkBox = binding.checkBox
    }

}

interface HabitStatusListener {
    fun onHabitStatusChange(id: Int, status: Boolean)
}