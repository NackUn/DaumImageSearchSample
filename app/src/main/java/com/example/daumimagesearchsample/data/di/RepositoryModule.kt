package com.example.daumimagesearchsample.data.di

import com.example.daumimagesearchsample.data.repository.search.SearchRepository
import com.example.daumimagesearchsample.data.repository.search.SearchRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {

    single<SearchRepository> {
        SearchRepositoryImpl(
            get()
        )
    }
}