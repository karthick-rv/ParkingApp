package com.example.parkingapp.feature_reservation.presentation.payment

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
import androidx.navigation.fragment.navArgs
import com.example.parkingapp.R
import com.example.parkingapp.databinding.FragmentReservationPaymentBinding
import com.example.parkingapp.feature_parking.common.Resource
import com.example.parkingapp.feature_reservation.domain.model.ReservationTicket
import com.example.parkingapp.feature_reservation.presentation.ReservationViewModel
import com.example.parkingapp.feature_reservation.presentation.reservation.ReservationEvent
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReservationPaymentFragment: Fragment() {

    private lateinit var binding: FragmentReservationPaymentBinding
    private val viewModel by activityViewModels<ReservationViewModel>()
    private val args by navArgs<ReservationPaymentFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReservationPaymentBinding.inflate(inflater, container, false)
        setupViews()
        return binding.root
    }

    private fun setupViews() {
        binding.amountValue.text = getString(R.string.rupees_value, args.reservationFee.toString())

        binding.btnPayFee.setOnClickListener {
            viewModel.onEvent(ReservationEvent.ReserveParkingSpace)
        }
        listenForReservationResult()
    }

    private fun listenForReservationResult() {
        lifecycleScope.launch {
            viewModel.reservationTicket.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    when(it){
                        is Resource.Error -> {
                            Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_SHORT)
                                .show()
                        }
                        is Resource.Loading -> TODO()
                        is Resource.Success -> {
                            it.data?.let { ticket -> navigateToReservationTicketFragment(ticket) }
                        }
                    }
                }
        }
    }

    private fun navigateToReservationTicketFragment(reservationTicket: ReservationTicket) {
        val action = ReservationPaymentFragmentDirections.actionReservationPaymentFragmentToReservationTicketFragment(reservationTicket)
        val controller = findNavController()
        if (controller.currentDestination?.id == R.id.reservationPaymentFragment) {
            controller.navigate(action)
        }
    }

}