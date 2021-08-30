package com.rtb.ricktracker.ui.today

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.rtb.ricktracker.R
import com.rtb.ricktracker.databinding.FragmentTodayBinding
import com.rtb.ricktracker.util.daysOfWeekFromLocale
import com.rtb.ricktracker.util.makeInVisible
import com.rtb.ricktracker.util.makeVisible
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class TodayFragment : Fragment() {

    private var selectedDate: LocalDate? = null
    private val today = LocalDate.now()

    private val titleSameYearFormatter = DateTimeFormatter.ofPattern("MMMM")
    private val titleFormatter = DateTimeFormatter.ofPattern("MMM yyyy")
    private val selectionFormatter = DateTimeFormatter.ofPattern("d MMM yyyy")
    private val daysOfWeek = daysOfWeekFromLocale()
    private val currentMonth = YearMonth.now()

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
                layout.setBackgroundResource(R.color.dayBackgroundColor)
                guideline.setGuidelinePercent((0..10).random() / 10.0F)

                if (day.date == today) {
                    textView.setTypeface(null, Typeface.BOLD);
                } else {
                    textView.setTypeface(null, Typeface.NORMAL);
                }

                when (day.date) {
                    selectedDate -> {
                        //textView.setTextColorRes(R.color.example_3_blue)
                        borderLayout.setBackgroundResource(R.color.secondaryColor)
                        //dotView.makeInVisible()
                    }
                    else -> {
                        borderLayout.background = null
                        //textView.setTextColorRes(R.color.example_3_black)
                        //dotView.isVisible = events[day.date].orEmpty().isNotEmpty()
                    }
                }
            } else {
                textView.makeInVisible()
                progressView.makeInVisible()
                layout.background = null
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
                    //tv.setTextColorRes(R.color.example_3_black)
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
                // Show today's events initially.
                selectDate(today)
            }
        }

        calendarView.dayBinder = dayBinder
        calendarView.monthScrollListener = {
/*
            homeActivityToolbar.title = if (it.year == today.year) {
                titleSameYearFormatter.format(it.yearMonth)
            } else {
                titleFormatter.format(it.yearMonth)
            }
*/
            // Select the first day of the month when
            // we scroll to a new month.
            selectDate(it.yearMonth.atDay(1))
        }
        calendarView.monthHeaderBinder = monthHeaderBinder

        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            //TODO: do something.
        })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_today, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
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
/*
        eventsAdapter.apply {
            events.clear()
            events.addAll(this@Example3Fragment.events[date].orEmpty())
            notifyDataSetChanged()
        }
*/
        binding.toolbar.title = selectionFormatter.format(date)
    }

}