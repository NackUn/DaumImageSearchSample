package com.example.daumimagesearchsample.presentation.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    fun addDisposable(disposable: Disposable) =
        compositeDisposable.add(disposable)

    fun removeDisposable(disposable: CompositeDisposable) =
        compositeDisposable.remove(disposable)

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

}