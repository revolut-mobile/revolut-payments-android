package com.revolut.revolutpaydemo.configuresdk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.revolut.revolutpay.api.RevolutPayEnvironment
import com.revolut.revolutpay.api.revolutPay
import com.revolut.revolutpaydemo.R
import com.revolut.revolutpaydemo.utils.Defaults
import com.revolut.revolutpayments.RevolutPayments
import com.revolut.revolutpaydemo.databinding.FragmentConfigureSdkBinding as Binding

class ConfigureSdkFragment : Fragment() {

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

        binding?.apply {
            prefillWithStoredConfig()
            revolutPayConfigureSdkUpdateButton.setOnClickListener {
                updateRevolutPayConfig()
                revolutPayConfigureSdkUpdateButton.isEnabled = false
            }
            revolutPayEnvironmentProductionButton.setOnClickListener { onInputAction() }
            revolutPayEnvironmentSandboxButton.setOnClickListener { onInputAction() }
            revolutPayOrderTokenEditText.addTextChangedListener { onInputAction() }
            revolutPayMerchantPublicKeyEditText.addTextChangedListener { onInputAction() }
        }
    }

    private fun Binding.prefillWithStoredConfig() {
        when (Defaults.environment) {
            RevolutPayEnvironment.MAIN -> revolutPayEnvironmentGroup.check(R.id.revolutPayEnvironmentProductionButton)
            RevolutPayEnvironment.SANDBOX -> revolutPayEnvironmentGroup.check(R.id.revolutPayEnvironmentSandboxButton)
        }
        Defaults.merchantPublicKey?.let(revolutPayMerchantPublicKeyEditText::setText)
        Defaults.orderToken?.let(revolutPayOrderTokenEditText::setText)
    }

    private fun Binding.updateRevolutPayConfig() {
        val merchantPublicKey = revolutPayMerchantPublicKeyEditText.text.toString()
        Defaults.merchantPublicKey = merchantPublicKey
        Defaults.environment = getEnvironment()
        Defaults.orderToken = revolutPayOrderTokenEditText.text.toString()
        RevolutPayments.revolutPay.init(
            environment = Defaults.environment,
            returnUri = Defaults.returnUri,
            merchantPublicKey = merchantPublicKey,
            requestShipping = false,
            customer = null
        )
    }

    private fun Binding.getEnvironment() =
        when (revolutPayEnvironmentGroup.checkedButtonId) {
            R.id.revolutPayEnvironmentProductionButton -> RevolutPayEnvironment.MAIN
            R.id.revolutPayEnvironmentSandboxButton -> RevolutPayEnvironment.SANDBOX
            else -> throw IllegalArgumentException("Can't find the environment")
        }

    private fun Binding.onInputAction() {
        val merchantPublicKey = revolutPayMerchantPublicKeyEditText.text.toString()
        val orderToken = revolutPayOrderTokenEditText.text.toString()
        when {
            merchantPublicKey.isBlank() -> {
                revolutPayConfigureSdkUpdateButton.isEnabled = false
            }
            merchantPublicKey.isNotBlank() && Defaults.merchantPublicKey != merchantPublicKey -> {
                revolutPayConfigureSdkUpdateButton.isEnabled = true
            }
            orderToken.isNotBlank() && Defaults.orderToken != orderToken -> {
                revolutPayConfigureSdkUpdateButton.isEnabled = true
            }
            getEnvironment() != Defaults.environment -> {
                revolutPayConfigureSdkUpdateButton.isEnabled = true
            }
            else -> revolutPayConfigureSdkUpdateButton.isEnabled = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}