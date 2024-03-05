package com.example.weathers.ui.realtime

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weathers.data.source.local.Location
import com.example.weathers.databinding.SearchListItemBinding

class SearchListAdapter(val onItemSelect: (String) -> Unit) :
    ListAdapter<Location, SearchListAdapter.SearchItemViewHolder>(COMPARATOR) {

    inner class SearchItemViewHolder(val binding: SearchListItemBinding, itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Location) {
            binding.item = item
            binding.root.setOnClickListener { onItemSelect(item.code) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemViewHolder {
        val binding = SearchListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchItemViewHolder(binding, binding.root)
    }

    override fun onBindViewHolder(holder: SearchItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Location>() {
            override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean = oldItem.code == newItem.code

            override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean = oldItem == newItem
        }
    }

}