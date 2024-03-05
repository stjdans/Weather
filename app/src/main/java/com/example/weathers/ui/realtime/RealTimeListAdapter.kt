package com.example.weathers.ui.realtime

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.weathers.R
import com.example.weathers.data.model.WeatherStaus
import com.example.weathers.data.model.WeatherWithPosition
import com.example.weathers.databinding.EmptyListItemBinding
import com.example.weathers.databinding.RealtimeListItemBinding

class RealTimeListAdapter(val onItemLongClick: (String) -> Unit) : ListAdapter<WeatherWithPosition, RealTimeListAdapter.WeatherItemViewHolder>(COMPARATOR){

    inner class WeatherItemViewHolder(val binding: ViewBinding, itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: WeatherWithPosition, position: Int) {
            when (binding) {
                is RealtimeListItemBinding -> {
                    binding.weatherUi.item = item
                    if (item.position != 0) {
                        binding.root.setOnLongClickListener {
                            onItemLongClick(item.weather.code)
                            return@setOnLongClickListener true
                        }
                    }
                }
                is EmptyListItemBinding -> {
                    binding.item = item
                }
                else -> {
                }
            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        val state = getItem(position).weather.status
        return when (state) {
            WeatherStaus.Success -> R.layout.realtime_list_item
            else -> R.layout.empty_list_item
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherItemViewHolder {
        val binding: ViewBinding
        when (viewType) {
            R.layout.realtime_list_item -> binding = RealtimeListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            else -> binding = EmptyListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        }

        return WeatherItemViewHolder(binding, binding.root)
    }

    override fun onBindViewHolder(holder: WeatherItemViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<WeatherWithPosition>() {
            override fun areItemsTheSame(oldItem: WeatherWithPosition, newItem: WeatherWithPosition) = oldItem.position == newItem.position

            override fun areContentsTheSame(oldItem: WeatherWithPosition, newItem: WeatherWithPosition): Boolean {
                val ow = oldItem.weather
                val nw = newItem.weather
                return ow.code == nw.code && ow.baseDate == nw.baseDate && ow.baseTime == nw.baseTime
            }

        }
    }
}

