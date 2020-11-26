package com.example.daumimagesearchsample.presentation.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel

abstract class BaseActivity<B : ViewDataBinding, VM : ViewModel>(
    @LayoutRes private val layoutRes: Int
) : AppCompatActivity() {

    protected lateinit var binding: B

    abstract val vm: VM

    abstract fun initViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutRes)
        binding.lifecycleOwner = this

        initViewModel()
    }
}