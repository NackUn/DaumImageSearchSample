package com.example.daumimagesearchsample.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.daumimagesearchsample.R
import com.example.daumimagesearchsample.domain.usecase.SearchImageUseCase
import com.example.daumimagesearchsample.presentation.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class MainViewModel(
    private val searchImageUseCase: SearchImageUseCase
) : BaseViewModel() {

    companion object {
        private const val FIRST_ITEM = 1
        private const val TRUE = 1
        private const val FALSE = 0

        private const val IS_END = "is_end"
        private const val PAGEABLE_COUNT = "pageable_count"
        private const val TOTAL_COUNT = "total_count"
        private const val IMAGE_URL = "image_url"
    }

    private val changeEditTextSubject = PublishSubject.create<String>()

    private val _items = MutableLiveData<List<Map<String, String>>>()
    val items: LiveData<List<Map<String, String>>> get() = _items

    private val _toastResId = MutableLiveData<Int>()
    val toastResId: LiveData<Int> get() = _toastResId

    private val _searchCompleteYN = MutableLiveData<Boolean>()
    val searchCompleteYN: LiveData<Boolean> get() = _searchCompleteYN

    private val _resultInfo = MutableLiveData<Map<String, Int>>()

    private val _loadingYN = MutableLiveData<Boolean>()
    val loadingYN: LiveData<Boolean> get() = _loadingYN

    var searchWord = MutableLiveData<String>()
    var currentPage = MutableLiveData<Int>()

    init {
        _items.value = mutableListOf()
        _resultInfo.value = mutableMapOf()
        _searchCompleteYN.value = false
        _loadingYN.value = false
        currentPage.value = FIRST_ITEM

        addDisposable(
            changeEditTextSubject
                .debounce(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { searchImage(FIRST_ITEM) },
                    {}
                )
        )
    }

    private fun showToast(resId: Int) {
        _toastResId.value = resId
    }

    private fun clearItems() {
        _items.value = mutableListOf()
    }

    private fun completeSearch() {
        _searchCompleteYN.value = true
    }

    private fun startLoading() {
        _loadingYN.postValue(true)
    }

    private fun stopLoading() {
        _loadingYN.postValue(false)
    }

    private fun finishSearchNoResult() {
        stopLoading()
        clearItems()
        completeSearch()
    }

    fun hasNextPage(): Boolean =
        _resultInfo.value?.let {
            return it[IS_END] != TRUE
        } ?: false

    fun searchImage(page: Int) {
        startLoading()

        searchWord.value?.let { searchWord ->
            addDisposable(
                searchImageUseCase(searchWord = searchWord, page = page)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            val isEnd = if (it.meta.is_end) {
                                TRUE
                            } else {
                                FALSE
                            }
                            _resultInfo.value = mapOf(
                                IS_END to isEnd,
                                PAGEABLE_COUNT to it.meta.pageable_count,
                                TOTAL_COUNT to it.meta.total_count
                            )

                            if (it.meta.total_count == 0) {
                                showToast(R.string.no_search_result)
                                finishSearchNoResult()
                                return@subscribe
                            }

                            when (page) {
                                FIRST_ITEM -> {
                                    clearItems()
                                    _items.value = it.documents.map { document ->
                                        mapOf(
                                            IMAGE_URL to document.image_url
                                        )
                                    }
                                    completeSearch()
                                }
                                else -> {
                                    val newItems = it.documents.map { document ->
                                        mapOf(
                                            IMAGE_URL to document.image_url
                                        )
                                    }
                                    val itemsArrayList = arrayListOf<Map<String, String>>()
                                    _items.value?.let { oldItems ->
                                        itemsArrayList.addAll(oldItems)
                                    }
                                    itemsArrayList.addAll(newItems)
                                    _items.value = itemsArrayList
                                }
                            }
                            stopLoading()
                        },
                        {
                            showToast(R.string.an_error_has_occurred)
                            finishSearchNoResult()
                        }
                    )
            )
        } ?: showToast(R.string.please_enter_the_exact_search_term)
    }

    fun changeEditText(text: String) {
        changeEditTextSubject.onNext(text)
    }

}