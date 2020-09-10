package com.example.android.trackmysleepquality.sleepdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight

class SleepDetailViewModel(
        nightId: Long = 0L,
        val database: SleepDatabaseDao
): ViewModel() {

    private val night: LiveData<SleepNight?> = database.getNightWithId(nightId)

    lateinit var scene: SleepDetailScene

    fun getNight() = night
    fun onClose(){
        scene.onClose()
    }
}