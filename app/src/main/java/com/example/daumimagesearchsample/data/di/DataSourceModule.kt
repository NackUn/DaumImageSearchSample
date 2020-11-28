package com.example.daumimagesearchsample.data.di

import com.example.daumimagesearchsample.data.api.KakaoApi
import com.example.daumimagesearchsample.data.source.kakao.KakaoDataSource
import com.example.daumimagesearchsample.data.source.kakao.KakaoRemoteDataSource
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import retrofit2.Retrofit

val dataSourceModule = module {

    single<KakaoApi> { get<Retrofit> { parametersOf("https://dapi.kakao.com") }.create(KakaoApi::class.java) }
    single<KakaoDataSource> { KakaoRemoteDataSource(get()) }
}