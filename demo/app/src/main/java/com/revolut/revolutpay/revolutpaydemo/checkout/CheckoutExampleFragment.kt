package com.revolut.revolutpay.revolutpaydemo.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.revolut.revolutpay.api.OrderResultCallback
import com.revolut.revolutpay.api.createController
import com.revolut.revolutpay.revolutpaydemo.R
import com.revolut.revolutpay.revolutpaydemo.utils.Defaults
import com.revolut.revolutpay.revolutpaydemo.databinding.FragmentCheckoutExampleBinding as Binding

class CheckoutExampleFragment : Fragment() {

    private var binding: Binding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = Binding.inflate(inflater, container, false)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val orderToken = Defaults.orderToken?.takeIf { it.isNotBlank() } ?: return
        binding?.apply {
            revolutPayButton
                .createController()
                .apply {
                    setHandler { flow ->
                        flow.setOrderToken(orderToken = orderToken)
                        flow.attachLifecycle(this@CheckoutExampleFragment.lifecycle)
                        flow.continueConfirmationFlow()
                    }
                }
                .apply {
                    setOrderResultCallback(object : OrderResultCallback {
                        override fun onOrderCompleted() {
                            Toast.makeText(
                                this@CheckoutExampleFragment.context,
                                R.string.checkout_completed,
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        override fun onOrderFailed(throwable: Throwable) {
                            throwable.printStackTrace()
                            Toast.makeText(
                                this@CheckoutExampleFragment.context,
                                R.string.checkout_failed,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}