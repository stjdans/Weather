package com.example.weathers.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("src_id")
fun setImage(view: ImageView, id: Int) {
    view.setImageResource(id)
}