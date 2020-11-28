package com.example.daumimagesearchsample.domain.usecase

import com.example.daumimagesearchsample.data.model.KakaoImageSearchResponse
import com.example.daumimagesearchsample.data.repository.search.SearchRepository
import io.reactivex.Observable

class SearchImageUseCase(private val repository: SearchRepository) {

    operator fun invoke(
        searchWord: String,
        sort: String = "accuracy",
        page: Int = 1,
        size: Int = 80
    ): Observable<KakaoImageSearchResponse> =
        repository.requestImageSearch(searchWord, sort, page, size)
}