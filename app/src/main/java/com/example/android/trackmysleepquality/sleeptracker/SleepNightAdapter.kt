package com.example.android.trackmysleepquality.sleeptracker

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.ListItemSleepNightBinding

class SleepNightAdapter: RecyclerView.Adapter<SleepNightAdapter.ViewHolder>() {


    private val TAG = "SleepNightAdapter"
    var data = listOf<SleepNight>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            sleep = data[position]
            clickListener = object : SleepNightListener {
                override fun onClick(night: SleepNight) {
                    Log.d(TAG, "item clicked: ")
                }
            }
        }

    }

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ListItemSleepNightBinding>(LayoutInflater.from(parent.context), R.layout.list_item_sleep_night, parent, false)
        return ViewHolder(binding)
    }

    class ViewHolder(val binding: ListItemSleepNightBinding) : RecyclerView.ViewHolder(binding.root)
}

interface SleepNightListener{
    fun onClick(night: SleepNight)
}