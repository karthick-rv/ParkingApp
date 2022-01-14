package com.example.parkingapp.feature_reservation.presentation.reservation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.parkingapp.R
import com.example.parkingapp.databinding.FragmentReservationTicketBinding

class ReservationTicketFragment: Fragment() {

    private lateinit var binding: FragmentReservationTicketBinding
    private val args by navArgs<ReservationTicketFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReservationTicketBinding.inflate(inflater, container, false)
        binding.reservationTicket = args.reservationTicket
        setupViews()
        return binding.root
    }

    private fun setupViews() {
        binding.goToHome.setOnClickListener {
            navigateToWelcomeFragment()
        }
    }

    private fun navigateToWelcomeFragment() {
        val action = ReservationTicketFragmentDirections.actionReservationTicketFragmentToReservationWelcomeFragment()
        val controller = findNavController()
        if (controller.currentDestination?.id == R.id.reservationTicketFragment) {
            controller.navigate(action)
        }
    }

}