package com.rtb.ricktracker.ui.createhabit

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.rtb.ricktracker.R
import com.rtb.ricktracker.databinding.FragmentCreateHabitBinding
import com.rtb.ricktracker.model.Habit
import java.time.LocalDate
import java.util.*

class CreateHabitFragment : Fragment() {

    private lateinit var viewModel: CreateHabitViewModel
    private var _binding: FragmentCreateHabitBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var selectedDate: LocalDate = LocalDate.now()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProvider(this).get(CreateHabitViewModel::class.java)

        _binding = FragmentCreateHabitBinding.inflate(inflater, container, false)

        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        val unitOptions = resources.getStringArray(R.array.unit_options)
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, unitOptions)
        binding.unitSelect.setAdapter(adapter)

        binding.calendarView.minDate = Date().time

        viewModel.loading.observe(viewLifecycleOwner, {
            binding.createBtn.isEnabled = !it
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.calendarView.setOnDateChangeListener { _, year, month, day ->
            selectedDate = LocalDate.of(year, month + 1, day)
        }

        binding.createBtn.setOnClickListener {
            onCreateHabit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onCreateHabit() {
        val description = binding.descriptionTxt.text.toString()
        val days = listOf(
            binding.monChip.isChecked,
            binding.tueChip.isChecked,
            binding.wedChip.isChecked,
            binding.thuChip.isChecked,
            binding.friChip.isChecked,
            binding.satChip.isChecked,
            binding.sunChip.isChecked,
        )
        val durationUnit = binding.unitSelect.text.toString()
        val duration = binding.durationTxt.text.toString()

        // TODO: Validations

        val newHabit = Habit(0, description, selectedDate, duration.toInt(), durationUnit, days)
        viewModel.createHabit(newHabit)
        Toast.makeText(requireContext(), getString(R.string.habit_added), Toast.LENGTH_LONG).show()
        findNavController().navigateUp()
    }

}