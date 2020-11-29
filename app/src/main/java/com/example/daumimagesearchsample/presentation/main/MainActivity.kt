package com.example.daumimagesearchsample.presentation.main

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.daumimagesearchsample.BR
import com.example.daumimagesearchsample.R
import com.example.daumimagesearchsample.databinding.MainActivityBinding
import com.example.daumimagesearchsample.databinding.MainItemBinding
import com.example.daumimagesearchsample.presentation.base.BaseActivity
import com.example.daumimagesearchsample.presentation.base.BaseRecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<MainActivityBinding, MainViewModel>(
    R.layout.main_activity
) {
    override val vm: MainViewModel by viewModel()

    private val mainAdapter =
        object : BaseRecyclerView.BaseAdapter<List<Map<String, String>>, MainItemBinding>(
            R.layout.main_item,
            BR.mainItem
        ) {}

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun setRecyclerView() {
        binding.mainRecyclerView.run {
            layoutManager = LinearLayoutManager(this@MainActivity).also {
                it.orientation = LinearLayoutManager.HORIZONTAL
            }

            val scrollListener = object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val lastVisibleItem = (layoutManager as LinearLayoutManager)
                        .findLastCompletelyVisibleItemPosition()

                    if ((layoutManager as LinearLayoutManager).itemCount <= lastVisibleItem + 1) {
                        if (vm.hasNextPage()) {
                            vm.currentPage.value?.let {
                                val nextPage = it + 1
                                vm.searchImage(nextPage)
                                vm.currentPage.value = nextPage
                            }
                        } else {
                            showToast("마지막 이미지입니다. 더 이상 검색결과가 없습니다.")
                        }
                    }
                }
            }

            addOnScrollListener(scrollListener)
            adapter = mainAdapter
        }
    }

    private fun setListener() {
        binding.mainEtSearch.addTextChangedListener(
            object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    vm.changeEditText(s.toString())
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                }
            }
        )
    }

    private fun closeKeyboard() {
        this@MainActivity.currentFocus?.let {
            val imm: InputMethodManager =
                getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    private fun startLoading() {
        binding.mainProgressBar.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        binding.mainProgressBar.visibility = View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setRecyclerView()
        setListener()
    }

    override fun initViewModel() {
        binding.setVariable(BR.mainVM, vm)

        val toastMsgObserver = Observer<String> {
            showToast(it)
        }
        vm.toastMsg.observe(this, toastMsgObserver)

        val searchCompleteYNObserver = Observer<Boolean> {
            if (it) {
                binding.mainRecyclerView.scrollToPosition(0)
                closeKeyboard()
            }
        }
        vm.searchCompleteYN.observe(this, searchCompleteYNObserver)

        val loadingYNObserver = Observer<Boolean> {
            if (it) {
                startLoading()
            } else {
                stopLoading()
            }
        }
        vm.loadingYN.observe(this, loadingYNObserver)
    }

}