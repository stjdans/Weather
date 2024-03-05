package com.example.weathers.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.weathers.R
import com.example.weathers.component.BaseFragment
import com.example.weathers.databinding.FragmentMapBinding
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MapFragment : BaseFragment<FragmentMapBinding>() {
    @Inject
    lateinit var viewModel: MapViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(inflater, container, false)

        (childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment)?.run {
            getMapAsync { googleMap ->
                viewModel.setUp(googleMap)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when {
                        state.infoMessage != null -> {
                            showSnackbar(state.infoMessage)
                        }

                        else -> {
                            state.weather?.also { viewModel.addMarker(requireContext(), it) }
                        }
                    }
                }
            }
        }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun showSnackbar(message: String) {
        val msg = message.ifEmpty { getString(R.string.error_empty) }
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG).show()
    }

}