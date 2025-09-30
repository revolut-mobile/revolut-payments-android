package com.revolut.merchantcardformsdkdemo

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.revolut.cardpayments.api.CardPaymentLauncher
import com.revolut.cardpayments.api.CardPaymentParams
import com.revolut.cardpayments.api.CardPaymentResult

class MerchantCardFormDemoActivity : AppCompatActivity() {

    private val payButton: Button by lazy { findViewById(R.id.buttonPay) }
    private val payWithEmailButton: Button by lazy { findViewById(R.id.buttonPayWithEmail) }
    private val orderIdEditText: TextInputEditText by lazy { findViewById(R.id.editTextOrderId) }

    // Declare RevolutPayCardPaymentLauncher and add result handling
    private val cardPaymentLauncher = CardPaymentLauncher(this) { result ->
        when (result) {
            is CardPaymentResult.Authorised,
            is CardPaymentResult.Declined,
            is CardPaymentResult.Error.ApiError,
            is CardPaymentResult.Error.GenericError,
            is CardPaymentResult.Error.NetworkError,
            is CardPaymentResult.Error.OrderNotAvailable,
            is CardPaymentResult.Error.OrderNotFound,
            is CardPaymentResult.Error.TimeoutError,
            is CardPaymentResult.UserAbandonedPayment,
            is CardPaymentResult.Failed -> {
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
            CardPaymentParams(
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
