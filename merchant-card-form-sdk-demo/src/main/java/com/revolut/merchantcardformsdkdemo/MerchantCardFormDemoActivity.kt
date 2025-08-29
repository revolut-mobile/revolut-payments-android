package com.revolut.merchantcardformsdkdemo

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.revolut.cardpayments.api.RevolutPayCardPaymentLauncher
import com.revolut.cardpayments.api.RevolutPayCardPaymentParams
import com.revolut.cardpayments.api.RevolutPayCardPaymentResult

class MerchantCardFormDemoActivity : AppCompatActivity() {

    private val payButton: Button by lazy { findViewById(R.id.buttonPay) }
    private val payWithEmailButton: Button by lazy { findViewById(R.id.buttonPayWithEmail) }
    private val orderIdEditText: TextInputEditText by lazy { findViewById(R.id.editTextOrderId) }

    // Declare RevolutPayCardPaymentLauncher and add result handling
    private val cardPaymentLauncher = RevolutPayCardPaymentLauncher(this) { result ->
        when (result) {
            is RevolutPayCardPaymentResult.Authorised,
            is RevolutPayCardPaymentResult.Declined,
            is RevolutPayCardPaymentResult.Error.ApiError,
            is RevolutPayCardPaymentResult.Error.GenericError,
            is RevolutPayCardPaymentResult.Error.NetworkError,
            is RevolutPayCardPaymentResult.Error.OrderNotAvailable,
            is RevolutPayCardPaymentResult.Error.OrderNotFound,
            is RevolutPayCardPaymentResult.Error.TimeoutError,
            is RevolutPayCardPaymentResult.Failed -> {
                Toast.makeText(this, result.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_merchant_card_form_demo)
        applySystemBarInsets()

        payButton.setOnClickListener {
            startPayment(null)
        }
        payWithEmailButton.setOnClickListener {
            startPayment("revolutpay@revolut.com")
        }
    }

    // Start payment using the launcher
    private fun startPayment(email: String?) {
        cardPaymentLauncher.launch(
            RevolutPayCardPaymentParams(
                orderId = orderIdEditText.text.toString(),
                email = email,
            )
        )
    }

    private fun applySystemBarInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(insets.left, insets.top, insets.right, insets.bottom)
            WindowInsetsCompat.CONSUMED
        }
    }
}
