package com.revolut.revolutpaylite.demo.payment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.revolut.payments.RevolutPaymentsSDK
import com.revolut.revolutpay.api.PaymentResult
import com.revolut.revolutpay.api.RevolutPaymentController
import com.revolut.revolutpay.api.order.OrderParams
import com.revolut.revolutpay.api.revolutPay
import com.revolut.revolutpaylite.demo.R
import com.revolut.revolutpaylite.demo.databinding.FragmentCustomButtonPaymentBinding as Binding

class CustomButtonPaymentFragment : Fragment() {

    private lateinit var binding: Binding

    private val paymentController: RevolutPaymentController = RevolutPaymentsSDK.revolutPay.createController(this) { paymentResult ->
        when (paymentResult) {
            is PaymentResult.Success -> showToast(R.string.order_completed)
            is PaymentResult.UserAbandonedPayment -> showToast(R.string.order_abandoned)
            is PaymentResult.Failure -> showToast(R.string.order_failed).also {
                Log.e("REVOLUT_PAY_SDK", paymentResult.exception.toString())
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return Binding.inflate(inflater, container, false)
            .also { binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            externalRevolutPayClearOrderToken.setOnClickListener {
                externalRevolutPayOrderTokenEditText.setText("")
            }

            confirmButton.setOnClickListener {
                externalRevolutPayOrderTokenEditText.text.toString()
                    .takeIf { it.isNotBlank() }
                    ?.let { orderToken -> pay(orderToken, externalRevolutPaySavePaymentMethodForMerchant.isChecked) }
            }
        }
    }

    private fun pay(orderToken: String, savePaymentMethodForMerchant: Boolean) {
        paymentController.pay(
            orderParams = OrderParams(
                orderToken = orderToken,
                returnUri = "payments://revolut-pay-demo".toUri(),
                requestShipping = false,
                savePaymentMethodForMerchant = savePaymentMethodForMerchant,
                customer = null
            )
        )
    }

    private fun showToast(resId: Int) = Toast.makeText(context, resId, Toast.LENGTH_LONG).show()
}
