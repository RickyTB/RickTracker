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

        habitsViewModel.text.observe(viewLifecycleOwner, Observer {
            //TODO: Use the viewmodel
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}