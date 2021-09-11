package com.rtb.ricktracker.ui.habits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rtb.ricktracker.databinding.FragmentHabitsBinding
import com.rtb.ricktracker.util.setupMainFragmentNavController

class HabitsFragment : Fragment() {

    private lateinit var habitsViewModel: HabitsViewModel
    private var _binding: FragmentHabitsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        habitsViewModel =
            ViewModelProvider(this).get(HabitsViewModel::class.java)

        _binding = FragmentHabitsBinding.inflate(inflater, container, false)

        val navController = findNavController()
        binding.toolbar.setupMainFragmentNavController(navController)

        binding.addHabitBtn.setOnClickListener {
            it.findNavController().navigate(HabitsFragmentDirections.toCreateHabitFragment())
        }

        val adapter = HabitsAdapter()
        val recyclerView = binding.habitRecycler
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        habitsViewModel.getHabits.observe(viewLifecycleOwner, Observer {
            adapter.values = it
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}