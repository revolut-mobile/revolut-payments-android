package com.revolut.revolutpaylite.demo.payment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.revolut.payments.RevolutPaymentsSDK
import com.revolut.revolutpay.api.PaymentResult
import com.revolut.revolutpay.api.PaymentState
import com.revolut.revolutpay.api.RevolutPayButton
import com.revolut.revolutpay.api.RevolutPaymentController
import com.revolut.revolutpay.api.order.OrderParams
import com.revolut.revolutpay.api.revolutPay
import com.revolut.revolutpaylite.demo.R
import com.revolut.revolutpaylite.demo.databinding.FragmentRevolutPayButtonPaymentBinding as Binding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class RevolutPayButtonPaymentFragment : Fragment() {

    private var binding: Binding? = null

    private lateinit var paymentController: RevolutPaymentController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        paymentController = RevolutPaymentsSDK.revolutPay.createController(this) { paymentResult ->
            when (paymentResult) {
                is PaymentResult.Success -> showToast(R.string.order_completed)
                is PaymentResult.UserAbandonedPayment -> showToast(R.string.order_abandoned)
                is PaymentResult.Failure -> showToast(R.string.order_failed).also {
                    Log.e("REVOLUT_PAY_SDK", paymentResult.exception.toString())
                }
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

            paymentController.bindButton(
                button = externalRevolutPayRevolutPayButtonExtraSmallWithoutBox,
                getOrderParams = ::fetchOrderParams,
                lifecycleOwner = this@RevolutPayButtonPaymentFragment,
                onGetOrderParamsError = { Toast.makeText(context, R.string.order_failed, Toast.LENGTH_LONG).show() }
            )
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

    private fun RevolutPaymentController.bindButton(
        button: RevolutPayButton,
        getOrderParams: suspend () -> OrderParams,
        lifecycleOwner: LifecycleOwner,
        onGetOrderParamsError: (Throwable) -> Unit,
    ) {
        paymentState.onEach {
            button.showBlockingLoading(it is PaymentState.Processing)
        }.launchIn(lifecycleScope)

        button.setOnClickListener {
            lifecycleOwner.lifecycleScope.launch {
                button.showBlockingLoading(true)
                runCatching { getOrderParams() }
                    .onFailure { error ->
                        onGetOrderParamsError(error)
                        button.showBlockingLoading(false)
                    }
                    .onSuccess { orderParams -> this@bindButton.pay(orderParams) }
            }
        }
    }

    private fun RevolutPayButton.showBlockingLoading(loading: Boolean) {
        isClickable = !loading
        showLoading(loading)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun showToast(resId: Int) = Toast.makeText(context, resId, Toast.LENGTH_LONG).show()
}
