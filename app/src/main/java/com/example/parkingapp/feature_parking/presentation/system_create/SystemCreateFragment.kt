package com.example.parkingapp.feature_parking.presentation.system_create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.parkingapp.databinding.FragmentSystemCreateBinding
import com.example.parkingapp.feature_parking.common.Resource
import com.example.parkingapp.feature_parking.domain.model.ParkingLotConfig
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
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
            viewModel.createParkingSystem(floorCount, parkingSpaceCountPerFloor)
            listenForParkingLotConfig()
        }
    }

    private fun listenForParkingLotConfig(){
        lifecycleScope.launch {
            viewModel.parkingLotConfigFlow.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                   when(it){
                       is Resource.Success -> {
                           val action =
                               SystemCreateFragmentDirections.actionSystemCreateFragmentToWelcomeFragment()
                           findNavController().navigate(action)
                       }
                       is Resource.Error -> {
                           it.message?.let { it1 ->
                               Snackbar.make(requireView(), it1, Snackbar.LENGTH_SHORT)
                                   .show()
                           }
                       }
                       else -> {}
                   }
                }
        }
    }
}