package com.example.daumimagesearchsample.data.source.kakao

import com.example.daumimagesearchsample.data.model.KakaoImageSearchResponse
import io.reactivex.Observable

interface KakaoDataSource {

    fun requestImageSearch(
        searchWord: String,
        sort: String,
        page: Int,
        size: Int
    ): Observable<KakaoImageSearchResponse>
}