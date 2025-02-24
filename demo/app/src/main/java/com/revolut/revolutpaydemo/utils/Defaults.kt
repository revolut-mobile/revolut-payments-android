package com.revolut.revolutpaydemo.utils

import android.net.Uri
import com.revolut.payments.model.RevolutPaymentsEnvironment

object Defaults {

    val returnUri: Uri = Uri.parse("demoapp://backlink.com")

    var environment: RevolutPaymentsEnvironment = RevolutPaymentsEnvironment.PRODUCTION
    var merchantPublicKey: String? = null
    var orderToken: String? = null
}