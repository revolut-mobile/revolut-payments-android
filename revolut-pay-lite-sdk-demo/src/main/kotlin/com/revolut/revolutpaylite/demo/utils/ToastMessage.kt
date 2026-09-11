package com.revolut.revolutpaylite.demo.utils

import android.widget.Toast
import androidx.fragment.app.Fragment

internal fun Fragment.showToast(resId: Int) = Toast.makeText(requireContext(), resId, Toast.LENGTH_LONG).show()

internal fun Fragment.showToast(text: String) = Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
