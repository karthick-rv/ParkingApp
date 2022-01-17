package com.example.parkingapp.feature_transactions.presentation.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkingapp.feature_parking.common.Resource
import com.example.parkingapp.feature_transactions.domain.model.TransactionData
import com.example.parkingapp.feature_transactions.domain.use_case.GetTransactionData
import com.example.parkingapp.feature_transactions.domain.use_case.TransactionUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(private val transactionUseCases: TransactionUseCases): ViewModel() {

    private val _transactionDataFlow: MutableSharedFlow<Resource<TransactionData>> = MutableSharedFlow()
    val transactionDataFlow: SharedFlow<Resource<TransactionData>> = _transactionDataFlow


    fun onEvent(transactionEvent: TransactionEvent){
        when(transactionEvent){
            is TransactionEvent.GetTransactionForVehicleType -> {
                viewModelScope.launch {
                    _transactionDataFlow.emit(Resource.Success(transactionUseCases.getTransactionData(transactionEvent.vehicleType)))
                }
            }
            TransactionEvent.GetAllTransaction -> {
                viewModelScope.launch {
                    _transactionDataFlow.emit(Resource.Success(transactionUseCases.getTransactionData(TransactionsFragment.ALL_VEHICLE_TYPE)))
                }
            }
        }
    }
}