package com.example.parkingapp.feature_transactions.presentation.transactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.parkingapp.R
import com.example.parkingapp.databinding.FragmentTransactionsBinding
import com.example.parkingapp.feature_parking.common.Resource
import com.example.parkingapp.feature_parking.domain.util.VehicleType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TransactionsFragment : Fragment() {

    private lateinit var binding: FragmentTransactionsBinding
    private val viewModel by activityViewModels<TransactionViewModel>()
    private val transactionAdapter by lazy {
        TransactionAdapter()
    }

    companion object{
        const val ALL_VEHICLE_TYPE = "ALL"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransactionsBinding.inflate(inflater, container, false)
        setupViews()
        listenForTransactionData()
        return binding.root
    }

    private fun setupViews() {
        val vehicleTypes = VehicleType.values().map { vehicleType -> vehicleType.name }.toMutableList()
        vehicleTypes.add(vehicleTypes.size, ALL_VEHICLE_TYPE)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item, vehicleTypes)
        binding.vehicleType.setAdapter(arrayAdapter)

        binding.vehicleType.setOnItemClickListener { adapterView, _, i, _ ->
           val vehicleType = adapterView.getItemAtPosition(i) as String
            viewModel.onEvent(TransactionEvent.GetTransactionForVehicleType(vehicleType))
        }
        binding.transactionsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            hasFixedSize()
        }
    }

    private fun listenForTransactionData() {
        lifecycleScope.launch {
            viewModel.transactionDataFlow.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    when (it) {
                        is Resource.Success -> {
                            binding.totalAmount.text = getString(R.string.rupees_value, it.data?.totalAmount.toString())
                            it.data?.transactions?.let { it1 ->
                                transactionAdapter.setParkingTickets(it1)
                                binding.transactionsRecyclerView.adapter = transactionAdapter
                            }
                        }
                        is Resource.Error -> TODO()
                        is Resource.Loading -> TODO()
                    }

                }
        }
    }

}