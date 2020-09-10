
package com.example.android.trackmysleepquality.sleeptracker

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.formatNights
import kotlinx.coroutines.*
import kotlin.math.log

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
            adapter.submitList(it)
        }
    }

    val adapter = SleepNightAdapter {
        Log.d("debugne", "${it.nightId}")
        scene.navToSleepDetail(it)
    }

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
//        uiScope.launch {
//            tonight.value = null
//            clear()
//            scene.showSnackBar()
//        }
        uiScope.launch {
            allNights.value?.let {
                addAllNights(it)
            }
        }
    }
    private suspend fun addAllNights(allNights: List<SleepNight>){
        withContext(Dispatchers.IO){
            allNights.forEach { database.insert(it.clone()) }
        }
    }
    private suspend fun clear(){
        withContext(Dispatchers.IO){
            database.clear()
        }
    }
}

