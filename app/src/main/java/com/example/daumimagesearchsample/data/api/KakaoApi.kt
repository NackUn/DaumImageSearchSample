package com.example.daumimagesearchsample.data.api

import com.example.daumimagesearchsample.Keys
import com.example.daumimagesearchsample.data.model.KakaoImageSearchResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface KakaoApi {

    @Headers(
        "Authorization: KakaoAK ${Keys.KAKAO_REST_API_KEY}"
    )
    @GET("v2/search/image")
    fun requestImageSearch(
        @Query("query") searchWord: String,
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Observable<KakaoImageSearchResponse>
}