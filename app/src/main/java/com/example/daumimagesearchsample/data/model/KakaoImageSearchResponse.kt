package com.example.daumimagesearchsample.data.model

data class KakaoImageSearchResponse(
    val documents: List<Document>,
    val meta: Meta
)