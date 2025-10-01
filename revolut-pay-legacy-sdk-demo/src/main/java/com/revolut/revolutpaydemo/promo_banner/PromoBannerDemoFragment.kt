package com.revolut.revolutpaydemo.promo_banner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.revolut.revolutpay.api.Currency
import com.revolut.revolutpay.api.params.Customer
import com.revolut.revolutpay.api.params.PromoBannerParams
import com.revolut.revolutpay.api.revolutPay
import com.revolut.revolutpay.domain.model.CountryCode
import com.revolut.revolutpaydemo.R
import com.revolut.revolutpayments.RevolutPayments
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
                    currency = Currency.EUR,
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
