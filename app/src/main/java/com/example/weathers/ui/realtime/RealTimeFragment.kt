package com.example.weathers.ui.realtime

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weathers.R
import com.example.weathers.component.BaseFragment
import com.example.weathers.data.toListWithPosition
import com.example.weathers.databinding.FragmentRealtimeBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RealTimeFragment : BaseFragment<FragmentRealtimeBinding>() {
    @Inject
    lateinit var viewModel: RealTimeViewModel

    private lateinit var weatherAdapter: RealTimeListAdapter
    private lateinit var searchAdater: SearchListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRealtimeBinding.inflate(inflater, container, false)
        weatherAdapter = RealTimeListAdapter(::deleteUserLocatin)
        searchAdater = SearchListAdapter(::setUserLocatin)

        setUpWeatherList()
        setUpSearchList()
        setUpSerchViewListener()

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.searchView.isShowing)
                    binding.searchView.hide()
                else {
                    if (!findNavController().popBackStack())
                        activity?.finish()
                }
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, callback)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.uiState.collect { uiState ->
                    uiState.infoMessage?.let(::showSnackbar)

                    weatherAdapter.submitList(uiState.weatherItems.toListWithPosition())
                    searchAdater.submitList(uiState.searchItems)
                }
            }
        }

        return binding.root
    }

    private fun showSnackbar(message: String) {
        val msg = message.ifEmpty { getString(R.string.error_empty) }
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG).show()
    }

    private fun setUpWeatherList() {
        binding.weatherRecyclerView.apply {
            adapter = weatherAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setUpSearchList() {
        binding.searchRecyclerView.apply {
            adapter = searchAdater
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setUpSerchViewListener() {
        binding.searchView.apply {
            editText.setOnEditorActionListener { v, actionId, event ->
                binding.searchBar.setText(v.text)
                return@setOnEditorActionListener true
            }

            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    viewModel.search(s.toString())
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }

    private fun setUserLocatin(code: String) {
        if (binding.searchView.isShowing)
            binding.searchView.hide()

        viewModel.setUserLocatin(code)
    }

    private fun deleteUserLocatin(code: String) {
        showSnackbar(getString(R.string.delete_item_info))
        viewModel.deleteUserLocatin(code)
    }
}