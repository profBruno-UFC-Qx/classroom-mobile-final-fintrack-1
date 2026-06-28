package com.example.fintrack.data.api

import com.example.fintrack.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("news")
    suspend fun getFinancialNews(
        @Query("apikey") apiKey: String,
        @Query("q") query: String = "finanças OR economia OR investimentos",
        @Query("language") language: String = "pt",
        @Query("category") category: String = "business"
    ): NewsResponse

    companion object {
        const val BASE_URL = "https://newsdata.io/api/1/"
    }
}
