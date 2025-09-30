package com.revolut.revolutpaydemo.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.revolut.revolutpay.api.OrderResultCallback
import com.revolut.revolutpay.api.revolutPay
import com.revolut.revolutpaydemo.R
import com.revolut.revolutpaydemo.databinding.FragmentCheckoutCustomBinding
import com.revolut.revolutpaydemo.utils.Defaults
import com.revolut.revolutpayments.RevolutPayments

class DemoCustomCheckoutFragment : Fragment() {
    private var binding: FragmentCheckoutCustomBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = FragmentCheckoutCustomBinding.inflate(inflater, container, false)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val orderToken = Defaults.orderToken?.takeIf { it.isNotBlank() } ?: return
        binding?.revolutPayButton?.setOnClickListener {
            showLoading(true)
            RevolutPayments.revolutPay.pay(
                requireContext(),
                orderToken = orderToken,
                savePaymentMethodForMerchant = binding?.revolutPaySavePaymentMethodForMerchant?.isChecked == true,
                lifecycle = lifecycle,
                callback = object : OrderResultCallback {
                    override fun onOrderCompleted() {
                        showLoading(false)
                        Toast.makeText(
                            context,
                            R.string.checkout_completed,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onOrderFailed(throwable: Throwable) {
                        showLoading(false)
                        Toast.makeText(
                            context,
                            R.string.checkout_failed,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onUserAbandonedPayment() {
                        showLoading(false)
                        Toast.makeText(
                            context,
                            R.string.checkout_abandoned,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun showLoading(loading: Boolean) {
        binding?.revolutPayProgressBar?.isVisible = loading
    }
}