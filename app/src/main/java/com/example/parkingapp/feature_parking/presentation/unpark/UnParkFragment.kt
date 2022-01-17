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
import com.example.parkingapp.feature_fee_collection.domain.model.ParkingTicket
import com.example.parkingapp.feature_fee_collection.domain.util.DialogUtil
import com.example.parkingapp.feature_parking.common.Resource
import com.example.parkingapp.feature_parking.domain.model.ParkingLot
import com.example.parkingapp.feature_parking.domain.model.Vehicle
import com.example.parkingapp.feature_parking.domain.util.VehicleType
import com.example.parkingapp.feature_parking.presentation.parking_lot.ParkingLotEvent
import com.example.parkingapp.feature_parking.presentation.parking_lot.ParkingLotViewModel
import com.example.parkingapp.feature_parking.presentation.vehicle.VehicleFragmentDirections
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
        setupRadioButtons()
        binding.btnUnPark.setOnClickListener {
            val parkingTicketNum = binding.txtInpTicketNum.editText?.text.toString()
            val vehicleNum = binding.txtVehicleNum.editText?.text.toString()
            val isReserveChecked = binding.radioBtnAlreadyReserved.isChecked

            val vehicle: Vehicle
            if (!isReserveChecked){
                vehicle = Vehicle(vehicleNum, "", "", VehicleType.CAR, parkingTicketNum = parkingTicketNum)
            }
            else{
                vehicle = Vehicle(vehicleNum, "" , "", VehicleType.CAR, reservationTicketNum = parkingTicketNum)
            }
            viewModel.onEvent(ParkingLotEvent.UnPark(vehicle, binding.radioBtnAlreadyReserved.isChecked))
        }
        listenForParkingTicket()
        listenForUnParkFromReservedSpaceResult()
    }

    private fun listenForUnParkFromReservedSpaceResult() {
        lifecycleScope.launch {
            viewModel.unParkFromReservedResultFlow.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    when(it){
                        is Resource.Error -> { Snackbar.make(
                            requireView(),
                            it.message.toString(), BaseTransientBottomBar.LENGTH_SHORT
                        ).show()}
                        is Resource.Loading -> TODO()
                        is Resource.Success -> {
                            if(it.data == true)
                                showDialog()
                            else{
                                Snackbar.make(
                                    requireView(),
                                    "Input not valid", BaseTransientBottomBar.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
        }
    }

    private fun showDialog() {
        val alertDialog = DialogUtil.create(
            requireContext(),
            "Vehicle UnParked",
            "Your vehicle is successfully unparked from the reserved space",
            "Okay"
        ) { navigateToWelcomeFragment() }
        alertDialog.show()
    }

    private fun setupRadioButtons() {
        binding.radioBtnAlreadyReserved.setOnClickListener {
            binding.apply {
                txtInpTicketNum.hint = getString(R.string.reservation_ticket_num)
            }
        }

        binding.radioBtnNotReserved.setOnClickListener {
            binding.apply {
                txtInpTicketNum.hint = getString(R.string.ticket_number)
            }
        }
    }

    private fun listenForParkingTicket() {
        lifecycleScope.launch {
            viewModel.parkingTicketFlow.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    handleParkingLotResult(it)
                }
        }
    }

    private fun handleParkingLotResult(resource: Resource<ParkingTicket>) {
        when (resource) {
            is Resource.Success -> resource.data?.let {
                navigateToParkingTicketFragment(it)
            }
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

    private fun navigateToParkingTicketFragment(parkingTicket: ParkingTicket) {
        val action = UnParkFragmentDirections.actionUnParkFragmentToParkingTicketFragment(parkingTicket)
        val controller = findNavController()
        if (controller.currentDestination?.id == R.id.unParkFragment) {
            controller.navigate(action)
        }
    }

    private fun navigateToWelcomeFragment() {
        val action =
            UnParkFragmentDirections.actionUnParkFragmentToWelcomeFragment()
        val controller = findNavController()
        if (controller.currentDestination?.id == R.id.unParkFragment) {
            controller.navigate(action)
        }
    }
}