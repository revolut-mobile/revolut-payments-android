package com.example.revolutpaydemo

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import com.revolut.revolutpay.api.OrderState
import com.revolut.revolutpay.api.RevolutPay
import com.revolut.revolutpay.ui.button.RevolutPayButton
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class DemoActivity : AppCompatActivity() {
    private val tokenInput: AppCompatEditText by lazy { findViewById(R.id.tokenInput) }
    private val lightModeButton: Button by lazy { findViewById(R.id.lightModeButton) }
    private val darkModeButton: Button by lazy { findViewById(R.id.darkModeButton) }
    private val revolutPayButton: RevolutPayButton by lazy { findViewById(R.id.revolutPayBtn) }

    private var loadingDisposable: Disposable? = null
    private var isPolling: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)
        tokenInput.addTextChangedListener {
            revolutPayButton.setPublicId(it.toString())
        }
        lightModeButton.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        darkModeButton.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        revolutPayButton.setOnClickListener {
            hideKeyboard(it)
            startPolling()
        }
    }

    private fun hideKeyboard(view: View) {
        (getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager)
            ?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onStart() {
        super.onStart()
        if (isPolling) {
            startPolling()
        }
    }

    override fun onStop() {
        super.onStop()
        loadingDisposable?.dispose()
    }

    private fun startPolling() {
        isPolling = true
        loadingDisposable = Observable.interval(0L, 3L, TimeUnit.SECONDS)
            .flatMap {
                Observable.fromCallable {
                    RevolutPay.fetchOrderState(tokenInput.text.toString())
                }
            }
            .takeUntil { orderState -> orderState == OrderState.COMPLETED || orderState == OrderState.FAILED }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ orderState ->
                when (orderState) {
                    OrderState.CREATED -> {
                        //ignore
                    }
                    OrderState.PENDING -> {
                        revolutPayButton.showLoadingProgress()
                    }
                    OrderState.COMPLETED -> {
                        isPolling = false
                        revolutPayButton.hideLoadingProgress()
                        Toast.makeText(this, "Payment confirmed", Toast.LENGTH_LONG).show()
                    }
                    OrderState.FAILED -> {
                        isPolling = false
                        revolutPayButton.hideLoadingProgress()
                        Toast.makeText(this, "Payment failed", Toast.LENGTH_LONG).show()
                    }
                }
            },
                {
                    isPolling = false
                    Toast.makeText(this, "Error $it", Toast.LENGTH_LONG).show()
                }
            )
    }
}