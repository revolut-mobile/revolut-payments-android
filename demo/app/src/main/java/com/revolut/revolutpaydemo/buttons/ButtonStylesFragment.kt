package com.revolut.revolutpaydemo.buttons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.revolut.revolutpaydemo.databinding.FragmentButtonStylesBinding as Binding

class ButtonStylesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = Binding.inflate(inflater, container, false).root
}