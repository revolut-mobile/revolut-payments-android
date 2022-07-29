package com.revolut.revolutpay.revolutpaydemo.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.revolut.revolutpay.api.revolutPay
import com.revolut.revolutpay.revolutpaydemo.R
import com.revolut.revolutpayments.RevolutPayments
import com.revolut.revolutpay.revolutpaydemo.databinding.FragmentRevolutPayDemoBinding as Binding

class RevolutPayDemoFragment : Fragment() {

    private var binding: Binding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(findNavController()) {
            navigate(R.id.action_RevolutPayDemoFragment_to_ConfigureSdkFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = Binding.inflate(inflater, container, false)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            with(findNavController()) {
                revolutPayConfigureSdkButton.setOnClickListener {
                    navigate(R.id.action_RevolutPayDemoFragment_to_ConfigureSdkFragment)
                }
                revolutPayCheckoutExample.setOnClickListener {
                    navigate(R.id.action_RevolutPayDemoFragment_to_CheckoutExampleFragment)
                }
                revolutPayButtonStyles.setOnClickListener {
                    navigate(R.id.action_RevolutPayDemoFragment_to_ButtonStylesFragment)
                }
                externalRevolutPayRevolutAppInstalledCheck.setOnClickListener {
                    checkRevolutApplicationInstalled()
                }
            }
        }
    }

    private fun NavController.checkRevolutApplicationInstalled() {
        if (RevolutPayments.revolutPay.isRevolutAppInstalled(context)) {
            Toast.makeText(
                context,
                R.string.revolut_application_installed,
                Toast.LENGTH_LONG
            ).show()
        } else {
            Toast.makeText(
                context,
                R.string.revolut_application_not_installed,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}