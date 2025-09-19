package com.revolut.revolutpaylite.demo

import android.app.Application
import com.revolut.payments.RevolutPaymentsSDK

class RevolutPayLiteDemoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        RevolutPaymentsSDK.configure(
            RevolutPaymentsSDK.Configuration(
                environment = RevolutPaymentsSDK.Environment.SANDBOX,
                merchantPublicKey = TODO("Enter your merchant public key"),
            )
        )
    }
}
