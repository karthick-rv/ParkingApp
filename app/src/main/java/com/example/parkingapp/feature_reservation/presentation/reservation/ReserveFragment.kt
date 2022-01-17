package com.example.parkingapp.feature_reservation.presentation.reservation

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.parkingapp.R
import com.example.parkingapp.databinding.FragmentReserveBinding
import com.example.parkingapp.feature_parking.domain.model.Vehicle
import com.example.parkingapp.feature_parking.domain.util.VehicleType
import com.example.parkingapp.feature_parking.presentation.parking_lot.ParkingLotViewModel
import com.example.parkingapp.feature_reservation.presentation.ReservationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class ReserveFragment: Fragment() {

    private lateinit var binding: FragmentReserveBinding
    private val viewModel by activityViewModels<ReservationViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReserveBinding.inflate(inflater, container, false)
        setupViews()
        return binding.root
    }

    private fun setupViews() {
        val vehicleTypes = VehicleType.values()
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item, vehicleTypes)
        binding.vehicleType.setAdapter(arrayAdapter)

        var vehicleType = VehicleType.CAR.name
        binding.vehicleType.setOnItemClickListener { adapterView, _, i, _ ->
            vehicleType = (adapterView.getItemAtPosition(i) as VehicleType).toString()
        }

        binding.btnPickDate.setOnClickListener {
            showDatePicker()
        }

        binding.btnReserveSpace.setOnClickListener {
            val vehicleNum = binding.txtInpVehicleNum.editText?.text.toString()
            val vehicleName = binding.txtInpVehicleModel.editText?.text.toString()

            val vehicle = Vehicle(vehicleNum, vehicleName, "", VehicleType.valueOf(vehicleType))
            val date = binding.tvPickedDate.text
            listenForReservationFees()
            viewModel.onEvent(ReservationEvent.CalculateFees(vehicle, date as String))
        }
    }

    private fun listenForReservationFees() {
        lifecycleScope.launch {
            viewModel.reservationFeesFlow.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    navigateToReservationPaymentFragment(it)
                }
        }
    }

    private fun navigateToReservationPaymentFragment(fees: Float) {
        val action = ReserveFragmentDirections.actionReserveFragmentToReservationPaymentFragment(fees)
        val controller = findNavController()
        if (controller.currentDestination?.id == R.id.reserveFragment) {
            controller.navigate(action)
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val date = calendar.get(Calendar.DAY_OF_MONTH)

        val calendarDialog =
            DatePickerDialog(requireContext(), { _, year, monthOfYear, dayOfMonth ->
                binding.tvPickedDate.text =
                    getString(R.string.reserve_date_format, dayOfMonth, (monthOfYear + 1), year)
            }, year, month, date)

        calendarDialog.show()
    }

}