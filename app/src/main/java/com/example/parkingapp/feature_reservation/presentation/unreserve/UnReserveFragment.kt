package com.example.parkingapp.feature_reservation.presentation.unreserve

import android.app.AlertDialog
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
import com.example.parkingapp.databinding.FragmentUnreserveBinding
import com.example.parkingapp.feature_parking.domain.model.Vehicle
import com.example.parkingapp.feature_parking.domain.util.VehicleType
import com.example.parkingapp.feature_reservation.presentation.ReservationViewModel
import com.example.parkingapp.feature_reservation.presentation.reservation.ReservationEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UnReserveFragment: Fragment() {

    private lateinit var binding: FragmentUnreserveBinding
    private val viewModel by activityViewModels<ReservationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUnreserveBinding.inflate(inflater, container, false)
        setupViews()
        return binding.root
    }

    private fun setupViews() {
        binding.btnUnReserve.setOnClickListener {
            val parkingTicketNum = binding.txtInpTicketNum.editText?.text.toString()
            val vehicleNum = binding.txtVehicleNum.editText?.text.toString()
            val vehicle = Vehicle(vehicleNum,"", "", VehicleType.CAR,parkingTicketNum)
            viewModel.onEvent(ReservationEvent.UnReserveParkingSpace(vehicle))
            listenForUnReserveResult()
        }
    }

    private fun listenForUnReserveResult() {
        lifecycleScope.launch {
            viewModel.unReserveResult.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    if(it) showDialog()
                }
        }
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Vehicle UnReserved")
        builder.setMessage("Your amount will be refunded to your account. Thank you")
        builder.setPositiveButton("Okay") { dialog, _ ->
            dialog.dismiss()
            navigateToWelcomeFragment()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun navigateToWelcomeFragment() {
        val action = UnReserveFragmentDirections.actionUnReserveFragmentToReservationWelcomeFragment()
        val controller = findNavController()
        if (controller.currentDestination?.id == R.id.unReserveFragment) {
            controller.navigate(action)
        }
    }
}