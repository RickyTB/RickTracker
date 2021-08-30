package com.rtb.ricktracker.ui.today

import android.view.View
import com.kizitonwose.calendarview.ui.ViewContainer
import com.rtb.ricktracker.databinding.LayoutCalendarHeaderBinding

class MonthViewContainer(view: View) : ViewContainer(view) {
    val legendLayout = LayoutCalendarHeaderBinding.bind(view).legendLayout.root
}
