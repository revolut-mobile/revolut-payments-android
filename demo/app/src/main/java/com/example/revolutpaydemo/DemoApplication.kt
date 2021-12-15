package com.example.revolutpaydemo

import android.app.Application
import com.revolut.revolutpay.api.RevolutPay

class DemoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        RevolutPay.init(
            RevolutPay.RevolutPayEnvironment.SANDBOX,
            "demoapp://backlink.com"
        )
    }
}