package com.example.weathers.ui.forecast

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weathers.data.model.WeatherWithPosition
import com.example.weathers.databinding.ForecastTimeListItemBinding

class ForecastDetailListAdapter() : ListAdapter<WeatherWithPosition, ForecastDetailListAdapter.WeatherItemViewHolder>(COMPARATOR){

    class WeatherItemViewHolder(val binding: ForecastTimeListItemBinding,itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: WeatherWithPosition, position: Int) {
            binding.item = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherItemViewHolder {
        val binding = ForecastTimeListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

