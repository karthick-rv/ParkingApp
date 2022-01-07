package com.example.parkingapp.feature_parking.presentation.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.parkingapp.R
import com.example.parkingapp.databinding.FragmentWelcomeBinding
import com.example.parkingapp.feature_parking.domain.model.ParkingLot
import com.example.parkingapp.feature_parking.domain.model.ParkingLotConfig
import com.example.parkingapp.feature_parking.presentation.parking_lot.ParkingLotEvent
import com.example.parkingapp.feature_parking.presentation.parking_lot.ParkingLotViewModel
import com.example.parkingapp.feature_parking.presentation.system_create.SystemConfigManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class WelcomeFragment : Fragment(R.layout.fragment_welcome) {

    private lateinit var binding: FragmentWelcomeBinding
    private val viewModel by activityViewModels<ParkingLotViewModel>()

    @Inject
    lateinit var systemConfigManager: SystemConfigManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        setupViews()
        return binding.root
    }

    private fun setupViews() {
        binding.btnPark.setOnClickListener {
            navigateToVehicleFragment()
        }
        binding.btnshowStatus.setOnClickListener {
            viewModel.onEvent(ParkingLotEvent.ShowLotStatus)
            listenForParkingLot()
        }
    }

    private fun listenForParkingLot() {
        lifecycleScope.launch {
            viewModel.parkingLotFlow.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    navigateToParkingLotFragment(it)
                }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        runBlocking {
            val floorCount =  systemConfigManager.systemFloorCountFlow.firstOrNull()!!
            val parkingSpaceCount = systemConfigManager.systemParkingSpaceCountFlow.firstOrNull()!!
            viewModel.configure(parkingLotConfig = ParkingLotConfig("", floorCount, parkingSpaceCount))
        }
    }

    private fun navigateToVehicleFragment() {
        val controller = findNavController()
        if (controller.currentDestination?.id == R.id.welcomeFragment) {
            controller.navigate(R.id.action_welcomeFragment_to_vehicleFragment)
        }
    }

    private fun navigateToParkingLotFragment(parkingLot: ParkingLot) {
        val action =
            WelcomeFragmentDirections.actionWelcomeFragmentToParkingLotFragment(parkingLot = parkingLot)
        val controller = findNavController()
        if (controller.currentDestination?.id == R.id.welcomeFragment) {
            controller.navigate(action)
        }
    }

}