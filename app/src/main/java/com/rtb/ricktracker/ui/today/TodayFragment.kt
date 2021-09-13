package com.rtb.ricktracker.ui.today

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.rtb.ricktracker.R
import com.rtb.ricktracker.databinding.FragmentTodayBinding
import com.rtb.ricktracker.util.*
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class TodayFragment : Fragment(), HabitStatusListener {

    private var selectedDate: LocalDate? = null
    private val today = LocalDate.now()

    private val selectionFormatter = DateTimeFormatter.ofPattern("d MMM yyyy")
    private val daysOfWeek = daysOfWeekFromLocale()
    private val currentMonth = YearMonth.now()
    private var monthProgress: Map<LocalDate, Float>? = null

    private lateinit var homeViewModel: TodayViewModel
    private var _binding: FragmentTodayBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val dayBinder = object : DayBinder<DayViewContainer> {
        override fun create(view: View) = DayViewContainer(view, ::selectDate)
        override fun bind(container: DayViewContainer, day: CalendarDay) {
            container.day = day
            val progressView = container.binding.progressView
            val guideline = container.binding.guideline
            val layout = container.binding.dayLayout
            val borderLayout = container.binding.borderLayout
            val textView = container.binding.dayTxt
            textView.text = day.date.dayOfMonth.toString()

            if (day.owner == DayOwner.THIS_MONTH) {
                textView.makeVisible()
                progressView.makeVisible()
                layout.setBackgroundResource(R.drawable.day_box)
                val progress = monthProgress?.get(day.date) ?: 0F
                guideline.setGuidelinePercent(1 - progress)
                when {
                    progress > 0.66F -> progressView.setBackgroundResource(R.drawable.day_progress_success)
                    progress > 0.33F -> progressView.setBackgroundResource(R.drawable.day_progress_warning)
                    else -> progressView.setBackgroundResource(R.drawable.day_progress_error)
                }

                if (day.date == today) {
                    textView.setTypeface(null, Typeface.BOLD);
                } else {
                    textView.setTypeface(null, Typeface.NORMAL);
                }
                if (day.date == selectedDate) {
                    borderLayout.setBackgroundResource(R.drawable.day_box_selected)
                } else {
                    borderLayout.background = null
                }

            } else {
                textView.makeInVisible()
                progressView.makeInVisible()
                layout.background = null
                borderLayout.background = null
            }
        }
    }

    private val monthHeaderBinder = object :
        MonthHeaderFooterBinder<MonthViewContainer> {
        override fun create(view: View) = MonthViewContainer(view)
        override fun bind(container: MonthViewContainer, month: CalendarMonth) {
            // Setup each header day text if we have not done that already.
            if (container.legendLayout.tag == null) {
                container.legendLayout.tag = month.yearMonth
                container.legendLayout.children.map { it as TextView }.forEachIndexed { index, tv ->
                    tv.text = daysOfWeek[index].name.first().toString()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(TodayViewModel::class.java)

        _binding = FragmentTodayBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val calendarView = binding.calendarView
        calendarView.apply {
            setup(currentMonth.minusMonths(10), currentMonth.plusMonths(10), daysOfWeek.first())
            scrollToMonth(currentMonth)
        }

        if (savedInstanceState == null) {
            calendarView.post {
                selectDate(today)
            }
        }

        calendarView.dayBinder = dayBinder
        calendarView.monthScrollListener = {
            homeViewModel.loadMonthHabits(it.yearMonth)
            selectDate(it.yearMonth.atDay(1))
        }
        calendarView.monthHeaderBinder = monthHeaderBinder

        homeViewModel.progress.observe(viewLifecycleOwner, {
            monthProgress = it
            calendarView.notifyCalendarChanged()
        })

        val adapter = DayHabitsAdapter(this)
        val recyclerView = binding.recycler
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        homeViewModel.dayHabits.observe(viewLifecycleOwner, {
            adapter.values = it
            if (it.isEmpty()) {
                binding.selectedDateLabel.makeVisible()
            } else {
                binding.selectedDateLabel.makeGone()
            }
        })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()
        binding.toolbar.setupMainFragmentNavController(navController)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun selectDate(date: LocalDate) {
        if (selectedDate != date) {
            val oldDate = selectedDate
            selectedDate = date
            oldDate?.let { binding.calendarView.notifyDateChanged(it) }
            binding.calendarView.notifyDateChanged(date)
            updateAdapterForDate(date)
        }
    }

    private fun updateAdapterForDate(date: LocalDate) {
        homeViewModel.loadDayHabits(date)
        binding.toolbar.title = selectionFormatter.format(date)
    }

    override fun onHabitStatusChange(id: Int, status: Boolean) {
        homeViewModel.changeHabitStatus(id, selectedDate!!, status)
    }

}