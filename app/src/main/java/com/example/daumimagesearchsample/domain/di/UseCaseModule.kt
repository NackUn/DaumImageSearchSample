package com.example.daumimagesearchsample.domain.di

import com.example.daumimagesearchsample.domain.usecase.SearchImageUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single {
        SearchImageUseCase(get())
    }
}