package com.example.android.trackmysleepquality.sleepdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.FragmentSleepDetailBinding

class SleepDetailFragment: Fragment(), SleepDetailScene {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentSleepDetailBinding.inflate(inflater, container, false)

        val application = requireNotNull(this.activity).application
        val database = SleepDatabase.getInstance(application).sleepDatabaseDao
        val nightId = SleepDetailFragmentArgs.fromBundle(arguments!!).nightKey
        val factory = SleepDetailViewModelFactory(nightId, database)
        val viewModel = ViewModelProvider(this, factory).get(SleepDetailViewModel::class.java)
        viewModel.scene = this

        binding.lifecycleOwner = this
        binding.sleepDetailViewModel = viewModel
        return binding.root
    }

    override fun onClose() {
        activity?.onBackPressed()
    }
}