package com.revolut.revolutpaylite.demo.payment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.revolut.payments.RevolutPaymentsSDK
import com.revolut.revolutpay.api.PaymentResult
import com.revolut.revolutpay.api.bindPaymentState
import com.revolut.revolutpay.api.order.OrderParams
import com.revolut.revolutpay.api.revolutPay
import com.revolut.revolutpaylite.demo.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.revolut.revolutpaylite.demo.databinding.FragmentRevolutPayButtonPaymentBinding as Binding

class RevolutPayButtonPaymentFragment : Fragment() {

    private var binding: Binding? = null

    private val paymentController = RevolutPaymentsSDK.revolutPay.createController(this) { paymentResult ->
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
        binding?.apply {
            externalRevolutPayClearOrderToken.setOnClickListener {
                externalRevolutPayOrderTokenEditText.setText("")
            }

            // Binds button state to payment lifecycle and shows loading indicator when processing
            externalRevolutPayRevolutPayButtonExtraSmallWithoutBox.bindPaymentState(paymentController, this@RevolutPayButtonPaymentFragment)

            // Starts the payment flow on button click
            externalRevolutPayRevolutPayButtonExtraSmallWithoutBox.setOnClickListener {
                fetchParamsAndPay()
            }
        }
    }

    private fun fetchParamsAndPay() {
        binding?.externalRevolutPayRevolutPayButtonExtraSmallWithoutBox?.let { payButton ->
            // Shows blocking loading indicator while fetching order params
            payButton.showBlockingLoading(true)
            viewLifecycleOwner.lifecycleScope.launch {
                // Fetches order params and starts the payment process
                runCatching { fetchOrderParams() }
                    // Starts the payment if params were fetched successfully
                    .onSuccess { orderParams -> paymentController.pay(orderParams) }
                    // Hides loading and shows error if fetching params failed
                    .onFailure { error ->
                        showToast(R.string.order_failed)
                        payButton.showBlockingLoading(false)
                    }
            }
        }
    }

    private suspend fun fetchOrderParams(): OrderParams {
        delay(500)
        return OrderParams(
            orderToken = requireNotNull(binding).externalRevolutPayOrderTokenEditText.text.toString(),
            returnUri = "payments://revolut-pay-demo".toUri(),
            requestShipping = false,
            customer = null,
            savePaymentMethodForMerchant = requireNotNull(binding).externalRevolutPaySavePaymentMethodForMerchant.isChecked,
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun showToast(resId: Int) = Toast.makeText(context, resId, Toast.LENGTH_LONG).show()
}
