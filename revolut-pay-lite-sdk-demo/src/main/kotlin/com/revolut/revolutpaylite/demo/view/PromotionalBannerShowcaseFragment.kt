package com.revolut.revolutpaylite.demo.view

import android.os.Bundle
import android.util.TypedValue
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.revolut.payments.RevolutPaymentsSDK
import com.revolut.payments.RevolutCurrency
import com.revolut.revolutpay.api.CountryCode
import com.revolut.revolutpay.api.order.Customer
import com.revolut.revolutpay.api.promobanner.PromoBannerParams
import com.revolut.revolutpay.api.revolutPay
import com.revolut.revolutpaylite.demo.R
import com.revolut.revolutpaylite.demo.databinding.FragmentPromotionalBannerShowcaseBinding as Binding
import java.util.UUID

class PromotionalBannerShowcaseFragment : Fragment() {

    private var binding: Binding? = null
    private var promoBanner: View? = null
    private val currencies: Array<RevolutCurrency> =
        arrayOf(RevolutCurrency.GBP, RevolutCurrency.EUR, RevolutCurrency.PLN, RevolutCurrency.RON)
    private var currentPhone = ""
    private var currentEmail = ""
    private var currentCurrency: RevolutCurrency = currencies.first()
    private val currentAmount = 10000L

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
        setupViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun setupViews() = with(binding!!) {
        externalRevolutPayPhoneSetSwitch.setOnCheckedChangeListener { _, checked ->
            externalRevolutPayPhoneEditText.isEnabled = checked
            updatePromoBanner(
                phone = getEnteredPhoneNumber(),
                email = getEnteredEmail()
            )
        }

        externalRevolutPayPhoneEditText.addTextChangedListener { editable ->
            currentPhone = editable.toString()
        }

        externalRevolutPayEmailSetSwitch.setOnCheckedChangeListener { _, checked ->
            externalRevolutPayEmailEditText.isEnabled = checked
            updatePromoBanner(
                phone = getEnteredPhoneNumber(),
                email = getEnteredEmail()
            )
        }

        externalRevolutPayEmailEditText.addTextChangedListener { editable ->
            currentEmail = editable.toString()
        }

        externalRevolutPayPhoneEditText.setOnKeyListener { view, code, event ->
            if (event.action == KeyEvent.ACTION_DOWN && code == KeyEvent.KEYCODE_ENTER) {
                updatePromoBanner(
                    phone = currentPhone,
                    email = currentEmail
                )
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        val currencyAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            currencies
        )
        externalRevolutPayCurrencySpinner.adapter = currencyAdapter
        externalRevolutPayCurrencySpinner.setSelection(currencyAdapter.getPosition(currentCurrency))
        externalRevolutPayCurrencySpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                if (currentCurrency != currencies[position]) {
                    currentCurrency = currencies[position]
                    updatePromoBanner(
                        phone = getEnteredPhoneNumber(),
                        email = getEnteredEmail()
                    )
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) = Unit
        }

        updatePromoBanner(
            phone = getEnteredPhoneNumber(),
            email = getEnteredEmail()
        )
    }

    private fun Binding.getEnteredPhoneNumber() =
        if (externalRevolutPayPhoneEditText.isEnabled) {
            externalRevolutPayPhoneEditText.text.toString()
        } else {
            ""
        }

    private fun Binding.getEnteredEmail() =
        if (externalRevolutPayEmailEditText.isEnabled) {
            externalRevolutPayEmailEditText.text.toString()
        } else {
            ""
        }

    private fun updatePromoBanner(
        phone: String,
        email: String,
    ) = with(binding!!) {
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.topMargin = 16f.dpToPixels()
        promoBanner?.let { externalRevolutPayLinearLayout.removeView(it) }
        promoBanner = createBanner(phone, email, currentCurrency)
        externalRevolutPayLinearLayout.addView(promoBanner, layoutParams)
    }

    private fun createBanner(phone: String, email: String, currency: RevolutCurrency): View =
        RevolutPaymentsSDK.revolutPay.providePromotionalBannerWidget(
            context = requireContext(),
            params = PromoBannerParams(
                transactionId = UUID.randomUUID().toString(),
                currency = currency,
                paymentAmount = currentAmount,
                customer = Customer(
                    name = null,
                    phone = phone,
                    email = email,
                    dateOfBirth = null,
                    country = CountryCode.RO,
                ),
            ),
            themeId = R.style.Theme_RevolutPayBanner
        )

    private fun Float.dpToPixels() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        resources.displayMetrics
    ).toInt()

}
