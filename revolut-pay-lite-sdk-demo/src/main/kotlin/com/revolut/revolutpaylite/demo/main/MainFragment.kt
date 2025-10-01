package com.revolut.revolutpaylite.demo.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.revolut.payments.RevolutPaymentsSDK
import com.revolut.revolutpay.api.revolutPay
import com.revolut.revolutpaylite.demo.R
import com.revolut.revolutpaylite.demo.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var binding: FragmentMainBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentMainBinding.inflate(inflater, container, false)
            .also { binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            with(findNavController()) {
                externalRevolutPayRevolutPayButtonDemo.setOnClickListener {
                    navigate(R.id.action_MainFragment_to_ButtonDemoFragment)
                }
                externalRevolutPayRevolutPayFlowDemo.setOnClickListener {
                    navigate(R.id.action_MainFragment_to_FlowDemoFragment)
                }
                externalRevolutPayRevolutPromoBannerDemo.setOnClickListener {
                    navigate(R.id.action_MainFragment_to_PromoBannerFragment)
                }
                externalRevolutPayRevolutPayFlowConfirmButtonDemo.setOnClickListener {
                    navigate(R.id.action_MainFragment_to_FlowConfirmButtonDemoFragment)
                }
                externalRevolutPayRevolutAppInstalledCheck.setOnClickListener {
                    if (RevolutPaymentsSDK.revolutPay.isRevolutAppInstalled(context)) {
                        Toast.makeText(context, R.string.revolut_application_installed, Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, R.string.revolut_application_not_installed, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
