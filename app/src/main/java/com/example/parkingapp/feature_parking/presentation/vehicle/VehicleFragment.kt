package com.example.parkingapp.feature_parking.presentation.vehicle

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
import com.example.parkingapp.databinding.FragmentVehicleBinding
import com.example.parkingapp.feature_parking.domain.model.Vehicle
import com.example.parkingapp.feature_parking.domain.util.VehicleType
import com.example.parkingapp.feature_parking.presentation.parking_lot.ParkingLotEvent
import com.example.parkingapp.feature_parking.presentation.parking_lot.ParkingLotViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VehicleFragment : Fragment() {

    private lateinit var binding: FragmentVehicleBinding
    private val viewModel by activityViewModels<ParkingLotViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVehicleBinding.inflate(inflater, container, false)
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

        binding.btnParkVehicle.setOnClickListener {
            val vehicleNum = binding.txtInpVehicleNum.editText?.text.toString()
            val vehicleName = binding.txtInpVehicleModel.editText?.text.toString()

            val vehicle = Vehicle(vehicleNum, vehicleName, "", VehicleType.valueOf(vehicleType))
            viewModel.onEvent(ParkingLotEvent.Park(vehicle))
        }

        listenForParkingLot()
    }

    private fun listenForParkingLot() {
        lifecycleScope.launch {
            viewModel.parkingLotFlow.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    val action = VehicleFragmentDirections.actionVehicleFragmentToParkingLotFragment(it)
                    val controller = findNavController()
                    if(controller.currentDestination?.id == R.id.vehicleFragment){
                        controller.navigate(action)
                    }
                }
        }
    }


}