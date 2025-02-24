package com.revolut.revolutpaydemo.configuresdk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.revolut.payments.RevolutPayments
import com.revolut.payments.model.RevolutPaymentsEnvironment
import com.revolut.revolutpaydemo.R
import com.revolut.revolutpaydemo.utils.Defaults
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
            updateRevolutPayConfig()
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
            RevolutPaymentsEnvironment.PRODUCTION -> revolutPayEnvironmentGroup.check(R.id.revolutPayEnvironmentProductionButton)
            RevolutPaymentsEnvironment.SANDBOX -> revolutPayEnvironmentGroup.check(R.id.revolutPayEnvironmentSandboxButton)
        }
        Defaults.merchantPublicKey?.let(revolutPayMerchantPublicKeyEditText::setText)
        Defaults.orderToken?.let(revolutPayOrderTokenEditText::setText)
    }

    private fun Binding.updateRevolutPayConfig() {
        val merchantPublicKey = revolutPayMerchantPublicKeyEditText.text.toString()
        Defaults.merchantPublicKey = merchantPublicKey
        Defaults.environment = getEnvironment()
        Defaults.orderToken = revolutPayOrderTokenEditText.text.toString()
        RevolutPayments.init(
            environment = Defaults.environment,
            merchantPublicKey = merchantPublicKey,
        )
    }

    private fun Binding.getEnvironment() =
        when (revolutPayEnvironmentGroup.checkedButtonId) {
            R.id.revolutPayEnvironmentProductionButton -> RevolutPaymentsEnvironment.PRODUCTION
            R.id.revolutPayEnvironmentSandboxButton -> RevolutPaymentsEnvironment.SANDBOX
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