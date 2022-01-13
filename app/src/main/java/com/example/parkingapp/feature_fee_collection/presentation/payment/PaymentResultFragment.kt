package com.example.parkingapp.feature_fee_collection.presentation.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.parkingapp.R
import com.example.parkingapp.databinding.FragmentPaymentResultBinding

class PaymentResultFragment: Fragment() {

    private lateinit var binding: FragmentPaymentResultBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaymentResultBinding.inflate(layoutInflater, container, false)
        setupViews()
        return binding.root
    }

    private fun setupViews() {
        val args: PaymentResultFragmentArgs by navArgs()
        binding.amountValue.text = getString(R.string.rupees_value, args.amountPaid.toString())
        binding.btnGoToHome.setOnClickListener {
            val action = PaymentResultFragmentDirections.actionPaymentResultFragmentToWelcomeFragment()
            findNavController().navigate(action)
        }
    }

}