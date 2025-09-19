package com.revolut.revolutpaylite.demo.view

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.WRAP_CONTENT
import androidx.fragment.app.Fragment
import com.revolut.payments.RevolutPaymentsSDK
import com.revolut.revolutpay.api.RevolutPayButton
import com.revolut.revolutpay.api.button.BoxText
import com.revolut.revolutpay.api.button.BoxTextCurrency
import com.revolut.revolutpay.api.button.ButtonParams
import com.revolut.revolutpay.api.button.Radius
import com.revolut.revolutpay.api.button.Size
import com.revolut.revolutpay.api.button.VariantModes
import com.revolut.revolutpay.api.revolutPay
import com.revolut.revolutpaylite.demo.databinding.FragmentRevolutPayButtonShowcaseBinding as Binding

class RevolutPayButtonShowcaseFragment : Fragment() {

    private var binding: Binding? = null

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
            externalRevolutPayDefaultModeButton.setOnClickListener {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
            }
            externalRevolutPayLightModeButton.setOnClickListener {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            externalRevolutPayDarkModeButton.setOnClickListener {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            val buttonEUR = createRevolutPayButton(BoxTextCurrency.EUR)
            val buttonGBP = createRevolutPayButton(BoxTextCurrency.GBP)
            val buttonUSD = createRevolutPayButton(BoxTextCurrency.USD)

            externalRevolutPayButtonsContainer.addView(buttonEUR)
            externalRevolutPayButtonsContainer.addView(buttonGBP)
            externalRevolutPayButtonsContainer.addView(buttonUSD)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun Float.dpToPixels() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        resources.displayMetrics
    ).toInt()

    private fun createRevolutPayButton(
        boxTextCurrency: BoxTextCurrency,
    ): RevolutPayButton =
        RevolutPaymentsSDK.revolutPay.provideButton(
            requireContext(),
            ButtonParams(
                radius = Radius.MEDIUM,
                buttonSize = Size.MEDIUM,
                variantModes = VariantModes(),
                boxText = BoxText.GET_CASHBACK_VALUE,
                boxTextCurrency = boxTextCurrency,
            )
        ).apply {
            layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                topMargin = 16f.dpToPixels()
            }

        }
}
