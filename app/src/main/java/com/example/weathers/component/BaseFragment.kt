package com.example.weathers.component

import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

open class BaseFragment<T : ViewBinding> : Fragment() {

    private var _vb: T? = null

    protected var binding
        get() = _vb!!
        set(value) {
            _vb = value
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _vb = null
    }

}