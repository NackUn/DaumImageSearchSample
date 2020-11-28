package com.example.daumimagesearchsample.data.repository.search

import com.example.daumimagesearchsample.data.model.KakaoImageSearchResponse
import io.reactivex.Observable

interface SearchRepository {

    fun requestImageSearch(
        searchWord: String,
        sort: String,
        page: Int,
        size: Int
    ): Observable<KakaoImageSearchResponse>
}