package com.example.weathers.ui.forecast

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weathers.data.model.EmptyDescriptor
import com.example.weathers.data.model.Weather
import com.example.weathers.data.model.WeatherWithPosition
import com.example.weathers.data.toListWithPosition
import com.example.weathers.data.toTimeBaseList
import com.example.weathers.databinding.ForecastListItemBinding

class ForecastListAdapter() : ListAdapter<WeatherWithPosition, ForecastListAdapter.WeatherItemViewHolder>(COMPARATOR){

    class WeatherItemViewHolder(val binding: ForecastListItemBinding,itemView : View) : RecyclerView.ViewHolder(itemView) {
        init {
            setUpWeatherList()
        }

        fun bind(item: WeatherWithPosition, position: Int) {
            binding.weatherUi.item = item
            (binding.horizonForecastList.adapter as ForecastDetailListAdapter).submitList(
                item.weather.toTimeBaseList().toMutableList().apply {
                    add(0, Weather(descriptor = EmptyDescriptor()))
                }.toListWithPosition()
            )
        }

        private fun setUpWeatherList() {
            binding.horizonForecastList.apply {
                adapter = ForecastDetailListAdapter()
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                addItemDecoration(ForecastDecoration())
                addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.HORIZONTAL))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherItemViewHolder {
        val binding = ForecastListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherItemViewHolder(binding, binding.root)
    }

    override fun onBindViewHolder(holder: WeatherItemViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }



    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<WeatherWithPosition>() {
            override fun areItemsTheSame(oldItem: WeatherWithPosition, newItem: WeatherWithPosition) =
                oldItem.weather.code == newItem.weather.code && oldItem.position == newItem.position

            override fun areContentsTheSame(oldItem: WeatherWithPosition, newItem: WeatherWithPosition) = areItemsTheSame(oldItem, newItem)
        }
    }
}

class ForecastDecoration() : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.left = 20
        outRect.right = 20
    }
}

