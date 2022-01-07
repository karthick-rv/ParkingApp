package com.example.parkingapp.feature_parking.presentation.system_create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.parkingapp.databinding.FragmentSystemCreateBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SystemCreateFragment : Fragment() {

    private lateinit var binding: FragmentSystemCreateBinding

    private val viewModel by viewModels<SystemCreateViewModel>()

    @Inject
    lateinit var systemConfigManager: SystemConfigManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSystemCreateBinding.inflate(inflater, container, false)
        setupViews()
        return binding.root
    }

    private fun setupViews() {
        binding.btnCreateSystem.setOnClickListener {
            val floorCount: String = binding.txtInputNumOfFloors.editText?.text.toString()
            val parkingSpaceCountPerFloor: String =
                binding.txtInputNumOfParkingSpace.editText?.text.toString()
            val response = viewModel.createParkingSystem(floorCount, parkingSpaceCountPerFloor)
            handleResponse(response)
        }
    }

    private fun handleResponse(response: SystemCreateResponse) {
        when (response) {
            is SystemCreateResponse.Success -> {
                val action =
                    SystemCreateFragmentDirections.actionSystemCreateFragmentToWelcomeFragment()
                lifecycleScope.launch {
                    systemConfigManager.storeSystemCreation(
                        floorCount = response.floorCount,
                        parkingSpaceCount = response.parkingSpaceCount
                    )
                }
                findNavController().navigate(action)
            }
            is SystemCreateResponse.Error -> {
                Snackbar.make(requireView(), response.message, Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
    }
}