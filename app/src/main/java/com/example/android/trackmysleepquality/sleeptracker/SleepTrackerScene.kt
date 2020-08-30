package com.example.android.trackmysleepquality.sleeptracker

import com.example.android.trackmysleepquality.database.SleepNight

interface SleepTrackerScene {

    fun navToSleepQuality(night: SleepNight)

    fun showSnackBar()
}