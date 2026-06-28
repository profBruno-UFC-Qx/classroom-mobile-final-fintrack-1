package com.example.fintrack

import android.app.Application
import com.example.fintrack.data.api.NewsApiService
import com.example.fintrack.data.local.AppDatabase
import com.example.fintrack.data.repository.NewsRepository
import com.example.fintrack.data.repository.TransactionRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FintrackApplication : Application() {
    val database by lazy { AppDatabase.getInstance(this) }
    val repository by lazy { TransactionRepository(database.transactionDao()) }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(NewsApiService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val newsApiService by lazy { retrofit.create(NewsApiService::class.java) }
    val newsRepository by lazy { NewsRepository(newsApiService) }

    override fun onCreate() {
        super.onCreate()
    }
}
