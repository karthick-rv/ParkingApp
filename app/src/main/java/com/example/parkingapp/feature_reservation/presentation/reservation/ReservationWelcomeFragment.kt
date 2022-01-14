package com.example.parkingapp.feature_reservation.presentation.reservation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.parkingapp.R
import com.example.parkingapp.databinding.FragmentReservationWelcomeBinding

class ReservationWelcomeFragment: Fragment() {

    private lateinit var binding: FragmentReservationWelcomeBinding

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