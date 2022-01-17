package com.example.parkingapp.feature_fee_collection.presentation.parking_ticket

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
import androidx.navigation.fragment.navArgs
import com.example.parkingapp.R
import com.example.parkingapp.databinding.FragmentParkingTicketBinding
import com.example.parkingapp.feature_parking.common.Resource
import com.example.parkingapp.feature_parking.domain.model.ParkingLot
import com.example.parkingapp.feature_parking.presentation.parking_lot.ParkingLotEvent
import com.example.parkingapp.feature_parking.presentation.parking_lot.ParkingLotViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ParkingTicketFragment : Fragment() {

    private lateinit var binding: FragmentParkingTicketBinding
    private val parkingLotViewModel by activityViewModels<ParkingLotViewModel>()
    private val args: ParkingTicketFragmentArgs by navArgs()

    companion object{
        const val PARKING_TICKET_ID = "parkingTicketId"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentParkingTicketBinding.inflate(inflater, container, false)
        binding.parkingTicket = args.parkingTicket
        setupViews()
        return binding.root
    }

    private fun setupViews() {
        args.parkingTicket.pickUpTime?.let {
            binding.btnViewParkingLotOrPayment.setOnClickListener {
                navigateToPaymentFragment(args.parkingTicket.ticketId)
            }
        } ?: binding.btnViewParkingLotOrPayment.setOnClickListener {
            navigateToParkingLotFragment()
        }
    }

    private fun navigateToParkingLotFragment() {
        val action =
            ParkingTicketFragmentDirections.actionParkingTicketFragmentToParkingLotFragment()
        navigateToFragment(action)
    }

    private fun navigateToPaymentFragment(parkingTicketId: Long) {
        val action =
            ParkingTicketFragmentDirections.actionParkingTicketFragmentToPaymentFragment(parkingTicketId)
        navigateToFragment(action)
    }

    private fun navigateToFragment(action: NavDirections) {
        val controller = findNavController()
        if (controller.currentDestination?.id == R.id.parkingTicketFragment) {
            controller.navigate(action)
        }
    }

}