package com.rtb.ricktracker.util

import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.rtb.ricktracker.R

fun Toolbar.setupMainFragmentNavController(
    navController: NavController,
) {
    val appBarConfiguration = AppBarConfiguration(
        setOf(
            R.id.navigation_today, R.id.navigation_habits
        )
    )
    this.setupWithNavController(navController, appBarConfiguration)
}