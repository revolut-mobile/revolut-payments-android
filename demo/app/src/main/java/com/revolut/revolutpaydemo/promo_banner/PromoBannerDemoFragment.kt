package com.revolut.revolutpaydemo.promo_banner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.revolut.payments.revolutpay.params.Customer
import com.revolut.revolutpaydemo.R
import com.revolut.payments.RevolutPayments
import com.revolut.payments.currency.RevolutCurrency
import com.revolut.payments.revolutpay.params.CountryCode
import com.revolut.payments.revolutpay.params.PromoBannerParams
import com.revolut.payments.revolutpay.revolutPay
import com.revolut.revolutpaydemo.databinding.FragmentRevolutPromoBannerBinding as Binding

class PromoBannerDemoFragment : Fragment() {

    private var binding: Binding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = Binding.inflate(inflater, container, false)
        .also { binding = it }
        .root
        .apply { addPromoBannerWidget() }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun FrameLayout.addPromoBannerWidget() {
        addView(
            RevolutPayments.revolutPay.providePromotionalBannerWidget(
                context = requireContext(),
                params = PromoBannerParams(
                    transactionId = "c93803f9-7299-4d8a-ba59-e0e590a3b0eb",
                    currency = RevolutCurrency.EUR,
                    paymentAmount = null,
                    customer = Customer(
                        name = null,
                        phone = null,
                        email = null,
                        dateOfBirth = null,
                        country = CountryCode.GB,
                    ),
                ),
                themeId = R.style.Theme_RevolutPayBanner,
            ),
            FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        )
    }
}
