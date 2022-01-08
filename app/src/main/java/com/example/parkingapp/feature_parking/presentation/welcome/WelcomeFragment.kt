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
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.parkingapp.R
import com.example.parkingapp.databinding.FragmentWelcomeBinding
import com.example.parkingapp.feature_parking.common.Resource
import com.example.parkingapp.feature_parking.domain.model.ParkingLot
import com.example.parkingapp.feature_parking.domain.model.ParkingLotConfig
import com.example.parkingapp.feature_parking.presentation.parking_lot.ParkingLotEvent
import com.example.parkingapp.feature_parking.presentation.parking_lot.ParkingLotViewModel
import com.example.parkingapp.feature_parking.presentation.system_create.SystemConfigManager
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar
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
            viewModel.getParkingLotOccupancyStatus()
            listenForParkingLotStatusToPark()
        }
        binding.btnshowStatus.setOnClickListener {
            viewModel.onEvent(ParkingLotEvent.ShowParkingLot)
            listenForParkingLot()
        }
        binding.btnUnPark.setOnClickListener {
            viewModel.getParkedSpaces()
            listenForParkedSpaces()
        }
    }

    private fun listenForParkingLotStatusToPark() {
        lifecycleScope.launch {
            viewModel.isParkingLotFullFlow.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    if(!it)
                        navigateToVehicleFragment()
                    else
                        Snackbar.make(
                            requireView(),
                            "Parking Lot is Full. Try after some time", LENGTH_SHORT
                        ).show()
                }
        }
    }

    private fun listenForParkedSpaces() {
        lifecycleScope.launch {
            viewModel.parkedSpacesFlow.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    if (it.isNotEmpty()) {
                        navigateToUnParkFragment()
                    } else {
                        Snackbar.make(
                            requireView(),
                            "No vehicle available to unpark", LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun listenForParkingLot() {
        lifecycleScope.launch {
            viewModel.parkingLotFlow.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    if (it is Resource.Success)
                        it.data?.let { it1 -> navigateToParkingLotFragment(it1) }
                }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        runBlocking {
            val parkingLotConfig = systemConfigManager.getSystemConfig().firstOrNull()
            parkingLotConfig?.let {
                viewModel.configure(
                    parkingLotConfig = ParkingLotConfig(
                        "",
                        it.floorCount,
                        it.parkingSpaceCount
                    )
                )
            }
        }
    }

    private fun navigateToVehicleFragment() {
        val action = WelcomeFragmentDirections.actionWelcomeFragmentToVehicleFragment()
        navigateToFragment(action)
    }

    private fun navigateToParkingLotFragment(parkingLot: ParkingLot) {
        val action =
            WelcomeFragmentDirections.actionWelcomeFragmentToParkingLotFragment(parkingLot = parkingLot)
        navigateToFragment(action)
    }

    private fun navigateToUnParkFragment() {
        val action = WelcomeFragmentDirections.actionWelcomeFragmentToUnParkFragment()
        navigateToFragment(action)
    }

    private fun navigateToFragment(action: NavDirections) {
        val controller = findNavController()
        if (controller.currentDestination?.id == R.id.welcomeFragment) {
            controller.navigate(action)
        }
    }
}