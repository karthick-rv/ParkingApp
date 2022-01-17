package com.example.parkingapp.feature_reservation.presentation.reservation

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
import com.example.parkingapp.databinding.FragmentReservationWelcomeBinding
import com.example.parkingapp.feature_parking.presentation.parking_lot.ParkingLotViewModel
import com.example.parkingapp.feature_reservation.presentation.ReservationViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ReservationWelcomeFragment: Fragment() {

    private lateinit var binding: FragmentReservationWelcomeBinding
    private val parkingLotViewModel by activityViewModels<ParkingLotViewModel>()
    private val viewModel by activityViewModels<ReservationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReservationWelcomeBinding.inflate(inflater, container, false)
        setupViews()
        return binding.root
    }

    private fun setupViews() {
        binding.btnReserve.setOnClickListener {
            navigateToReserveFragment()
        }

        binding.btnUnReserve.setOnClickListener {
            navigateToUnReserveFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenParkingLotManagerState()
    }

    private fun listenParkingLotManagerState() {
        lifecycleScope.launch {
            parkingLotViewModel.parkingLotManagerFlow.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collectLatest {
                    viewModel.configure(it)
                }
        }
    }

    private fun navigateToUnReserveFragment() {
        val action = ReservationWelcomeFragmentDirections.actionReservationWelcomeFragmentToUnReserveFragment()
        val controller = findNavController()
        if (controller.currentDestination?.id == R.id.reservationWelcomeFragment) {
            controller.navigate(action)
        }
    }

    private fun navigateToReserveFragment() {
        val action = ReservationWelcomeFragmentDirections.actionReservationWelcomeFragmentToReserveFragment()
        val controller = findNavController()
        if (controller.currentDestination?.id == R.id.reservationWelcomeFragment) {
            controller.navigate(action)
        }
    }
}