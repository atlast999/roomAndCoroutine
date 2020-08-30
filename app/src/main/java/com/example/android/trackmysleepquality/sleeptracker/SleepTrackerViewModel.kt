/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.sleeptracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.formatNights
import kotlinx.coroutines.*

/**
 * ViewModel for SleepTrackerFragment.
 */
class SleepTrackerViewModel(
        val database: SleepDatabaseDao,
        application: Application) : AndroidViewModel(application) {

    private val viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    lateinit var scene: SleepTrackerScene
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val tonight = MutableLiveData<SleepNight?>()
    private val allNights = database.getAllNights().apply {
        observeForever {
            adapter.data = it
        }
    }

    val adapter = SleepNightAdapter()

    val nightString = Transformations.map(allNights){nights ->
        formatNights(nights, application.resources)
    }
    val isTracking = Transformations.map(tonight){
        it != null
    }
    val clearButtonEnabled = Transformations.map(allNights){
        it.isNotEmpty()
    }


    init {
        initializeTonight()
    }

    private fun initializeTonight() {
        uiScope.launch {
            tonight.value = getTonightFromDatabase()
        }
    }

    private suspend fun getTonightFromDatabase(): SleepNight? {
        return withContext(Dispatchers.IO){
            var night = database.getTonight()
            if (night?.startTime != night?.endTime){
                night = null
            }
            night
        }
    }

    fun onStart(){
        uiScope.launch {
            val newNight = SleepNight()
            insert(newNight)
            tonight.value = getTonightFromDatabase()
        }
    }

    private suspend fun insert(newNight: SleepNight) {
        withContext(Dispatchers.IO){
            database.insert(newNight)
        }
    }

    fun onStop(){
        uiScope.launch {
            val night = tonight.value ?: return@launch
            night.endTime = System.currentTimeMillis()
            update(night)
            scene.navToSleepQuality(night)
            tonight.value = null
        }
    }

    private suspend fun update(night: SleepNight?) {
        withContext(Dispatchers.IO){
            if (night != null) {
                database.update(night)
            }
        }
    }

    fun onClear(){
        uiScope.launch {
            tonight.value = null
            clear()
            scene.showSnackBar()
        }
    }
    private suspend fun clear(){
        withContext(Dispatchers.IO){
            database.clear()
        }
    }
}

