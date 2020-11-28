package com.example.daumimagesearchsample.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.daumimagesearchsample.R

@BindingAdapter("setUrlImg")
fun ImageView.setUrlImg(url: String) {
    Glide.with(this)
        .load(url)
        .placeholder(R.drawable.glide_loading)
        .error(R.drawable.glide_error)
        .into(this)
}