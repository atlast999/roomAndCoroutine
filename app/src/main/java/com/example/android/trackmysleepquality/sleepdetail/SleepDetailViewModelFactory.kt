package com.example.android.trackmysleepquality.sleepdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.sleepquality.SleepQualityViewModel
import java.lang.IllegalArgumentException

class SleepDetailViewModelFactory(
        private val night: Long,
        private val databaseDao: SleepDatabaseDao
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SleepDetailViewModel::class.java)){
            return SleepDetailViewModel(night, databaseDao) as T
        }
        throw IllegalArgumentException("unknown ViewModel class")
    }

}