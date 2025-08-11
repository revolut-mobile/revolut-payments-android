package com.revolut.revolutpaydemo.utils

import android.net.Uri
import com.revolut.revolutpay.api.RevolutPayEnvironment

object Defaults {

    val returnUri: Uri = Uri.parse("demo://revolut-pay")

    var environment: RevolutPayEnvironment = RevolutPayEnvironment.MAIN
    var merchantPublicKey: String? = null
    var orderToken: String? = null
}