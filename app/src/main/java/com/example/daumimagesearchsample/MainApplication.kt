package com.example.daumimagesearchsample

import android.app.Application
import com.example.daumimagesearchsample.data.di.dataSourceModule
import com.example.daumimagesearchsample.data.di.networkModule
import com.example.daumimagesearchsample.data.di.repositoryModule
import com.example.daumimagesearchsample.domain.di.useCaseModule
import com.example.daumimagesearchsample.presentation.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            modules(
                listOf(
                    viewModelModule,
                    networkModule,
                    dataSourceModule,
                    repositoryModule,
                    useCaseModule
                )
            )
        }
    }
}