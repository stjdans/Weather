package com.example.weathers.component

import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

open class BaseActivity<T: ViewBinding> : AppCompatActivity() {

    private var _vb: T? = null

    protected var binding
        get() = _vb!!
        set(value) {
            _vb = value
        }
    override fun onDestroy() {
        super.onDestroy()
        _vb = null
    }
}