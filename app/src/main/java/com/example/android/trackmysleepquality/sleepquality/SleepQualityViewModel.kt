
package com.example.android.trackmysleepquality.sleepquality

import androidx.lifecycle.ViewModel
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import kotlinx.coroutines.*

class SleepQualityViewModel(private val nightKey: Long = 0L,
                            val database: SleepDatabaseDao) : ViewModel(){


    private val viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    lateinit var scene: SleepQualityScene

    fun onQualitySelected(quality: Int){
        uiScope.launch {
            withContext(Dispatchers.IO){
                val tonight = database.get(nightKey) ?: return@withContext
                tonight.sleepQuality = quality
                database.update(tonight)
            }
            scene.navBack()
        }
    }

}