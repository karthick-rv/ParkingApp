package com.example.parkingapp.feature_parking.presentation.parking_lot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import com.example.parkingapp.R
import com.example.parkingapp.databinding.FragmentParkingLotNewBinding
import com.example.parkingapp.feature_parking.common.Resource
import com.example.parkingapp.feature_parking.domain.model.LoadingInfo
import com.example.parkingapp.feature_parking.domain.model.ParkingSpace
import com.example.parkingapp.feature_parking.domain.use_case.ParkingLotManager
import com.example.parkingapp.feature_parking.domain.util.ParkingSpaceUtil
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ParkingLotFragment : Fragment() {

    private lateinit var binding: FragmentParkingLotNewBinding
    private val parkingLotAdapter by lazy {
        ParkingLotRecyclerViewAdapter()
    }
    private val viewModel by activityViewModels<ParkingLotViewModel>()
    var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentParkingLotNewBinding.inflate(inflater, container, false)
        listenForParkingSpaces()
        setupViews()
        initScrollListener()
        return binding.root
    }

    private fun listenForParkingSpaces() {
        lifecycleScope.launch {
            viewModel.parkedSpacesFlow.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    when (it) {
                        is Resource.Error -> TODO()
                        is Resource.Loading -> TODO()
                        is Resource.Success -> {
                            it.data?.let { it1 -> setRecyclerViewItems(it1) }
                        }
                    }

                }
        }
    }

    private fun setupViews() {
        fetchRecyclerViewItems()
        binding.parkingLotRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = createLayoutManager()
            adapter = parkingLotAdapter
        }
    }

    private fun createLayoutManager(): GridLayoutManager {
        val layoutManager = GridLayoutManager(requireContext(), 5)
        layoutManager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (parkingLotAdapter.getItemViewType(position)) {
                    R.layout.item_parking_space -> 1
                    R.layout.item_floor_name -> 5
                    else -> 0
                }
            }
        }
        return layoutManager
    }

    private fun initScrollListener() {
        binding.parkingLotRecyclerView.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as GridLayoutManager

                if(!isLoading){
                    if(layoutManager.findLastCompletelyVisibleItemPosition() == parkingLotAdapter.items.size - 1){

                        loadMore()
                        isLoading = true
                    }
                }
            }
        })
    }

    private fun fetchRecyclerViewItems() {
        viewModel.onEvent(ParkingLotEvent.ShowParkingLot(LoadingInfo(1, 0)))
    }

    private fun loadMore() {

        val currentFloorIndex = viewModel.currentLoadingInfo.floorIndex
        val currentLoadIndex = viewModel.currentLoadingInfo.loadIndex
        val floorCount = viewModel.getFloorCount()
        val parkingSpaceCount = viewModel.getParkingSpaceCount(1)
        val loadCount = parkingSpaceCount / ParkingLotManager.COUNT_PER_LOAD
        val loadCountRem = parkingSpaceCount % ParkingLotManager.COUNT_PER_LOAD

        if(loadCountRem>0 && currentLoadIndex == loadCount -1){
            viewModel.onEvent(ParkingLotEvent.ShowParkingLot(LoadingInfo(currentFloorIndex, currentLoadIndex + 1, loadCountRem)))
        }else if(currentLoadIndex < loadCount - 1){
            viewModel.onEvent(ParkingLotEvent.ShowParkingLot(LoadingInfo(currentFloorIndex, currentLoadIndex + 1)))
        }
        else if(currentFloorIndex < floorCount){
            viewModel.onEvent(ParkingLotEvent.ShowParkingLot(LoadingInfo(currentFloorIndex + 1, 0)))
        }
    }


    private fun setRecyclerViewItems(parkingSpaces: List<ParkingSpace>) {
        val itemList = mutableListOf<ParkingSpaceRecyclerViewItem>()
        if (viewModel.currentLoadingInfo.loadIndex == 0) {
            itemList.add(
                ParkingSpaceRecyclerViewItem.FloorItem(
                    ParkingSpaceUtil.getCharForNumber(
                        viewModel.currentLoadingInfo.floorIndex
                    ).toString()
                )
            )
        }

        parkingSpaces.forEach {
            itemList.add(ParkingSpaceRecyclerViewItem.ParkingSpaceItem(it))
        }

        val position = parkingLotAdapter.items.size
        parkingLotAdapter.items.addAll(itemList)
        binding.parkingLotRecyclerView.recycledViewPool.clear()
        binding.parkingLotRecyclerView.post {
//            parkingLotAdapter.notifyItemInserted(position)
            parkingLotAdapter.notifyDataSetChanged()
        }
        isLoading = false
    }

}