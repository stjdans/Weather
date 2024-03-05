package com.example.weathers.ui.forecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weathers.component.BaseFragment
import com.example.weathers.data.toListWithPosition
import com.example.weathers.databinding.FragmentForecastBinding
import com.example.weathers.ui.realtime.RealTimeListAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ForecastFragment : BaseFragment<FragmentForecastBinding>() {
    @Inject
    lateinit var viewModel: ForecastViewModel
    private lateinit var weatherAdapter: ForecastListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForecastBinding.inflate(inflater, container, false)
        weatherAdapter = ForecastListAdapter()

        setUpWeatherList()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.uiState.collect {
                    weatherAdapter.submitList(it.weatherItems.toListWithPosition())
                }
            }
        }

        return binding.root
    }

    private fun setUpWeatherList() {
        binding.weatherRecyclerView.apply {
            adapter = weatherAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }
}