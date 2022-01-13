package com.example.parkingapp.feature_fee_collection.presentation.payment

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.color
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.parkingapp.R
import com.example.parkingapp.databinding.FragmentPaymentBinding
import com.example.parkingapp.feature_fee_collection.domain.model.PaymentDetail
import com.example.parkingapp.feature_fee_collection.domain.model.PaymentDetailUiEvent
import com.example.parkingapp.feature_fee_collection.presentation.FeeCollectionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PaymentFragment : Fragment() {

    private lateinit var binding: FragmentPaymentBinding
    private val viewModel by viewModels<FeeCollectionViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaymentBinding.inflate(inflater, container, false)
        setupViews()
        return binding.root
    }

    private fun setupViews() {
        binding.btnApply.setOnClickListener {
            val couponCode = binding.couponCodeLayout.editText?.text.toString()
            viewModel.onEvent(PaymentDetailEvent.ApplyCoupon(couponCode))
        }
        binding.btnPayAmount.setOnClickListener {
            viewModel.onEvent(PaymentDetailEvent.PayParkingFees)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.paymentDetailFlow.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    when (it) {
                        is PaymentDetailUiEvent.CalculationSuccess -> calculationSuccess(it.paymentDetail)
                        is PaymentDetailUiEvent.CouponError -> {
                            binding.tvDiscount.text = it.errorMsg
                        }
                        is PaymentDetailUiEvent.CouponSuccess -> {
                            couponSuccess(it.paymentDetail)
                        }
                        is PaymentDetailUiEvent.PaymentResult -> {
                            if(it.isSuccess){
                                it.amountPaid?.let { it1 -> navigateToPaymentResultFragment(it1) }
                            }
                        }
                    }
                }
        }
    }


    private fun calculationSuccess(paymentDetail: PaymentDetail) {
        val fhf = getString(R.string.rupees_value, paymentDetail.firstHourFee.toString())
        val rhf = getString(R.string.rupees_value, paymentDetail.remainingHourFee.toString())
        val total = getString(R.string.rupees_value, paymentDetail.totalFee.toString())
        val toPay = getString(R.string.pay_fees, paymentDetail.totalFee.toInt().toString())
        val duration =  getString(R.string.duration_value, paymentDetail.parkedDuration.toString())
        binding.apply {
            tvDurationParked.text = duration
            fhfValue.text = fhf
            rhfValue.text = rhf
            totalValue.text = total
            btnPayAmount.text = toPay
        }
    }

    private fun couponSuccess(paymentDetail: PaymentDetail){
        val firstHourFee = binding.fhfValue.text
        val remainingHourFee = binding.rhfValue.text
        val total = binding.totalValue.text
        val toPay = getString(R.string.pay_fees, paymentDetail.totalFee.toInt().toString())
        val msg = getString(R.string.coupon_msg)

        val fhf = getString(R.string.rupees_value, paymentDetail.firstHourFee.toString())
        val rhf = getString(R.string.rupees_value, paymentDetail.remainingHourFee.toString())
        val totalString = getString(R.string.rupees_value, paymentDetail.totalFee.toString())


        binding.apply {
            fhfValue.text = setStyle(firstHourFee, fhf)
            rhfValue.text = setStyle(remainingHourFee, rhf)
            totalValue.text = setStyle(total, totalString)
            couponCodeLayout.editText?.setText("")
            couponCodeLayout.editText?.isEnabled = false
            btnApply.isEnabled = false
            tvDiscount.text = msg
            btnPayAmount.text = toPay
        }
    }

    private fun setStyle(strikeText: CharSequence, colorText: String): SpannableStringBuilder {
        val color = ContextCompat.getColor(requireContext(), R.color.purple_700)
        val spannableStringBuilder =  SpannableStringBuilder()
            .append(strikeText)
            .append(" ")
            .color(color) { append(colorText) }
            spannableStringBuilder.setSpan(StrikethroughSpan(), 0, strikeText.length,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannableStringBuilder
    }

    private fun navigateToPaymentResultFragment(amountPaid: Float) {
        val action = PaymentFragmentDirections.actionPaymentFragmentToPaymentResultFragment(amountPaid)
        findNavController().navigate(action)
    }

}