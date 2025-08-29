package com.revolut.merchantcardformsdkdemo

import android.app.Application
import com.revolut.payments.RevolutPaymentsSDK

class MerchantCardFormDemoApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        //Configure SDK
        configureSDK()
    }

    private fun configureSDK() {
        RevolutPaymentsSDK.configure(
            RevolutPaymentsSDK.Configuration(
                environment = RevolutPaymentsSDK.Environment.SANDBOX,
                merchantPublicKey =  TODO("Enter your merchant public key"),
            )
        )
    }
}
