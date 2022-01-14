package com.example.parkingapp.feature_parking.presentation.parking_lot

import android.os.Bundle
import android.view.Gravity.CENTER
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.parkingapp.databinding.FragmentParkingLotBinding
import com.example.parkingapp.feature_parking.domain.model.ParkingLot
import dagger.hilt.android.AndroidEntryPoint
import com.example.parkingapp.R
import com.example.parkingapp.databinding.ItemParkingSpaceBinding
import com.example.parkingapp.feature_parking.domain.model.Floor

@AndroidEntryPoint
class ParkingLotFragment :Fragment(){

    private lateinit var binding: FragmentParkingLotBinding
    private lateinit var parkingLot: ParkingLot

    companion object{
        const val PARKING_LOT = "parkingLot"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentParkingLotBinding.inflate(layoutInflater, container, false)
        parkingLot = arguments?.getSerializable(PARKING_LOT) as ParkingLot
        addParkingLot(inflater)
        setupViews()
        return binding.root
    }

    private fun setupViews() {
        binding.btnContinue.setOnClickListener {
            findNavController().navigate(R.id.action_parkingLotFragment_to_welcomeFragment)
        }
    }

    private fun addParkingLot(inflater: LayoutInflater) {
        val layout = binding.floorLayout
        parkingLot.floors.forEach{
            val floorLayout = createFloor(it, inflater)
            layout.addView(floorLayout)
        }
    }

    private fun createFloor(floor: Floor, inflater: LayoutInflater): LinearLayout {
        val floorLayout = LinearLayout(requireContext()).apply {
            id = floor.name.toInt()
            orientation = LinearLayout.VERTICAL
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        }

        val textView = TextView(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            gravity = CENTER
            setPadding(10)
            setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.purple_200))
            setTextColor(resources.getColor(R.color.black))
            textSize = 16F
            text = floor.name.toString()
        }

        val spaceLayout = createSpacesForFloor(floor, inflater)
        floorLayout.addView(textView)
        floorLayout.addView(spaceLayout)
        return floorLayout
    }

    private fun createSpacesForFloor(floor: Floor, inflater: LayoutInflater): GridLayout {
        val spaceLayout = GridLayout(requireContext()).apply {
            id = floor.name.toInt()
            columnCount=6
        }

        floor.parkingSpaces.forEach{
            val parkingSpaceView = ItemParkingSpaceBinding.inflate(inflater, spaceLayout, false).apply {
                tvParkingSpaceName.text = it.name
                tvParkingSpaceType.text = it.type.name
                tvVehicleName.text = it.vehicleNum
                tvTicketNum.visibility = View.GONE
                it.parkingTicketNum?.let {
                    tvTicketNum.text = it.toString()
                    tvTicketNum.visibility = View.VISIBLE
                }
                if(!it.free) parkingSpaceLayout.setBackgroundResource(R.drawable.rectangle_shape_grey)
            }
            spaceLayout.addView(parkingSpaceView.root)
        }
        return spaceLayout
    }

}