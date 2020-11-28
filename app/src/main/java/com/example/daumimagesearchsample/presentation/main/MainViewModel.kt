package com.example.daumimagesearchsample.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.daumimagesearchsample.domain.usecase.SearchImageUseCase
import com.example.daumimagesearchsample.presentation.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class MainViewModel(
    private val searchImageUseCase: SearchImageUseCase
) : BaseViewModel() {

    private val changeEditTextSubject = PublishSubject.create<String>()

    private val _items = MutableLiveData<List<Map<String, String>>>()
    val items: LiveData<List<Map<String, String>>> get() = _items

    private val _toastMsg = MutableLiveData<String>()
    val toastMsg: LiveData<String> get() = _toastMsg

    private val _searchCompleteYN = MutableLiveData<Boolean>()
    val searchCompleteYN: LiveData<Boolean> get() = _searchCompleteYN

    var searchWord = MutableLiveData<String>()

    init {
        _items.value = mutableListOf()
        _searchCompleteYN.value = false

        addDisposable(
            changeEditTextSubject
                .debounce(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { searchImage() },
                    {}
                )
        )
    }

    private fun showToast(msg: String) {
        _toastMsg.value = msg
    }

    private fun clearItems() {
        _items.value = mutableListOf()
    }

    private fun completeSearch() {
        _searchCompleteYN.value = true
    }

    private fun searchImage() {
        searchWord.value?.let { searchWord ->
            addDisposable(
                searchImageUseCase(searchWord)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            clearItems()
                            if (it.meta.total_count == 0) {
                                showToast("검색결과가 없습니다.")
                                completeSearch()
                                return@subscribe
                            }
                            _items.value = it.documents.map { document ->
                                mapOf(
                                    "image_url" to document.image_url
                                )
                            }
                            completeSearch()
                        },
                        { showToast("오류가 발생했습니다. 오류 메세지 : {$it.message}") }
                    )
            )
        }
    }

    fun changeEditText(text: String) {
        changeEditTextSubject.onNext(text)
    }

}