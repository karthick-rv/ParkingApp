package com.example.parkingapp.feature_parking.presentation.unpark

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
import com.example.parkingapp.databinding.FragmentUnparkBinding
import com.example.parkingapp.feature_parking.common.Resource
import com.example.parkingapp.feature_parking.domain.model.ParkingLot
import com.example.parkingapp.feature_parking.domain.model.Vehicle
import com.example.parkingapp.feature_parking.domain.util.VehicleType
import com.example.parkingapp.feature_parking.presentation.parking_lot.ParkingLotEvent
import com.example.parkingapp.feature_parking.presentation.parking_lot.ParkingLotViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UnParkFragment: Fragment() {

    private lateinit var binding: FragmentUnparkBinding
    val viewModel by activityViewModels<ParkingLotViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUnparkBinding.inflate(inflater, container, false)
        setupViews()
        return binding.root
    }

    private fun setupViews() {

        binding.btnUnPark.setOnClickListener {
            val vehicleNum = binding.txtInpVehicleNum.editText?.text.toString()
            val parkingSpaceNum = binding.txtParkingSpaceNum.editText?.text.toString()
            val vehicle = Vehicle(vehicleNum, "", parkingSpaceNum, VehicleType.CAR)
            viewModel.onEvent(ParkingLotEvent.UnPark(vehicle))
            listenForParkingLot()
        }
    }

    private fun listenForParkingLot() {
        lifecycleScope.launch {
            viewModel.parkingLotFlow.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    handleParkingLotResult(it)
                }
        }
    }

    private fun handleParkingLotResult(resource: Resource<ParkingLot>) {
        when (resource) {
            is Resource.Success -> resource.data?.let { navigateToParkingLotFragment(it) }
            is Resource.Loading -> {
            }
            is Resource.Error -> {
                resource.message?.let {
                    Snackbar.make(requireView(),
                        it, BaseTransientBottomBar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun navigateToParkingLotFragment(parkingLot: ParkingLot) {
        val action = UnParkFragmentDirections.actionUnParkFragmentToParkingLotFragment(parkingLot)
        val controller = findNavController()
        if (controller.currentDestination?.id == R.id.unParkFragment) {
            controller.navigate(action)
        }
    }

}