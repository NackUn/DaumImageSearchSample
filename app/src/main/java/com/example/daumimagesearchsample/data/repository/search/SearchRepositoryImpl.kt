package com.example.daumimagesearchsample.data.repository.search

import com.example.daumimagesearchsample.data.model.KakaoImageSearchResponse
import com.example.daumimagesearchsample.data.source.kakao.KakaoDataSource
import io.reactivex.Observable

class SearchRepositoryImpl(
    private val kakaoDataSource: KakaoDataSource
) : SearchRepository {

    override fun requestImageSearch(
        searchWord: String,
        sort: String,
        page: Int,
        size: Int
    ): Observable<KakaoImageSearchResponse> =
        kakaoDataSource.requestImageSearch(searchWord, sort, page, size)
}