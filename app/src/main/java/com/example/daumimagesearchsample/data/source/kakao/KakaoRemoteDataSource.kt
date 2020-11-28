package com.example.daumimagesearchsample.data.source.kakao

import com.example.daumimagesearchsample.data.api.KakaoApi
import com.example.daumimagesearchsample.data.model.KakaoImageSearchResponse
import io.reactivex.Observable

class KakaoRemoteDataSource(
    private val retrofit: KakaoApi
) : KakaoDataSource {

    override fun requestImageSearch(
        searchWord: String,
        sort: String,
        page: Int,
        size: Int
    ): Observable<KakaoImageSearchResponse> =
        retrofit.requestImageSearch(searchWord, sort, page, size)
}